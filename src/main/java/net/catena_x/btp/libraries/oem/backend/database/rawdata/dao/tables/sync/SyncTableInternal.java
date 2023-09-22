package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.config.PersistenceRawDataConfiguration;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.function.Supplier;

@Component
public class SyncTableInternal extends RawTableBase {
    public static final String DEFAULT_ID = "DEFAULT";

    @PersistenceContext(unitName = PersistenceRawDataConfiguration.UNIT_NAME) EntityManager entityManager;

    @Autowired private SyncRepository syncRepository;

    private final Logger logger = LoggerFactory.getLogger(SyncTableInternal.class);

    @RDTransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @RDTransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @RDTransactionDefaultUseExisting
    public void clearExternalTransaction() throws OemDatabaseException {
        try {
            syncRepository.deleteAll();
        }
        catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all sync objects failed!" + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void clearNewTransaction() throws OemDatabaseException {
        clearExternalTransaction();
    }

    @RDTransactionDefaultUseExisting
    public void initExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            syncRepository.init(id);
        }
        catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Initializing sync object failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void initNewTransaction(@NotNull final String id) throws OemDatabaseException {
        initExternalTransaction(id);
    }

    @RDTransactionSerializableUseExisting
    public void reInitExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            final SyncDAO current = syncRepository.getCurrent(id);
            if( current == null ) {
                initExternalTransaction(id);
            } else {
                syncRepository.setCurrent(id, 0);
            }
        }
        catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Reinitializing sync object failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionSerializableCreateNew
    public void reInitNewTransaction(@NotNull final String id) throws OemDatabaseException {
        reInitExternalTransaction(id);
    }

    @RDTransactionDefaultUseExisting
    public SyncDAO getCurrentExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            return syncRepository.getCurrent(id);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying sync object failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public SyncDAO getCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return getCurrentExternalTransaction(id);
    }

    @RDTransactionSerializableUseExisting
    public SyncDAO setCurrentExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            syncRepository.setCurrent(id,  getOrCreateCurrent(id).getSyncCounter() + 1);
            entityManager.flush();
            entityManager.clear();
            return syncRepository.getCurrent(id);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Setting sync object failed! " + exception.getMessage(), exception);
        }
    }

    private SyncDAO getOrCreateCurrent(@NotNull final String id) {
        final SyncDAO syncObject = syncRepository.getCurrent(id);
        if(syncObject != null) {
            return syncObject;
        }
        syncRepository.init(id);
        return syncRepository.getCurrent(id);
    }

    @RDTransactionSerializableCreateNew
    public SyncDAO setCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return setCurrentExternalTransaction(id);
    }
}
