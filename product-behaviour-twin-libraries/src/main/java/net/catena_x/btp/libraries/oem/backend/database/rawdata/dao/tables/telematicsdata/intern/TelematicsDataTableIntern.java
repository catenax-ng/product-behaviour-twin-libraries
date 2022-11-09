package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.intern;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.converter.InputTelematicsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.converter.TelematicsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.model.TelematicsDataDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.Sync;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.TelematicsData;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class TelematicsDataTableIntern extends RawTableBase {
    @PersistenceContext EntityManager entityManager;

    @Autowired private TelematicsDataRepository telematicsDataRepository;
    @Autowired private TelematicsDataConverter telematicsDataConverter;
    @Autowired private InputTelematicsDataConverter inputTelematicsDataConverter;
    @Autowired private SyncTable syncTable;

    @TransactionSerializableUseExisting
    public String uploadTelematicsDataGetIdExternTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        try {
            entityManager.flush();
            entityManager.clear();

            final String newId = generateNewId();
            final Sync sync = syncTable.setCurrentDefaultExternTransaction();
            final TelematicsDataDAO newDAO = inputTelematicsDataConverter.toDAOWithId(newId, newTelematicsData);

            telematicsDataRepository.insert(
                    newDAO.getId(), newDAO.getVehicleId(), newDAO.getCreationTimestamp(),
                    newDAO.getMileage(), newDAO.getOperatingSeconds(), newDAO.getLoadCollectives(),
                    newDAO.getAdaptionValues(), sync.getSyncCounter());

            return newId;
        }
        catch(final Exception exception) {
            throw failed("Upload telematic data failed!", exception);
        }
    }

    @TransactionSerializableCreateNew
    public String uploadTelematicsDataGetIdNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return uploadTelematicsDataGetIdExternTransaction(newTelematicsData);
    }

    @TransactionDefaultUseExisting
    public void deleteAllExternTransaction() throws OemDatabaseException {
        try {
            telematicsDataRepository.deleteAll();
        } catch(final Exception exception) {
            throw failed("Deleting all telematics data failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternTransaction();
    }

    @TransactionDefaultUseExisting
    public TelematicsData getByIdExternTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            return telematicsDataConverter.toDTO(telematicsDataRepository.queryById(id));
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public TelematicsData getByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return getByIdExternTransaction(id);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsData> getByVehicleIdExternTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        try {
            return telematicsDataConverter.toDTO(telematicsDataRepository.queryByVehicleId(vehicleId));
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by vehicle id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsData> getByVehicleIdNewTransaction(final String vehicleId) throws OemDatabaseException {
        return getByVehicleIdExternTransaction(vehicleId);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsData> getUpdatedSinceExternTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        try {
            return telematicsDataConverter.toDTO(telematicsDataRepository.queryByStorageSince(timestamp));
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by update timestamp failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsData> getUpdatedSinceNewTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return getUpdatedSinceExternTransaction(timestamp);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsData> getSyncCounterSinceExternTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        try {
            return telematicsDataConverter.toDTO(telematicsDataRepository.queryBySyncCounterSince(syncCounter));
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by sync counter failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsData> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return getSyncCounterSinceExternTransaction(syncCounter);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsData> getAllExternTransaction() throws OemDatabaseException {
        try {
            return telematicsDataConverter.toDTO(telematicsDataRepository.queryAll());
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsData> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternTransaction();
    }

    @TransactionDefaultUseExisting
    public List<TelematicsData> getAllOrderByStorageTimestampExternTransaction() throws OemDatabaseException {
        try {
            return telematicsDataConverter.toDTO(telematicsDataRepository.queryAllOrderByStorageTimestamp());
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsData> getAllOrderByStorageTimestampNewTransaction() throws OemDatabaseException {
        return getAllOrderByStorageTimestampExternTransaction();
    }

    @TransactionDefaultUseExisting
    public List<TelematicsData> getAllOrderBySyncCounterExternTransaction() throws OemDatabaseException {
        try {
            return telematicsDataConverter.toDTO(telematicsDataRepository.queryAllOrderBySyncCounter());
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception );
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsData> getAllOrderBySyncCounterNewTransaction() throws OemDatabaseException {
        return getAllOrderBySyncCounterExternTransaction();
    }
}
