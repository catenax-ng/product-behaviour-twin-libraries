package net.catena_x.btp.libraries.oem.backend.model.dto.vehicle;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.VehicleTableIntern;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class VehicleTable extends RawTableBase {
    @Autowired private VehicleTableIntern intern;
    @Autowired private VehicleConverter vehicleConverter;

    public void registerVehicleNewTransaction(@NotNull final VehicleInfo newVehicleInfo) throws OemDatabaseException {
        intern.registerVehicleNewTransaction(newVehicleInfo);
    }

    public void registerVehicleExternTransaction(@NotNull final VehicleInfo newVehicleInfo) throws OemDatabaseException {
        intern.registerVehicleExternTransaction(newVehicleInfo);
    }

    public void deleteAllNewTransaction() throws OemDatabaseException {
        intern.deleteAllNewTransaction();
    }

    public void deleteAllExternTransaction() throws OemDatabaseException {
        intern.deleteAllExternTransaction();
    }

    public void appendTelematicsDataNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        intern.appendTelematicsDataNewTransaction(newTelematicsData);
    }

    public void appendTelematicsDataExternTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        intern.appendTelematicsDataExternTransaction(newTelematicsData);
    }

    public void deleteByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        intern.deleteByIdNewTransaction(id);
    }

    public void deleteByIdExternTransaction(@NotNull final String id) throws OemDatabaseException {
        intern.deleteByIdExternTransaction(id);
    }

    public void deleteByVanNewTransaction(@NotNull final String van) throws OemDatabaseException {
        intern.deleteByVanNewTransaction(van);
    }

    public void deleteByVanExternTransaction(@NotNull final String van) throws OemDatabaseException {
        intern.deleteByVanExternTransaction(van);
    }

    public Vehicle getByIdNewTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getByIdNewTransaction(vehicleId));
    }

    public Vehicle getByIdExternTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getByIdExternTransaction(vehicleId));
    }

    public Vehicle getByIdWithTelematicsDataNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(intern.getByIdWithTelematicsDataNewTransaction(vehicleId));
    }

    public Vehicle getByIdWithTelematicsDataExternTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(intern.getByIdWithTelematicsDataExternTransaction(vehicleId));
    }

    public Vehicle findByVanNewTransaction(@NotNull final String van) throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.findByVanNewTransaction(van));
    }

    public Vehicle findByVanExternTransaction(@NotNull final String van) throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.findByVanExternTransaction(van));
    }

    public Vehicle getByVanWithTelematicsDataNewTransaction(@NotNull final String van) throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(intern.getByVanWithTelematicsDataNewTransaction(van));
    }

    public Vehicle getByVanWithTelematicsDataExternTransaction(@NotNull final String van) throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(intern.getByVanWithTelematicsDataExternTransaction(van));
    }

    public List<Vehicle> getAllNewTransaction() throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getAllNewTransaction());
    }

    public List<Vehicle> getAllExternTransaction() throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getAllExternTransaction());
    }

    public List<Vehicle> getAllWithTelematicsDataNewTransaction() throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(intern.getAllWithTelematicsDataNewTransaction());
    }

    public List<Vehicle> getAllWithTelematicsDataExternTransaction() throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(intern.getAllWithTelematicsDataExternTransaction());
    }

    public List<Vehicle> getUpdatedSinceNewTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getUpdatedSinceNewTransaction(updatedSince));
    }

    public List<Vehicle> getUpdatedSinceExternTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getUpdatedSinceExternTransaction(updatedSince));
    }

    public List<Vehicle> getUpdatedSinceWithTelematicsDataNewTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                intern.getUpdatedSinceWithTelematicsDataNewTransaction(updatedSince));
    }

    public List<Vehicle> getUpdatedSinceWithTelematicsDataExternTransaction(@NotNull final Instant updatedSince)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                intern.getUpdatedSinceWithTelematicsDataExternTransaction(updatedSince));
    }

    public List<Vehicle> getProducedBetweenNewTransaction(@NotNull final Instant producedSince,
                                                          @NotNull final Instant producedUntil)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getProducedBetweenNewTransaction(producedSince, producedUntil));
    }

    public List<Vehicle> getProducedBetweenExternTransaction(@NotNull final Instant producedSince,
                                                             @NotNull final Instant producedUntil)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getProducedBetweenExternTransaction(producedSince, producedUntil));
    }

    public List<Vehicle> getProducedBetweenWithTelematicsDataNewTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                intern.getProducedBetweenWithTelematicsDataNewTransaction(producedSince, producedUntil));
    }

    public List<Vehicle> getProducedBetweenWithTelematicsDataExternTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                intern.getProducedBetweenWithTelematicsDataExternTransaction(producedSince, producedUntil));
    }

    public List<Vehicle> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getSyncCounterSinceNewTransaction(syncCounter));
    }

    public List<Vehicle> getSyncCounterSinceExternTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return vehicleConverter.toDTO(intern.getSyncCounterSinceExternTransaction(syncCounter));
    }

    public List<Vehicle> getSyncCounterSinceWithTelematicsDataNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                intern.getSyncCounterSinceWithTelematicsDataNewTransaction(syncCounter));
    }

    public List<Vehicle> getSyncCounterSinceWithTelematicsDataExternTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return vehicleConverter.toDTOWithTelematicsData(
                intern.getSyncCounterSinceWithTelematicsDataExternTransaction(syncCounter));
    }
}
