package net.catena_x.btp.libraries.oem.backend.model.dto.infoitem;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.InfoTableInternal;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class InfoTable extends RawTableBase {
    @Autowired private InfoTableInternal internal;
    @Autowired private InfoItemConverter infoItemconverter;

    public void setInfoItemNewTransaction(@NotNull final InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        internal.setInfoItemNewTransaction(key, value);
    }

    public void setInfoItemExternalTransaction(@NotNull final InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        internal.setInfoItemExternalTransaction(key, value);
    }

    public InfoItem getInfoItemNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return infoItemconverter.toDTO(internal.getInfoItemNewTransaction(key));
    }

    public InfoItem getInfoItemExternalTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return infoItemconverter.toDTO(internal.getInfoItemExternalTransaction(key));
    }

    public List<InfoItem> getAllNewTransaction() throws OemDatabaseException {
        return infoItemconverter.toDTO(internal.getAllNewTransaction());
    }

    public List<InfoItem> getAllExternalTransaction() throws OemDatabaseException {
        return infoItemconverter.toDTO(internal.getAllExternalTransaction());
    }

    public void deleteAllNewTransaction() throws OemDatabaseException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws OemDatabaseException {
        internal.deleteAllExternalTransaction();
    }

    public String getInfoValueNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return internal.getInfoValueNewTransaction(key);
    }

    public String getInfoValueExternalTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return internal.getInfoValueExternalTransaction(key);
    }

    public Instant getCurrentDatabaseTimestampNewTransaction() throws OemDatabaseException {
        return internal.getCurrentDatabaseTimestampNewTransaction();
    }
}
