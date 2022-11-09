package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.intern;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.converter.SyncConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.model.SyncDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.Sync;
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
    @Autowired private SyncConverter syncConverter;

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
    public Sync getCurrentExternTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            return syncConverter.toDTO(syncRepository.getCurrent(id));
        } catch (final Exception exception) {
            throw failed("Querying sync object failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public Sync getCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return getCurrentExternTransaction(id);
    }

    @TransactionSerializableUseExisting
    public Sync setCurrentExternTransaction(@NotNull final String id) throws OemDatabaseException {
        try {
            syncRepository.setCurrent(id, syncRepository.getCurrent(id).getSyncCounter() + 1);
            entityManager.flush();
            entityManager.clear();
            return syncConverter.toDTO(syncRepository.getCurrent(id));
        } catch (final Exception exception) {
            throw failed("Setting sync object failed!", exception);
        }
    }

    @TransactionSerializableCreateNew
    public Sync setCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return setCurrentExternTransaction(id);
    }
}
