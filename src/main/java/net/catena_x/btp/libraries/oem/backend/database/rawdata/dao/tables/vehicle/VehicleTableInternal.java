package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.util.StatusFromBammFunction;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.config.PersistenceRawDataConfiguration;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTableInternal;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.InputTelematicsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.TelematicsDataDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.TelematicsDataTableInternal;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Component
public class VehicleTableInternal extends RawTableBase {
    @PersistenceContext(unitName = PersistenceRawDataConfiguration.UNIT_NAME) EntityManager entityManager;

    @Autowired private RawVehicleRepository rawVehicleRepository;
    @Autowired private TelematicsDataTableInternal telematicsDataTable;
    @Autowired private SyncTableInternal syncTable;
    @Autowired private InputTelematicsDataConverter inputTelematicsDataConverter;

    private final Logger logger = LoggerFactory.getLogger(VehicleTableInternal.class);

    @RDTransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @RDTransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @RDTransactionDefaultUseExisting
    public void registerVehicleExternalTransaction(@NotNull final VehicleInfo newVehicleInfo)
            throws OemDatabaseException {
        try {
            /* Init with sync counter = 0 to avoid recalculation of empty vehicles. */
            rawVehicleRepository.register(newVehicleInfo.vehicleId(), newVehicleInfo.van(),
                    newVehicleInfo.gearboxId(), newVehicleInfo.productionDate(), 0);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Failed to register vehicle!", exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void registerVehicleNewTransaction(@NotNull VehicleInfo newVehicleInfo) throws OemDatabaseException {
        registerVehicleExternalTransaction(newVehicleInfo);
    }

    @RDTransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all vehicles failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternalTransaction();
    }

    @RDTransactionSerializableUseExisting
    public void appendTelematicsDataExternalTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        final VehicleWithTelematicsDataDAO vehicle =
                getByIdWithTelematicsDataExternalTransaction(newTelematicsData.vehicleId());

        if(vehicle == null) {
            throw new OemDatabaseException("Vehicle " + newTelematicsData.vehicleId() + " does not exist!");
        }

        appendTelematicsData(vehicle, newTelematicsData);
    }

    @RDTransactionSerializableCreateNew
    public void appendTelematicsDataNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        appendTelematicsDataExternalTransaction(newTelematicsData);
    }

    private void appendTelematicsData(@NotNull final VehicleWithTelematicsDataDAO vehicle,
                                      @NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {

        final InputTelematicsData telematicsDatatoAdd = telematicsDataIfNewer(newTelematicsData, vehicle.telematicsData());

        if(telematicsDatatoAdd != null) {
            final String newTelematicsId = telematicsDataTable.updateTelematicsDataGetIdExternalTransaction(
                    telematicsDatatoAdd);

            try {
                final SyncDAO sync = syncTable.setCurrentExternalTransaction(SyncTableInternal.DEFAULT_ID);
                rawVehicleRepository.updateNewestTelematicsIdByVehicleId(vehicle.vehicle().getVehicleId(),
                        newTelematicsId, sync.getSyncCounter());
            } catch (final Exception exception) {
                logger.error(exception.getMessage());
                exception.printStackTrace();
                throw failed("Appending telematics data failed! " + exception.getMessage(), exception);
            }
        }
    }

    private InputTelematicsData telematicsDataIfNewer(@NotNull final InputTelematicsData newTelematicsData,
                                                      @Nullable final TelematicsDataDAO telematicsData)
            throws OemDatabaseException {

        if(telematicsData == null) {
            return newTelematicsData;
        }

        final InputTelematicsData existingTelematicsData = inputTelematicsDataConverter.toDTO(telematicsData);

        assertCompatible(existingTelematicsData, newTelematicsData);

        boolean newer = replaceNewer(existingTelematicsData.loadSpectra(), newTelematicsData.loadSpectra(),
                (item) -> { return ((ClassifiedLoadSpectrum)item).getMetadata().getStatus().getDate(); });

        newer = newer || replaceNewer(existingTelematicsData.adaptionValues(), newTelematicsData.adaptionValues(),
                (item) -> { return  ((AdaptionValues)item).getStatus().getDate(); });

            return newer? newTelematicsData : null;
    }

    private <T> boolean replaceNewer(@NotNull final List<T> existingData, @NotNull final List<T> newData,
                                     @NotNull final StatusFromBammFunction getStatus) {
        boolean newer = false;

        int size = existingData.size();
        for (int i = 0; i < size; i++) {
            final T existingItem = existingData.get(i);
            final T newItem = newData.get(i);

            if(getStatus.getTimestamp(newItem).isAfter(getStatus.getTimestamp(existingItem))) {
                newer = true;
            }
            else if(getStatus.getTimestamp(newItem).isBefore(getStatus.getTimestamp(existingItem))) {
                newData.set(i, existingItem);
            }
        }

        return newer;
    }

    private void assertCompatible(@NotNull final InputTelematicsData telematicsData1,
                                  @NotNull final InputTelematicsData telematicsData2) throws OemDatabaseException {

        if((telematicsData1.loadSpectra() == null) != (telematicsData2.loadSpectra() == null)) {
            throw new OemDatabaseException("Loadspectra incompatible!");
        }

        if(telematicsData1.loadSpectra() != null) {
            if (telematicsData1.loadSpectra().size() != telematicsData2.loadSpectra().size()) {
                throw new OemDatabaseException("Loadspectra incompatible!");
            }
        }

        if((telematicsData1.adaptionValues() == null) != (telematicsData2.adaptionValues() == null)) {
            throw new OemDatabaseException("Adaption values incompatible!");
        }

        if(telematicsData1.adaptionValues() != null) {
            if (telematicsData1.adaptionValues().size() != telematicsData2.adaptionValues().size()) {
                throw new OemDatabaseException("Adaption values incompatible!");
            }
        }
    }

    @RDTransactionDefaultUseExisting
    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteByVehilceId(id);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting vehicle by id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        deleteByIdExternalTransaction(id);
    }

    @RDTransactionDefaultUseExisting
    public void deleteByVanExternalTransaction(@NotNull final String van) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteByVan(van);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting vehicle by van failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteByVanNewTransaction(@NotNull final String van) throws OemDatabaseException {
        deleteByVanExternalTransaction(van);
    }

    @RDTransactionDefaultUseExisting
    public VehicleDAO getByIdExternalTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        try {
            return rawVehicleRepository.queryByVehicleId(vehicleId);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying vehicle by id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public VehicleDAO getByIdNewTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        return getByIdExternalTransaction(vehicleId);
    }

    @RDTransactionDefaultUseExisting
    public VehicleWithTelematicsDataDAO getByIdWithTelematicsDataExternalTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData("v.vehicle_id=:vehicle_id")
                    .setParameter("vehicle_id", vehicleId);
            return getSingle(executeQueryVehicleTelematicsData(query));
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying vehicle by id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public VehicleWithTelematicsDataDAO getByIdWithTelematicsDataNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return getByIdWithTelematicsDataExternalTransaction(vehicleId);
    }

    @RDTransactionDefaultUseExisting
    public VehicleDAO findByVanExternalTransaction(@NotNull final String van) throws OemDatabaseException {
        try {
            return rawVehicleRepository.queryByVan(van);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying vehicle by van failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public VehicleDAO findByVanNewTransaction(@NotNull final String van) throws OemDatabaseException {
        return findByVanExternalTransaction(van);
    }

    @RDTransactionDefaultUseExisting
    public VehicleWithTelematicsDataDAO getByVanWithTelematicsDataExternalTransaction(@NotNull final String van)
            throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData("v.van=:van")
                    .setParameter("van", van);
            return getSingle(executeQueryVehicleTelematicsData(query));
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying vehicle by van failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public VehicleWithTelematicsDataDAO getByVanWithTelematicsDataNewTransaction(
            @NotNull final String van) throws OemDatabaseException {
        return getByVanWithTelematicsDataExternalTransaction(van);
    }

    @RDTransactionDefaultUseExisting
    public VehicleDAO getByGearboxIdExternalTransaction(@NotNull final String gearboxId) throws OemDatabaseException {
        try {
            return rawVehicleRepository.queryByGearboxId(gearboxId);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying vehicle by gearbox_id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public VehicleDAO getByGearboxIdNewTransaction(@NotNull final String gearboxId) throws OemDatabaseException {
        return getByGearboxIdExternalTransaction(gearboxId);
    }

    @RDTransactionDefaultUseExisting
    public VehicleWithTelematicsDataDAO getByGearboxIdWithTelematicsDataExternalTransaction(
            @NotNull final String gearboxId) throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData(
                    "v.gearbox_id=:gearbox_id")
                    .setParameter("gearbox_id", gearboxId);
            return getSingle(executeQueryVehicleTelematicsData(query));
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying vehicle by gearbox_id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public VehicleWithTelematicsDataDAO getByGearboxIdWithTelematicsDataNewTransaction(@NotNull final String gearboxId)
            throws OemDatabaseException {
        return getByGearboxIdWithTelematicsDataExternalTransaction(gearboxId);
    }

    private VehicleWithTelematicsDataDAO queryAndAppendTelematicsData(final VehicleDAO vehicleFromDB)
            throws OemDatabaseException {
        try {
            final String newestTelematicsId = vehicleFromDB.getNewestTelematicsId();

            if(newestTelematicsId == null) {
                return new VehicleWithTelematicsDataDAO(vehicleFromDB, null);
            }

            return new VehicleWithTelematicsDataDAO(vehicleFromDB,
                    telematicsDataTable.getByIdExternalTransaction(
                            vehicleFromDB.getNewestTelematicsId()));
        } catch(final Exception exception) {
            throw failed("Querying vehicle by van failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultUseExisting
    public List<VehicleDAO> getAllExternalTransaction() throws OemDatabaseException {
        try {
            return rawVehicleRepository.queryAll();
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<VehicleDAO> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternalTransaction();
    }

    @RDTransactionDefaultUseExisting
    public List<VehicleWithTelematicsDataDAO> getAllWithTelematicsDataExternalTransaction() throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData();
            return executeQueryVehicleTelematicsData(query);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<VehicleWithTelematicsDataDAO> getAllWithTelematicsDataNewTransaction() throws OemDatabaseException {
        return getAllWithTelematicsDataExternalTransaction();
    }

    @RDTransactionDefaultUseExisting
    public List<VehicleDAO> getUpdatedSinceExternalTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        try {
            return rawVehicleRepository.queryUpdatedSince(updatedSince);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<VehicleDAO> getUpdatedSinceNewTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return getUpdatedSinceExternalTransaction(updatedSince);
    }

    @RDTransactionDefaultUseExisting
    public List<VehicleWithTelematicsDataDAO> getUpdatedSinceWithTelematicsDataExternalTransaction(
            @NotNull final Instant updatedSince) throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData(
                    "v.update_timestamp>=:updated_since")
                    .setParameter("updated_since", updatedSince);
            return executeQueryVehicleTelematicsData(query);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<VehicleWithTelematicsDataDAO> getUpdatedSinceWithTelematicsDataNewTransaction(
            @NotNull final Instant updatedSince) throws OemDatabaseException {
        return getUpdatedSinceWithTelematicsDataExternalTransaction(updatedSince);
    }

    @RDTransactionDefaultUseExisting
    public List<VehicleDAO> getProducedBetweenExternalTransaction(@NotNull final Instant producedSince,
                                                                @NotNull final Instant producedUntil)
            throws OemDatabaseException {
        try {
            return rawVehicleRepository.queryByProductionDate(producedSince, producedUntil);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<VehicleDAO> getProducedBetweenNewTransaction(@NotNull final Instant producedSince,
                                                          @NotNull final Instant producedUntil)
            throws OemDatabaseException {
        return getProducedBetweenExternalTransaction(producedSince, producedUntil);
    }

    @RDTransactionDefaultUseExisting
    public List<VehicleWithTelematicsDataDAO> getProducedBetweenWithTelematicsDataExternalTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData(
                    "v.production_date>=:produced_since AND v.production_date<=:produced_until")
                    .setParameter("produced_since", producedSince)
                    .setParameter("produced_until", producedUntil);
            return executeQueryVehicleTelematicsData(query);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<VehicleWithTelematicsDataDAO> getProducedBetweenWithTelematicsDataNewTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemDatabaseException {
        return getProducedBetweenWithTelematicsDataExternalTransaction(producedSince, producedUntil);
    }

    @RDTransactionSerializableUseExisting
    public List<VehicleDAO> getSyncCounterSinceExternalTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        try {
            return rawVehicleRepository.queryUpdatedSinceSyncCounter(syncCounter);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles by sync counter failed! " + exception.getMessage(), exception);
        }    }

    @RDTransactionSerializableCreateNew
    public List<VehicleDAO> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return getSyncCounterSinceExternalTransaction(syncCounter);
    }

    @RDTransactionSerializableUseExisting
    public List<VehicleWithTelematicsDataDAO> getSyncCounterSinceWithTelematicsDataExternalTransaction(
            @NotNull long syncCounter)
            throws OemDatabaseException {
        try {
            final NativeQuery<Object[]> query = createJoinQueryVehicleTelematicsData(
                    "v.sync_counter>=:sync_counter")
                    .setParameter("sync_counter", syncCounter);
            return executeQueryVehicleTelematicsData(query);
        }
        catch(final Exception exception) {
            throw failed("Querying vehicles failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionSerializableCreateNew
    public List<VehicleWithTelematicsDataDAO> getSyncCounterSinceWithTelematicsDataNewTransaction(
            @NotNull final long syncCounter)
            throws OemDatabaseException {
        return getSyncCounterSinceWithTelematicsDataExternalTransaction(syncCounter);
    }

    private List<VehicleWithTelematicsDataDAO> executeQueryVehicleTelematicsData(
            @NotNull final NativeQuery<Object[]> query) throws OemDatabaseException {
        try {
            final List<Object[]> results = query.list();
            final List<VehicleWithTelematicsDataDAO> vehicles = new ArrayList<>(results.size());

            results.stream().forEach((record) -> vehicles.add(
                    new VehicleWithTelematicsDataDAO((VehicleDAO)record[0], (TelematicsDataDAO)record[1])));

            return vehicles;
        } catch(final Exception exception) {
            throw failed("Executing query failed! " + exception.getMessage(), exception);
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
            return ((Session)this.entityManager.getDelegate()).createNativeQuery(query, Object[].class);
        }
        catch(final Exception exception) {
            throw failed("Initializing query failed! " + exception.getMessage(), exception);
        }
    }

    private VehicleWithTelematicsDataDAO getSingle(@Nullable final List<VehicleWithTelematicsDataDAO> vehicles)
            throws OemDatabaseException {
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
