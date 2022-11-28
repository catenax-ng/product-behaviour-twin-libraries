package net.catena_x.btp.libraries.oem.backend.model.dto.sync;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTableInternal;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class SyncTable extends RawTableBase {
    @Autowired private SyncTableInternal internal;
    @Autowired private SyncConverter syncConverter;

    final String ID_DEFAULT = "DEFAULT";

    public void clearNewTransaction() throws OemDatabaseException {
        internal.clearNewTransaction();
    }

    public void clearExternalTransaction() throws OemDatabaseException {
        internal.clearExternalTransaction();
    }

    public void initDefaultNewTransaction() throws OemDatabaseException {
        internal.initNewTransaction(ID_DEFAULT);
    }

    public void initDefaultExternalTransaction() throws OemDatabaseException {
        internal.initExternalTransaction(ID_DEFAULT);
    }

    public void reInitDefaultNewTransaction() throws OemDatabaseException {
        internal.reInitNewTransaction(ID_DEFAULT);
    }

    public void reInitDefaultExternalTransaction() throws OemDatabaseException {
        internal.reInitExternalTransaction(ID_DEFAULT);
    }

    public Sync getCurrentDefaultNewTransaction() throws OemDatabaseException {
        return syncConverter.toDTO(internal.getCurrentNewTransaction(ID_DEFAULT));
    }

    public Sync getCurrentDefaultExternalTransaction() throws OemDatabaseException {
        return syncConverter.toDTO(internal.getCurrentExternalTransaction(ID_DEFAULT));
    }

    public Sync setCurrentDefaultNewTransaction() throws OemDatabaseException {
        return syncConverter.toDTO(internal.setCurrentNewTransaction(ID_DEFAULT));
    }

    public Sync setCurrentDefaultExternalTransaction() throws OemDatabaseException {
        return syncConverter.toDTO(internal.setCurrentExternalTransaction(ID_DEFAULT));
    }

    private void initNewTransaction(@NotNull final String id) throws OemDatabaseException {
        internal.initNewTransaction(id);
    }

    private void initExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        internal.initExternalTransaction(id);
    }

    private Sync getCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return syncConverter.toDTO(internal.getCurrentNewTransaction(id));
    }

    private Sync getCurrentExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        return syncConverter.toDTO(internal.getCurrentExternalTransaction(id));
    }

    private Sync setCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return syncConverter.toDTO(internal.setCurrentNewTransaction(id));
    }

    private Sync setCurrentExternalTransaction(@NotNull final String id) throws OemDatabaseException {
        return syncConverter.toDTO(internal.setCurrentExternalTransaction(id));
    }
}
