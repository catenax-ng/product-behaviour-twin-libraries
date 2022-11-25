package net.catena_x.btp.libraries.oem.backend.model.dto.sync;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTableIntern;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class SyncTable extends RawTableBase {
    @Autowired private SyncTableIntern intern;
    @Autowired private SyncConverter syncConverter;

    final String ID_DEFAULT = "DEFAULT";

    public void clearNewTransaction() throws OemDatabaseException {
        intern.clearNewTransaction();
    }

    public void clearExternTransaction() throws OemDatabaseException {
        intern.clearExternTransaction();
    }

    public void initDefaultNewTransaction() throws OemDatabaseException {
        intern.initNewTransaction(ID_DEFAULT);
    }

    public void initDefaultExternTransaction() throws OemDatabaseException {
        intern.initExternTransaction(ID_DEFAULT);
    }

    public void reInitDefaultNewTransaction() throws OemDatabaseException {
        intern.reInitNewTransaction(ID_DEFAULT);
    }

    public void reInitDefaultExternTransaction() throws OemDatabaseException {
        intern.reInitExternTransaction(ID_DEFAULT);
    }

    public Sync getCurrentDefaultNewTransaction() throws OemDatabaseException {
        return syncConverter.toDTO(intern.getCurrentNewTransaction(ID_DEFAULT));
    }

    public Sync getCurrentDefaultExternTransaction() throws OemDatabaseException {
        return syncConverter.toDTO(intern.getCurrentExternTransaction(ID_DEFAULT));
    }

    public Sync setCurrentDefaultNewTransaction() throws OemDatabaseException {
        return syncConverter.toDTO(intern.setCurrentNewTransaction(ID_DEFAULT));
    }

    public Sync setCurrentDefaultExternTransaction() throws OemDatabaseException {
        return syncConverter.toDTO(intern.setCurrentExternTransaction(ID_DEFAULT));
    }

    private void initNewTransaction(@NotNull final String id) throws OemDatabaseException {
        intern.initNewTransaction(id);
    }

    private void initExternTransaction(@NotNull final String id) throws OemDatabaseException {
        intern.initExternTransaction(id);
    }

    private Sync getCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return syncConverter.toDTO(intern.getCurrentNewTransaction(id));
    }

    private Sync getCurrentExternTransaction(@NotNull final String id) throws OemDatabaseException {
        return syncConverter.toDTO(intern.getCurrentExternTransaction(id));
    }

    private Sync setCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return syncConverter.toDTO(intern.setCurrentNewTransaction(id));
    }

    private Sync setCurrentExternTransaction(@NotNull final String id) throws OemDatabaseException {
        return syncConverter.toDTO(intern.setCurrentExternTransaction(id));
    }
}
