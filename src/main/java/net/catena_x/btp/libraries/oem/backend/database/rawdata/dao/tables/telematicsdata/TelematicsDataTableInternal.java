package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.config.PersistenceRawDataConfiguration;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTableInternal;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@Component
public class TelematicsDataTableInternal extends RawTableBase {
    @PersistenceContext(unitName = PersistenceRawDataConfiguration.UNIT_NAME) EntityManager entityManager;

    @Autowired private TelematicsDataRepository telematicsDataRepository;
    @Autowired private InputTelematicsDataConverter inputTelematicsDataConverter;
    @Autowired private SyncTableInternal syncTable;

    private final Logger logger = LoggerFactory.getLogger(TelematicsDataTableInternal.class);

    @RDTransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @RDTransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @RDTransactionSerializableCreateNew
    public void adjust(@NotNull final boolean isPostgreSQL) {
        final String queryString = isPostgreSQL?
                "ALTER TABLE telematics_data ALTER COLUMN load_spectra TYPE TEXT" :
                "ALTER TABLE telematics_data ALTER COLUMN load_spectra VARCHAR(2147483640)";

        //ToDo: Check return type "NativeQuery.class".
        final NativeQuery query =
                ((Session)entityManager.getDelegate()).createNativeQuery(queryString, NativeQuery.class);
        query.executeUpdate();
    }

    @RDTransactionSerializableUseExisting
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
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Upload telematics data failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionSerializableCreateNew
    public String updateTelematicsDataGetIdNewTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        return updateTelematicsDataGetIdExternalTransaction(newTelematicsData);
    }

    @RDTransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemDatabaseException {
        try {
            telematicsDataRepository.deleteAll();
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all telematics data failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternalTransaction();
    }

    @RDTransactionDefaultUseExisting
    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            telematicsDataRepository.deleteById(id);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting telematics data by id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        deleteByIdExternalTransaction(id);
    }

    @RDTransactionDefaultUseExisting
    public void deleteByVehicleIdExternalTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        try {
            telematicsDataRepository.deleteByVehicleId(vehicleId);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting telematics data by vehicle id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteByVehicleIdNewTransaction(@NotNull final String vehicleId) throws OemDatabaseException {
        deleteByVehicleIdExternalTransaction(vehicleId);
    }

    @RDTransactionDefaultUseExisting
    public void deleteStoredUntilExternalTransaction(@NotNull final Instant storedUntil) throws OemDatabaseException {
        try {
            telematicsDataRepository.deleteStoredUntil(storedUntil);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting telematics data by storage timestamp failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteStoredUntilNewTransaction(@NotNull final Instant storedUntil) throws OemDatabaseException {
        deleteStoredUntilExternalTransaction(storedUntil);
    }

    @RDTransactionDefaultUseExisting
    public void deleteSyncCounterUntilExternalTransaction(@NotNull final long syncCounter) throws OemDatabaseException {
        try {
            telematicsDataRepository.deleteSyncCounterUntil(syncCounter);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting telematics data by sync counter failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteSyncCounterUntilNewTransaction(@NotNull final long syncCounter) throws OemDatabaseException {
        deleteSyncCounterUntilExternalTransaction(syncCounter);
    }

    @RDTransactionDefaultUseExisting
    public TelematicsDataDAO getByIdExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryById(id);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for telematics data by id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public TelematicsDataDAO getByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return getByIdExternalTransaction(id);
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getByVehicleIdExternalTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryByVehicleId(vehicleId);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for telematics data by vehicle id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getByVehicleIdNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return getByVehicleIdExternalTransaction(vehicleId);
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getByVehicleIdOrderBySyncCounterExternalTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryByVehicleIdOrderBySyncCounter(vehicleId);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for telematics data by vehicle id failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getByVehicleIdOrderBySyncCounterNewTransaction(@NotNull final String vehicleId)
            throws OemDatabaseException {
        return getByVehicleIdOrderBySyncCounterExternalTransaction(vehicleId);
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getUpdatedSinceExternalTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryByStorageSince(timestamp);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for telematics data by update timestamp failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getUpdatedSinceNewTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return getUpdatedSinceExternalTransaction(timestamp);
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getUpdatedUntilExternalTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryByStorageUntil(timestamp);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for telematics data by update timestamp failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getUpdatedUntilNewTransaction(@NotNull final Instant timestamp)
            throws OemDatabaseException {
        return getUpdatedUntilExternalTransaction(timestamp);
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getSyncCounterSinceExternalTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryBySyncCounterSince(syncCounter);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for telematics data by sync counter failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getSyncCounterSinceNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return getSyncCounterSinceExternalTransaction(syncCounter);
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getSyncCounterUntilExternalTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryBySyncCounterUntil(syncCounter);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for telematics data by sync counter failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getSyncCounterUntilNewTransaction(@NotNull final long syncCounter)
            throws OemDatabaseException {
        return getSyncCounterUntilExternalTransaction(syncCounter);
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllExternalTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAll();
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for all telematics data failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternalTransaction();
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllOrderByStorageTimestampExternalTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAllOrderByStorageTimestamp();
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for all telematics data failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllOrderByStorageTimestampNewTransaction() throws OemDatabaseException {
        return getAllOrderByStorageTimestampExternalTransaction();
    }

    @RDTransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllOrderBySyncCounterExternalTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAllOrderBySyncCounter();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception );
        }
    }

    @RDTransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllOrderBySyncCounterNewTransaction() throws OemDatabaseException {
        return getAllOrderBySyncCounterExternalTransaction();
    }
}
