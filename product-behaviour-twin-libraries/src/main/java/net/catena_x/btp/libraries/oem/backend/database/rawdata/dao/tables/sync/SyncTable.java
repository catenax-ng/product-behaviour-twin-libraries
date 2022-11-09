package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.intern.SyncTableIntern;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.Sync;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class SyncTable extends RawTableBase {
    @Autowired private SyncTableIntern intern;

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
        return intern.getCurrentNewTransaction(ID_DEFAULT);
    }

    public Sync getCurrentDefaultExternTransaction() throws OemDatabaseException {
        return intern.getCurrentExternTransaction(ID_DEFAULT);
    }

    public Sync setCurrentDefaultNewTransaction() throws OemDatabaseException {
        return intern.setCurrentNewTransaction(ID_DEFAULT);
    }

    public Sync setCurrentDefaultExternTransaction() throws OemDatabaseException {
        return intern.setCurrentExternTransaction(ID_DEFAULT);
    }

    private void initNewTransaction(@NotNull final String id) throws OemDatabaseException {
        intern.initNewTransaction(id);
    }

    private void initExternTransaction(@NotNull final String id) throws OemDatabaseException {
        intern.initExternTransaction(id);
    }

    private Sync getCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return intern.getCurrentNewTransaction(id);
    }

    private Sync getCurrentExternTransaction(@NotNull final String id) throws OemDatabaseException {
        return intern.getCurrentExternTransaction(id);
    }

    private Sync setCurrentNewTransaction(@NotNull final String id) throws OemDatabaseException {
        return intern.setCurrentNewTransaction(id);
    }

    private Sync setCurrentExternTransaction(@NotNull final String id) throws OemDatabaseException {
        return intern.setCurrentExternTransaction(id);
    }
}
