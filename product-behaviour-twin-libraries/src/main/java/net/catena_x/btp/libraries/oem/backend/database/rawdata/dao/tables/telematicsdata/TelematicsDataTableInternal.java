package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTableInternal;
import net.catena_x.btp.libraries.util.database.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.util.database.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.util.database.annotations.TransactionSerializableCreateNew;
import net.catena_x.btp.libraries.util.database.annotations.TransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class TelematicsDataTableInternal extends RawTableBase {
    @PersistenceContext EntityManager entityManager;

    @Autowired private TelematicsDataRepository telematicsDataRepository;
    @Autowired private InputTelematicsDataConverter inputTelematicsDataConverter;
    @Autowired private SyncTableInternal syncTable;

    @TransactionSerializableCreateNew
    public void adjust(@NotNull final boolean isPostgreSQL) {
        final String queryString = isPostgreSQL?
                "ALTER TABLE telematics_data ALTER COLUMN load_spectra TYPE TEXT" :
                "ALTER TABLE telematics_data ALTER COLUMN load_spectra VARCHAR(2147483640)";

        final NativeQuery query = (NativeQuery)((Session)entityManager.getDelegate()).createSQLQuery(queryString);
        query.executeUpdate();
    }

    @TransactionSerializableUseExisting
    public String updateTelematicsDataGetIdExternalTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        try {
            entityManager.flush();
            entityManager.clear();

            final String newId = generateNewId();
            final SyncDAO sync = syncTable.setCurrentExternalTransaction(SyncTableInternal.DEFAULT_ID);
            final TelematicsDataDAO newDAO = inputTelematicsDataConverter.toDAOWithId(newId, newTelematicsData);

            telematicsDataRepository.insert(
                    newDAO.getId(), newDAO.getVehicleId(), newDAO.getLoadSpectra(),
                    newDAO.getAdaptionValues(), sync.getSyncCounter());

            return newId;
        }
        catch(final Exception exception) {
            throw failed("Upload telematic data failed!", exception);
        }
    }

    @TransactionSerializableCreateNew
    public String updateTelematicsDataGetIdNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return updateTelematicsDataGetIdExternalTransaction(newTelematicsData);
    }

    @TransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemDatabaseException {
        try {
            telematicsDataRepository.deleteAll();
        } catch(final Exception exception) {
            throw failed("Deleting all telematics data failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternalTransaction();
    }

    @TransactionDefaultUseExisting
    public TelematicsDataDAO getByIdExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryById(id);
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public TelematicsDataDAO getByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return getByIdExternalTransaction(id);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getByVehicleIdExternalTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryByVehicleId(vehicleId);
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by vehicle id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getByVehicleIdNewTransaction(final String vehicleId) throws OemDatabaseException {
        return getByVehicleIdExternalTransaction(vehicleId);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getUpdatedSinceExternalTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryByStorageSince(timestamp);
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by update timestamp failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getUpdatedSinceNewTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return getUpdatedSinceExternalTransaction(timestamp);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getSyncCounterSinceExternalTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryBySyncCounterSince(syncCounter);
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by sync counter failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return getSyncCounterSinceExternalTransaction(syncCounter);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllExternalTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAll();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternalTransaction();
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllOrderByStorageTimestampExternalTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAllOrderByStorageTimestamp();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllOrderByStorageTimestampNewTransaction() throws OemDatabaseException {
        return getAllOrderByStorageTimestampExternalTransaction();
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllOrderBySyncCounterExternalTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAllOrderBySyncCounter();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception );
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllOrderBySyncCounterNewTransaction() throws OemDatabaseException {
        return getAllOrderBySyncCounterExternalTransaction();
    }
}
