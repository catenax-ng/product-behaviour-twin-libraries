package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.intern.TelematicsDataTableIntern;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.TelematicsData;
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

    public String uploadTelematicsDataGetIdNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return intern.uploadTelematicsDataGetIdNewTransaction(newTelematicsData);
    }

    public String uploadTelematicsDataGetIdExternTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return intern.uploadTelematicsDataGetIdExternTransaction(newTelematicsData);
    }

    public void deleteAllNewTransaction() throws OemDatabaseException {
        intern.deleteAllNewTransaction();
    }

    public void deleteAllExternTransaction() throws OemDatabaseException {
        intern.deleteAllExternTransaction();
    }

    public TelematicsData getByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return intern.getByIdNewTransaction(id);
    }

    public TelematicsData getByIdExternTransaction(@NotNull final String id) throws OemDatabaseException {
        return intern.getByIdExternTransaction(id);
    }

    public List<TelematicsData> getByVehicleIdNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return intern.getByVehicleIdNewTransaction(vehicleId);
    }

    public List<TelematicsData> getByVehicleIdExternTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return intern.getByVehicleIdExternTransaction(vehicleId);
    }

    public List<TelematicsData> getUpdatedSinceNewTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return intern.getUpdatedSinceNewTransaction(timestamp);
    }

    public List<TelematicsData> getUpdatedSinceExternTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return intern.getUpdatedSinceExternTransaction(timestamp);
    }

    public List<TelematicsData> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return intern.getSyncCounterSinceNewTransaction(syncCounter);
    }

    public List<TelematicsData> getSyncCounterSinceExternTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return intern.getSyncCounterSinceExternTransaction(syncCounter);
    }

    public List<TelematicsData> getAllNewTransaction() throws OemDatabaseException {
        return intern.getAllNewTransaction();
    }

    public List<TelematicsData> getAllExternTransaction() throws OemDatabaseException {
        return intern.getAllExternTransaction();
    }

    public List<TelematicsData> getAllOrderByStorageTimestampNewTransaction() throws OemDatabaseException {
        return intern.getAllOrderByStorageTimestampNewTransaction();
    }

    public List<TelematicsData> getAllOrderByStorageTimestampExternTransaction() throws OemDatabaseException {
        return intern.getAllOrderByStorageTimestampExternTransaction();
    }
}
