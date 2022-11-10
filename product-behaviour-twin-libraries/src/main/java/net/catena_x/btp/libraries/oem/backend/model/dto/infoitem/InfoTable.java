package net.catena_x.btp.libraries.oem.backend.model.dto.infoitem;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.InfoTableIntern;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class InfoTable extends RawTableBase {
    @Autowired private InfoTableIntern intern;
    @Autowired private InfoItemConverter infoItemconverter;

    public void setInfoItemNewTransaction(@NotNull final InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        intern.setInfoItemNewTransaction(key, value);
    }

    public void setInfoItemExternTransaction(@NotNull final InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        intern.setInfoItemExternTransaction(key, value);
    }

    public InfoItem getInfoItemNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return infoItemconverter.toDTO(intern.getInfoItemNewTransaction(key));
    }

    public InfoItem getInfoItemExternTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return infoItemconverter.toDTO(intern.getInfoItemExternTransaction(key));
    }

    public List<InfoItem> getAllNewTransaction() throws OemDatabaseException {
        return infoItemconverter.toDTO(intern.getAllNewTransaction());
    }

    public List<InfoItem> getAllExternTransaction() throws OemDatabaseException {
        return infoItemconverter.toDTO(intern.getAllExternTransaction());
    }

    public void deleteAllNewTransaction() throws OemDatabaseException {
        intern.deleteAllNewTransaction();
    }

    public void deleteAllExternTransaction() throws OemDatabaseException {
        intern.deleteAllExternTransaction();
    }

    public String getInfoValueNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return intern.getInfoValueNewTransaction(key);
    }

    public String getInfoValueExternTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return intern.getInfoValueExternTransaction(key);
    }

    public Instant getCurrentDatabaseTimestampNewTransaction() throws OemDatabaseException {
        return intern.getCurrentDatabaseTimestampNewTransaction();
    }
}
