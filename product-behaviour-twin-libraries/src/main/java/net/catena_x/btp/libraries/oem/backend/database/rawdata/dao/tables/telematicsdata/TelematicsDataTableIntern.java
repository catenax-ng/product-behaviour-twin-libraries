package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTableIntern;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class TelematicsDataTableIntern extends RawTableBase {
    @PersistenceContext EntityManager entityManager;

    @Autowired private TelematicsDataRepository telematicsDataRepository;
    @Autowired private InputTelematicsDataConverter inputTelematicsDataConverter;
    @Autowired private SyncTableIntern syncTable;

    @TransactionSerializableUseExisting
    public String uploadTelematicsDataGetIdExternTransaction(@NotNull final InputTelematicsData newTelematicsData)
            throws OemDatabaseException {
        try {
            entityManager.flush();
            entityManager.clear();

            final String newId = generateNewId();
            final SyncDAO sync = syncTable.setCurrentExternTransaction(SyncTableIntern.DEFAULT_ID);
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
    public TelematicsDataDAO getByIdExternTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryById(id);
        }
        catch(final Exception exception) {
            throw failed("Querying database for telematics data by id failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public TelematicsDataDAO getByIdNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return getByIdExternTransaction(id);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getByVehicleIdExternTransaction(@NotNull final String vehicleId)
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
        return getByVehicleIdExternTransaction(vehicleId);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getUpdatedSinceExternTransaction(@NotNull final Instant timestamp)
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
        return getUpdatedSinceExternTransaction(timestamp);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getSyncCounterSinceExternTransaction(@NotNull final long syncCounter)
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
        return getSyncCounterSinceExternTransaction(syncCounter);
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllExternTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAll();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternTransaction();
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllOrderByStorageTimestampExternTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAllOrderByStorageTimestamp();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllOrderByStorageTimestampNewTransaction() throws OemDatabaseException {
        return getAllOrderByStorageTimestampExternTransaction();
    }

    @TransactionDefaultUseExisting
    public List<TelematicsDataDAO> getAllOrderBySyncCounterExternTransaction() throws OemDatabaseException {
        try {
            return telematicsDataRepository.queryAllOrderBySyncCounter();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all telematics data failed!", exception );
        }
    }

    @TransactionDefaultCreateNew
    public List<TelematicsDataDAO> getAllOrderBySyncCounterNewTransaction() throws OemDatabaseException {
        return getAllOrderBySyncCounterExternTransaction();
    }
}
