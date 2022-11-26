package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.util.database.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.util.database.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.util.database.annotations.TransactionSerializableCreateNew;
import net.catena_x.btp.libraries.util.database.annotations.TransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

@Component
public class SyncTableInternal extends RawTableBase {
    @PersistenceContext EntityManager entityManager;

    @Autowired private SyncRepository syncRepository;

    public static final String DEFAULT_ID = "DEFAULT";

    @TransactionDefaultUseExisting
    public void clearExternalTransaction() throws OemDatabaseException {
        try {
            syncRepository.deleteAll();
        }
        catch (final Exception exception) {
            throw failed("Deleting all sync objects failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void clearNewTransaction() throws OemDatabaseException {
        clearExternalTransaction();
    }

    @TransactionDefaultUseExisting
    public void initExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            syncRepository.init(id);
        }
        catch (final Exception exception) {
            throw failed("Initializing sync object failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void initNewTransaction(@NotNull final String id) throws OemDatabaseException {
        initExternalTransaction(id);
    }

    @TransactionSerializableUseExisting
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
            throw failed("Reinitializing sync object failed!", exception);
        }
    }

    @TransactionSerializableCreateNew
    public void reInitNewTransaction(@NotNull final String id) throws OemDatabaseException {
        reInitExternalTransaction(id);
    }

    @TransactionDefaultUseExisting
    public SyncDAO getCurrentExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            return syncRepository.getCurrent(id);
        } catch (final Exception exception) {
            throw failed("Querying sync object failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public SyncDAO getCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return getCurrentExternalTransaction(id);
    }

    @TransactionSerializableUseExisting
    public SyncDAO setCurrentExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            syncRepository.setCurrent(id, syncRepository.getCurrent(id).getSyncCounter() + 1);
            entityManager.flush();
            entityManager.clear();
            return syncRepository.getCurrent(id);
        } catch (final Exception exception) {
            throw failed("Setting sync object failed!", exception);
        }
    }

    @TransactionSerializableCreateNew
    public SyncDAO setCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return setCurrentExternalTransaction(id);
    }
}
