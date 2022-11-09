package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.intern;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.converter.InfoItemConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class InfoTableIntern extends RawTableBase {
    @Autowired private InfoItemRepository infoItemRepository;
    @Autowired private InfoItemConverter infoItemConverter;

    @TransactionDefaultUseExisting
    public void setInfoItemExternTransaction(@NotNull final InfoItem.InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        try {
            infoItemRepository.insert(key.toString(), value);
        } catch (final Exception exception) {
            throw failed("Inserting info value failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void setInfoItemNewTransaction(@NotNull final InfoItem.InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        setInfoItemExternTransaction(key, value);
    }

    @TransactionDefaultUseExisting
    public InfoItem getInfoItemExternTransaction(@NotNull final InfoItem.InfoKey key) throws OemDatabaseException {
        try {
            return infoItemConverter.toDTO(infoItemRepository.queryByKey(key.toString()));
        } catch (final Exception exception) {
            throw failed("Reading info item failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public InfoItem getInfoItemNewTransaction(@NotNull final InfoItem.InfoKey key) throws OemDatabaseException {
        return getInfoItemExternTransaction(key);
    }

    @TransactionDefaultUseExisting
    public List<InfoItem> getAllExternTransaction() throws OemDatabaseException {
        try {
            return infoItemConverter.toDTO(infoItemRepository.queryAll());
        } catch (final Exception exception) {
            throw failed("Reading all info items failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<InfoItem> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternTransaction();
    }

    @TransactionDefaultUseExisting
    public void deleteAllExternTransaction() throws OemDatabaseException {
        try {
            infoItemRepository.deleteAll();
        } catch (final Exception exception) {
            throw failed("Deleting all info items failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternTransaction();
    }

    @TransactionDefaultUseExisting
    public String getInfoValueExternTransaction(@NotNull final InfoItem.InfoKey key) throws OemDatabaseException {
        return getInfoItemExternTransaction(key).getValue();
    }

    @TransactionDefaultCreateNew
    public String getInfoValueNewTransaction(@NotNull final InfoItem.InfoKey key) throws OemDatabaseException {
        return getInfoValueExternTransaction(key);
    }

    @TransactionSerializableUseExisting
    public Instant getCurrentDatabaseTimestampExternTransaction() throws OemDatabaseException {
        return getInfoItemExternTransaction(InfoItem.InfoKey.dataversion).getQueryTimestamp();
    }

    @TransactionSerializableCreateNew
    public Instant getCurrentDatabaseTimestampNewTransaction() throws OemDatabaseException {
        return getCurrentDatabaseTimestampExternTransaction();
    }
}
