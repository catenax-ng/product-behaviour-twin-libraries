package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.intern;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.TelematicsDataTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.converter.TelematicsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.model.TelematicsDataDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.converter.VehicleConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.model.VehicleDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.Sync;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.TelematicsData;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.Vehicle;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleTableIntern extends RawTableBase {
    @PersistenceContext EntityManager entityManager;

    @Autowired private RawVehicleRepository rawVehicleRepository;
    @Autowired private TelematicsDataTable telematicsDataTable;
    @Autowired private SyncTable syncTable;
    @Autowired private VehicleConverter vehicleConverter;
    @Autowired private TelematicsDataConverter telematicsDataConverter;

    @TransactionDefaultUseExisting
    public void registerVehicleExternTransaction(@NotNull final VehicleInfo newVehicleInfo)
            throws OemDatabaseException {
        try {
            /* Init with sync counter = 0 to avoid recalculation of empty vehicles. */
            rawVehicleRepository.register(newVehicleInfo.vehilceId(), newVehicleInfo.van(),
                    newVehicleInfo.gearboxId(), newVehicleInfo.productionDate(), 0);
        }
        catch(Exception exception) {
            throw failed("Failed to register vehicle!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void registerVehicleNewTransaction(@NotNull VehicleInfo newVehicleInfo) throws OemDatabaseException {
        registerVehicleExternTransaction(newVehicleInfo);
    }

    @TransactionDefaultUseExisting
    public void deleteAllExternTransaction() throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteAll();
        } catch (final Exception exception) {
            throw failed("Deleting all vehicles failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternTransaction();
    }

    @TransactionSerializableUseExisting
    public void appendTelematicsDataByIdExternTransaction(@NotNull final String id,
                                                          @NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        appendTelematicsData(getByIdWithTelematicsDataNewTransaction(id), newTelematicsData);
    }

    @TransactionSerializableCreateNew
    public void appendTelematicsDataByIdNewTransaction(@NotNull final String id,
                                                       @NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        appendTelematicsDataByIdExternTransaction(id, newTelematicsData);
    }
    @TransactionSerializableUseExisting
    public void appendTelematicsDataByVanExternTransaction(@NotNull final String van,
                                                           @NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        appendTelematicsData(getByVanWithTelematicsDataNewTransaction(van), newTelematicsData);
    }

    @TransactionSerializableCreateNew
    public void appendTelematicsDataByVanNewTransaction(@NotNull final String van,
                                                        @NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        appendTelematicsDataByVanExternTransaction(van, newTelematicsData);
    }

    private void appendTelematicsData(@NotNull final Vehicle vehicle,
                                      @NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        if( telematicsDataIsNewer(newTelematicsData, vehicle.getNewestTelematicsData()) ) {
            final String newTelematicsId = telematicsDataTable.uploadTelematicsDataGetIdExternTransaction(
                    newTelematicsData);

            try {
                final Sync sync = syncTable.setCurrentDefaultExternTransaction();
                rawVehicleRepository.updateNewestTelematicsIdByVehicleId(vehicle.getVehicleId(),
                        newTelematicsId, sync.getSyncCounter());
            } catch (final Exception exception) {
                throw failed("Appending telematics data failed!", exception);
            }
        }
    }

    private boolean telematicsDataIsNewer(@NotNull final InputTelematicsData newTelematicsData,
                                          @Nullable final TelematicsData telematicsData) {
        if(telematicsData == null) {
            return true;
        }

        return newTelematicsData.state().creationTimestamp().isAfter(telematicsData.getCreationTimestamp());
    }

    @TransactionDefaultUseExisting
    public void deleteByIdExternTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteByVehilceId(id);
        }
        catch(final Exception exception) {
            throw failed("Deleting vehicle by id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        deleteByIdExternTransaction(id);
    }

    @TransactionDefaultUseExisting
    public void deleteByVanExternTransaction(@NotNull final String van) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteByVan(van);
        }
        catch(Exception exception) {
            throw failed("Deleting vehicle by van failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteByVanNewTransaction(@NotNull final String van) throws OemDatabaseException {
        deleteByVanExternTransaction(van);
    }

    @TransactionDefaultUseExisting
    public Vehicle getByIdExternTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        try {
            return vehicleConverter.toDTO(rawVehicleRepository.queryByVehicleId(vehicleId));
        } catch (final Exception exception) {
            throw failed("Querying vehicle by id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public Vehicle getByIdNewTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        return getByIdExternTransaction(vehicleId);
    }

    @TransactionDefaultUseExisting
    public Vehicle getByIdWithTelematicsDataExternTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData("v.vehicle_id=:vehicle_id")
                    .setParameter("vehicle_id", vehicleId);
            return getSingle(executeQueryVehicleTelematicsData(query));
        } catch(final Exception exception) {
            throw failed("Querying vehicle by id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public Vehicle getByIdWithTelematicsDataNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return getByIdWithTelematicsDataExternTransaction(vehicleId);
    }

    @TransactionDefaultUseExisting
    public Vehicle findByVanExternTransaction(@NotNull final String van) throws OemDatabaseException {
        try {
            return vehicleConverter.toDTO(rawVehicleRepository.queryByVan(van));
        } catch(final Exception exception) {
            throw failed("Querying vehicle by van failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public Vehicle findByVanNewTransaction(@NotNull final String van) throws OemDatabaseException {
        return findByVanExternTransaction(van);
    }

    @TransactionDefaultUseExisting
    public Vehicle getByVanWithTelematicsDataExternTransaction(@NotNull final String van) throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData("v.van=:van")
                    .setParameter("van", van);
            return getSingle(executeQueryVehicleTelematicsData(query));
        } catch(final Exception exception) {
            throw failed("Querying vehicle by van failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public Vehicle getByVanWithTelematicsDataNewTransaction(@NotNull final String van) throws OemDatabaseException {
        return getByVanWithTelematicsDataExternTransaction(van);
    }

    @TransactionDefaultUseExisting
    public Vehicle getByGearboxIdExternTransaction(@NotNull final String gearboxId) throws OemDatabaseException {
        try {
            return vehicleConverter.toDTO(rawVehicleRepository.queryByGearboxId(gearboxId));
        } catch(final Exception exception) {
            throw failed("Querying vehicle by gearbox_id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public Vehicle getByGearboxIdNewTransaction(@NotNull final String gearboxId) throws OemDatabaseException {
        return getByGearboxIdExternTransaction(gearboxId);
    }

    @TransactionDefaultUseExisting
    public Vehicle getByGearboxIdWithTelematicsDataExternTransaction(@NotNull final String gearboxId)
            throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData(
                    "v.gearbox_id=:gearbox_id")
                    .setParameter("gearbox_id", gearboxId);
            return getSingle(executeQueryVehicleTelematicsData(query));
        } catch(final Exception exception) {
            throw failed("Querying vehicle by gearbox_id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public Vehicle getByGearboxIdWithTelematicsDataNewTransaction(@NotNull final String gearboxId)
            throws OemDatabaseException {
        return getByGearboxIdWithTelematicsDataExternTransaction(gearboxId);
    }

    private Vehicle queryAndAppendTelematicsData(final VehicleDAO vehicleFromDB) throws OemDatabaseException {
        try {
            final Vehicle vehicle = new Vehicle();
            final String newestTelematicsId = vehicleFromDB.getNewestTelematicsId();

            if(newestTelematicsId != null) {
                vehicle.setNewestTelematicsData(telematicsDataTable.getByIdExternTransaction(
                        vehicleFromDB.getNewestTelematicsId()));
            }

            return vehicle;
        } catch(final Exception exception) {
            throw failed("Querying vehicle by van failed!", exception);
        }
    }

    @TransactionDefaultUseExisting
    public List<Vehicle> getAllExternTransaction() throws OemDatabaseException {
        try {
            return vehicleConverter.toDTO(rawVehicleRepository.queryAll());
        }
        catch(Exception exception) {
            throw failed("Querying vehicles failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<Vehicle> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternTransaction();
    }

    @TransactionDefaultUseExisting
    public List<Vehicle> getAllWithTelematicsDataExternTransaction() throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData();
            return executeQueryVehicleTelematicsData(query);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<Vehicle> getAllWithTelematicsDataNewTransaction() throws OemDatabaseException {
        return getAllWithTelematicsDataExternTransaction();
    }

    @TransactionDefaultUseExisting
    public List<Vehicle> getUpdatedSinceExternTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        try {
            return vehicleConverter.toDTO(rawVehicleRepository.queryUpdatedSince(updatedSince));
        }
        catch(Exception exception) {
            throw failed("Querying vehicles failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<Vehicle> getUpdatedSinceNewTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return getUpdatedSinceExternTransaction(updatedSince);
    }

    @TransactionDefaultUseExisting
    public List<Vehicle> getUpdatedSinceWithTelematicsDataExternTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData(
                    "v.update_timestamp>=:updated_since")
                    .setParameter("updated_since", updatedSince);
            return executeQueryVehicleTelematicsData(query);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<Vehicle> getUpdatedSinceWithTelematicsDataNewTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return getUpdatedSinceWithTelematicsDataExternTransaction(updatedSince);
    }

    @TransactionDefaultUseExisting
    public List<Vehicle> getProducedBetweenExternTransaction(@NotNull final Instant producedSince,
                                                             @NotNull final Instant producedUntil)
            throws OemDatabaseException {
        try {
            return vehicleConverter.toDTO(rawVehicleRepository.queryByProductionDate(producedSince, producedUntil));
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<Vehicle> getProducedBetweenNewTransaction(@NotNull final Instant producedSince,
                                                          @NotNull final Instant producedUntil)
            throws OemDatabaseException {
        return getProducedBetweenExternTransaction(producedSince, producedUntil);
    }

    @TransactionDefaultUseExisting
    public List<Vehicle> getProducedBetweenWithTelematicsDataExternTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData(
                    "v.production_date>=:produced_since AND v.production_date<=:produced_until")
                    .setParameter("produced_since", producedSince)
                    .setParameter("produced_until", producedUntil);
            return executeQueryVehicleTelematicsData(query);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<Vehicle> getProducedBetweenWithTelematicsDataNewTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemDatabaseException {
        return getProducedBetweenWithTelematicsDataExternTransaction(producedSince, producedUntil);
    }

    @TransactionSerializableUseExisting
    public List<Vehicle> getSyncCounterSinceExternTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        try {
            return vehicleConverter.toDTO(rawVehicleRepository.queryUpdatedSinceSyncCounter(syncCounter));
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles by sync counter failed!", exception);
        }    }

    @TransactionSerializableCreateNew
    public List<Vehicle> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return getSyncCounterSinceExternTransaction(syncCounter);
    }

    @TransactionSerializableUseExisting
    public List<Vehicle> getSyncCounterSinceWithTelematicsDataExternTransaction(@NotNull long syncCounter)
            throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData(
                    "v.sync_counter>=:sync_counter")
                    .setParameter("sync_counter", syncCounter);
            return executeQueryVehicleTelematicsData(query);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed!", exception);
        }
    }

    @TransactionSerializableCreateNew
    public List<Vehicle> getSyncCounterSinceWithTelematicsDataNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return getSyncCounterSinceWithTelematicsDataExternTransaction(syncCounter);
    }

    private List<Vehicle> executeQueryVehicleTelematicsData(@NotNull final NativeQuery<Object[]> query)
            throws OemDatabaseException {
        try {
            final List<Object[]> results = query.list();
            final List<Vehicle> vehicles = new ArrayList<>(results.size());

            results.stream().forEach((record) -> vehicles.add(
                    vehicleConverter.toDTOWithTelematicsData((VehicleDAO)record[0],
                    telematicsDataConverter.toDTO((TelematicsDataDAO)record[1]))));

            return vehicles;
        } catch(final Exception exception) {
            throw failed("Executing query failed!", exception);
        }
    }

    private NativeQuery<Object[]> createJoinQueryVehicleTelematicsData() throws OemDatabaseException {
        return createJoinQueryVehicleTelematicsData(null);
    }

    private NativeQuery<Object[]> createJoinQueryVehicleTelematicsData(@Nullable final String where)
            throws OemDatabaseException {
        String query = "SELECT {v.*}, {t.*} FROM vehicles v " +
                       "LEFT OUTER JOIN telematics_data t ON v.newest_telematics_id = t.id";

        if(where != null) {
            query += " WHERE " + where;
        }

        return createQuery(query).addEntity("v", VehicleDAO.class).addEntity("t", TelematicsDataDAO.class);
    }

    private NativeQuery<Object[]> createQuery(@NotNull final String query) throws OemDatabaseException {
        try {
            return (NativeQuery<Object[]>)((Session)this.entityManager.getDelegate()).createSQLQuery(query);
        }
        catch(Exception exception) {
            throw failed("Initializing query failed!", exception);
        }
    }

    private Vehicle getSingle(@Nullable final List<Vehicle> vehicles) throws OemDatabaseException {
        if(vehicles == null) {
            return null;
        }

        if(vehicles.isEmpty()) {
            return null;
        }

        if(vehicles.size() > 1) {
            throw failed("Found more than one vehicle with the same id!");
        }

        return vehicles.get(0);
    }
}
