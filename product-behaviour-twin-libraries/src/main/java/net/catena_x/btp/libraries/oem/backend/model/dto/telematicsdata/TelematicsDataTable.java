package net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.TelematicsDataTableIntern;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class TelematicsDataTable extends RawTableBase {
    @Autowired private TelematicsDataTableIntern intern;
    @Autowired private TelematicsDataConverter telematicsDataConverter;

    public String uploadTelematicsDataGetIdNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return intern.updateTelematicsDataGetIdNewTransaction(newTelematicsData);
    }

    public String uploadTelematicsDataGetIdExternTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return intern.updateTelematicsDataGetIdExternTransaction(newTelematicsData);
    }

    public void deleteAllNewTransaction() throws OemDatabaseException {
        intern.deleteAllNewTransaction();
    }

    public void deleteAllExternTransaction() throws OemDatabaseException {
        intern.deleteAllExternTransaction();
    }

    public TelematicsData getByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getByIdNewTransaction(id));
    }

    public TelematicsData getByIdExternTransaction(@NotNull final String id) throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getByIdExternTransaction(id));
    }

    public List<TelematicsData> getByVehicleIdNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getByVehicleIdNewTransaction(vehicleId));
    }

    public List<TelematicsData> getByVehicleIdExternTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getByVehicleIdExternTransaction(vehicleId));
    }

    public List<TelematicsData> getUpdatedSinceNewTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getUpdatedSinceNewTransaction(timestamp));
    }

    public List<TelematicsData> getUpdatedSinceExternTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getUpdatedSinceExternTransaction(timestamp));
    }

    public List<TelematicsData> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getSyncCounterSinceNewTransaction(syncCounter));
    }

    public List<TelematicsData> getSyncCounterSinceExternTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getSyncCounterSinceExternTransaction(syncCounter));
    }

    public List<TelematicsData> getAllNewTransaction() throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getAllNewTransaction());
    }

    public List<TelematicsData> getAllExternTransaction() throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getAllExternTransaction());
    }

    public List<TelematicsData> getAllOrderByStorageTimestampNewTransaction() throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getAllOrderByStorageTimestampNewTransaction());
    }

    public List<TelematicsData> getAllOrderByStorageTimestampExternTransaction() throws OemDatabaseException {
        return telematicsDataConverter.toDTO(intern.getAllOrderByStorageTimestampExternTransaction());
    }
}
