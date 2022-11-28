package net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.TelematicsDataTableInternal;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class TelematicsDataTable extends RawTableBase {
    @Autowired private TelematicsDataTableInternal internal;
    @Autowired private TelematicsDataConverter telematicsDataConverter;

    public String uploadTelematicsDataGetIdNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return internal.updateTelematicsDataGetIdNewTransaction(newTelematicsData);
    }

    public String uploadTelematicsDataGetIdExternalTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return internal.updateTelematicsDataGetIdExternalTransaction(newTelematicsData);
    }

    public void deleteAllNewTransaction() throws OemDatabaseException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws OemDatabaseException {
        internal.deleteAllExternalTransaction();
    }

    public void deleteByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        internal.deleteByIdNewTransaction(id);
    }

    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        internal.deleteByIdExternalTransaction(id);
    }

    public void deleteByVehicleIdNewTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        internal.deleteByVehicleIdNewTransaction(vehicleId);
    }

    public void deleteByVehicleIdExternalTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        internal.deleteByVehicleIdExternalTransaction(vehicleId);
    }

    public void deleteStoredUntilNewTransaction(@NotNull final Instant storedUntil) throws OemDatabaseException {
        internal.deleteStoredUntilNewTransaction(storedUntil);
    }

    public void deleteStoredUntilExternalTransaction(@NotNull final Instant storedUntil) throws OemDatabaseException {
        internal.deleteStoredUntilExternalTransaction(storedUntil);
    }

    public void deleteSyncCounterUntilNewTransaction(@NotNull final long syncCounter) throws OemDatabaseException {
        internal.deleteSyncCounterUntilNewTransaction(syncCounter);
    }

    public void deleteSyncCounterUntilExternalTransaction(@NotNull final long syncCounter) throws OemDatabaseException {
        internal.deleteSyncCounterUntilExternalTransaction(syncCounter);
    }

    public TelematicsData getByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getByIdNewTransaction(id));
    }

    public TelematicsData getByIdExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getByIdExternalTransaction(id));
    }

    public List<TelematicsData> getByVehicleIdNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getByVehicleIdNewTransaction(vehicleId));
    }

    public List<TelematicsData> getByVehicleIdExternalTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getByVehicleIdExternalTransaction(vehicleId));
    }


    public List<TelematicsData> getByVehicleIdOrderBySyncCounterNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getByVehicleIdOrderBySyncCounterNewTransaction(vehicleId));
    }

    public List<TelematicsData> getByVehicleIdOrderBySyncCounterExternalTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getByVehicleIdOrderBySyncCounterExternalTransaction(vehicleId));
    }

    public List<TelematicsData> getUpdatedSinceNewTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getUpdatedSinceNewTransaction(timestamp));
    }

    public List<TelematicsData> getUpdatedSinceExternalTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getUpdatedSinceExternalTransaction(timestamp));
    }

    public List<TelematicsData> getUpdatedUntilNewTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getUpdatedUntilNewTransaction(timestamp));
    }

    public List<TelematicsData> getUpdatedUntilExternalTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getUpdatedUntilExternalTransaction(timestamp));
    }

    public List<TelematicsData> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getSyncCounterSinceNewTransaction(syncCounter));
    }

    public List<TelematicsData> getSyncCounterSinceExternalTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getSyncCounterSinceExternalTransaction(syncCounter));
    }

    public List<TelematicsData> getSyncCounterUntilNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getSyncCounterUntilNewTransaction(syncCounter));
    }

    public List<TelematicsData> getSyncCounterUntilExternalTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getSyncCounterUntilExternalTransaction(syncCounter));
    }
    
    public List<TelematicsData> getAllNewTransaction() throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getAllNewTransaction());
    }

    public List<TelematicsData> getAllExternalTransaction() throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getAllExternalTransaction());
    }

    public List<TelematicsData> getAllOrderByStorageTimestampNewTransaction() throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getAllOrderByStorageTimestampNewTransaction());
    }

    public List<TelematicsData> getAllOrderByStorageTimestampExternalTransaction() throws OemDatabaseException {
        return telematicsDataConverter.toDTO(internal.getAllOrderByStorageTimestampExternalTransaction());
    }
}
