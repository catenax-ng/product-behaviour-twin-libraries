package net.catena_x.btp.libraries.oem.backend.model.dto.vehicle;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.VehicleTableInternal;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.VehicleWithTelematicsDataDAO;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@Component
public class VehicleTable extends RawTableBase {
    @Autowired private VehicleTableInternal internal;
    @Autowired private VehicleConverter vehicleConverter;

    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableNewTransaction(function);
    }

    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableExternalTransaction(function);
    }

    public void registerVehicleNewTransaction(@NotNull final VehicleInfo newVehicleInfo) throws OemDatabaseException {
        internal.registerVehicleNewTransaction(newVehicleInfo);
    }

    public void registerVehicleExternalTransaction(@NotNull final VehicleInfo newVehicleInfo) throws OemDatabaseException {
        internal.registerVehicleExternalTransaction(newVehicleInfo);
    }

    public void deleteAllNewTransaction() throws OemDatabaseException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws OemDatabaseException {
        internal.deleteAllExternalTransaction();
    }

    public void appendTelematicsDataNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        internal.appendTelematicsDataNewTransaction(newTelematicsData);
    }

    public void appendTelematicsDataExternalTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        internal.appendTelematicsDataExternalTransaction(newTelematicsData);
    }

    public void deleteByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        internal.deleteByIdNewTransaction(id);
    }

    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        internal.deleteByIdExternalTransaction(id);
    }

    public void deleteByVanNewTransaction(@NotNull final String van) throws OemDatabaseException {
        internal.deleteByVanNewTransaction(van);
    }

    public void deleteByVanExternalTransaction(@NotNull final String van) throws OemDatabaseException {
        internal.deleteByVanExternalTransaction(van);
    }

    public Vehicle getByIdNewTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getByIdNewTransaction(vehicleId));
    }

    public Vehicle getByIdExternalTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getByIdExternalTransaction(vehicleId));
    }

    public Vehicle getByIdWithTelematicsDataNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(internal.getByIdWithTelematicsDataNewTransaction(vehicleId));
    }

    public Vehicle getByIdWithTelematicsDataExternalTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(internal.getByIdWithTelematicsDataExternalTransaction(vehicleId));
    }

    public Vehicle findByVanNewTransaction(@NotNull final String van) throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.findByVanNewTransaction(van));
    }

    public Vehicle findByVanExternalTransaction(@NotNull final String van) throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.findByVanExternalTransaction(van));
    }

    public Vehicle getByVanWithTelematicsDataNewTransaction(@NotNull final String van) throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(internal.getByVanWithTelematicsDataNewTransaction(van));
    }

    public Vehicle getByVanWithTelematicsDataExternalTransaction(@NotNull final String van) throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(internal.getByVanWithTelematicsDataExternalTransaction(van));
    }

    public List<Vehicle> getAllNewTransaction() throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getAllNewTransaction());
    }

    public List<Vehicle> getAllExternalTransaction() throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getAllExternalTransaction());
    }

    public List<Vehicle> getAllWithTelematicsDataNewTransaction() throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(internal.getAllWithTelematicsDataNewTransaction());
    }

    public List<Vehicle> getAllWithTelematicsDataExternalTransaction() throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(internal.getAllWithTelematicsDataExternalTransaction());
    }

    public List<Vehicle> getUpdatedSinceNewTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getUpdatedSinceNewTransaction(updatedSince));
    }

    public List<Vehicle> getUpdatedSinceExternalTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getUpdatedSinceExternalTransaction(updatedSince));
    }

    public List<Vehicle> getUpdatedSinceWithTelematicsDataNewTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                internal.getUpdatedSinceWithTelematicsDataNewTransaction(updatedSince));
    }

    public List<Vehicle> getUpdatedSinceWithTelematicsDataExternalTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                internal.getUpdatedSinceWithTelematicsDataExternalTransaction(updatedSince));
    }

    public List<Vehicle> getProducedBetweenNewTransaction(@NotNull final Instant producedSince,
                                                          @NotNull final Instant producedUntil)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getProducedBetweenNewTransaction(producedSince, producedUntil));
    }

    public List<Vehicle> getProducedBetweenExternalTransaction(@NotNull final Instant producedSince,
                                                             @NotNull final Instant producedUntil)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getProducedBetweenExternalTransaction(producedSince, producedUntil));
    }

    public List<Vehicle> getProducedBetweenWithTelematicsDataNewTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                internal.getProducedBetweenWithTelematicsDataNewTransaction(producedSince, producedUntil));
    }

    public List<Vehicle> getProducedBetweenWithTelematicsDataExternalTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                internal.getProducedBetweenWithTelematicsDataExternalTransaction(producedSince, producedUntil));
    }

    public List<Vehicle> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getSyncCounterSinceNewTransaction(syncCounter));
    }

    public List<Vehicle> getSyncCounterSinceExternalTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(internal.getSyncCounterSinceExternalTransaction(syncCounter));
    }

    public List<Vehicle> getSyncCounterSinceWithTelematicsDataNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        final List<VehicleWithTelematicsDataDAO> result =
                internal.getSyncCounterSinceWithTelematicsDataNewTransaction(syncCounter);
        return vehicleConverter.toDTOWithTelematicsData(result);
    }

    public List<Vehicle> getSyncCounterSinceWithTelematicsDataExternalTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                internal.getSyncCounterSinceWithTelematicsDataExternalTransaction(syncCounter));
    }
}
