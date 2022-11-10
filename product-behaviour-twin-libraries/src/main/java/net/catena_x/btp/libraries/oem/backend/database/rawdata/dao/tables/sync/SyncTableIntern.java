package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

@Component
public class SyncTableIntern extends RawTableBase {
    @PersistenceContext EntityManager entityManager;

    @Autowired private SyncRepository syncRepository;

    public static final String DEFAULT_ID = "DEFAULT";

    @TransactionDefaultUseExisting
    public void clearExternTransaction() throws OemDatabaseException {
        try {
            syncRepository.deleteAll();
        }
        catch (final Exception exception) {
            throw failed("Deleting all sync objects failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void clearNewTransaction() throws OemDatabaseException {
        clearExternTransaction();
    }

    @TransactionDefaultUseExisting
    public void initExternTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            syncRepository.init(id);
        }
        catch (final Exception exception) {
            throw failed("Initializing sync object failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void initNewTransaction(@NotNull final String id) throws OemDatabaseException {
        initExternTransaction(id);
    }

    @TransactionSerializableUseExisting
    public void reInitExternTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            final SyncDAO current = syncRepository.getCurrent(id);
            if( current == null ) {
                initExternTransaction(id);
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
        reInitExternTransaction(id);
    }

    @TransactionDefaultUseExisting
    public SyncDAO getCurrentExternTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            return syncRepository.getCurrent(id);
        } catch (final Exception exception) {
            throw failed("Querying sync object failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public SyncDAO getCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return getCurrentExternTransaction(id);
    }

    @TransactionSerializableUseExisting
    public SyncDAO setCurrentExternTransaction(@NotNull final String id) throws OemDatabaseException {
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
        return setCurrentExternTransaction(id);
    }
}
