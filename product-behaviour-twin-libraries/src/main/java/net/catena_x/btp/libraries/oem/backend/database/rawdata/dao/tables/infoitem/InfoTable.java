package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.intern.InfoTableIntern;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class InfoTable extends RawTableBase {
    @Autowired private InfoTableIntern intern;

    public void setInfoItemNewTransaction(@NotNull final InfoItem.InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        intern.setInfoItemNewTransaction(key, value);
    }

    public void setInfoItemExternTransaction(@NotNull final InfoItem.InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        intern.setInfoItemExternTransaction(key, value);
    }

    public InfoItem getInfoItemNewTransaction(@NotNull final InfoItem.InfoKey key) throws OemDatabaseException {
        return intern.getInfoItemNewTransaction(key);
    }

    public InfoItem getInfoItemExternTransaction(@NotNull final InfoItem.InfoKey key) throws OemDatabaseException {
        return intern.getInfoItemExternTransaction(key);
    }

    public List<InfoItem> getAllNewTransaction() throws OemDatabaseException {
        return intern.getAllNewTransaction();
    }

    public List<InfoItem> getAllExternTransaction() throws OemDatabaseException {
        return intern.getAllExternTransaction();
    }

    public void deleteAllNewTransaction() throws OemDatabaseException {
        intern.deleteAllNewTransaction();
    }

    public void deleteAllExternTransaction() throws OemDatabaseException {
        intern.deleteAllExternTransaction();
    }

    public String getInfoValueNewTransaction(@NotNull final InfoItem.InfoKey key) throws OemDatabaseException {
        return intern.getInfoValueNewTransaction(key);
    }

    public String getInfoValueExternTransaction(@NotNull final InfoItem.InfoKey key) throws OemDatabaseException {
        return intern.getInfoValueExternTransaction(key);
    }

    public Instant getCurrentDatabaseTimestampNewTransaction() throws OemDatabaseException {
        return intern.getCurrentDatabaseTimestampNewTransaction();
    }
}
