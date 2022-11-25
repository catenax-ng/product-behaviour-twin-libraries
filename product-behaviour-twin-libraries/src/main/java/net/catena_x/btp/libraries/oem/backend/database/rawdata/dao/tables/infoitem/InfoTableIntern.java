package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.util.database.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.util.database.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.util.database.annotations.TransactionSerializableCreateNew;
import net.catena_x.btp.libraries.util.database.annotations.TransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class InfoTableIntern extends RawTableBase {
    @Autowired private InfoItemRepository infoItemRepository;

    @TransactionDefaultUseExisting
    public void setInfoItemExternTransaction(@NotNull final InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        try {
            infoItemRepository.insert(key.toString(), value);
        } catch (final Exception exception) {
            throw failed("Inserting info value failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void setInfoItemNewTransaction(@NotNull final InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        setInfoItemExternTransaction(key, value);
    }

    @TransactionDefaultUseExisting
    public InfoItemDAO getInfoItemExternTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        try {
            return infoItemRepository.queryByKey(key.toString());
        } catch (final Exception exception) {
            throw failed("Reading info item failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public InfoItemDAO getInfoItemNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return getInfoItemExternTransaction(key);
    }

    @TransactionDefaultUseExisting
    public List<InfoItemDAO> getAllExternTransaction() throws OemDatabaseException {
        try {
            return infoItemRepository.queryAll();
        } catch (final Exception exception) {
            throw failed("Reading all info items failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<InfoItemDAO> getAllNewTransaction() throws OemDatabaseException {
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
    public String getInfoValueExternTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return getInfoItemExternTransaction(key).getValue();
    }

    @TransactionDefaultCreateNew
    public String getInfoValueNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return getInfoValueExternTransaction(key);
    }

    @TransactionSerializableUseExisting
    public Instant getCurrentDatabaseTimestampExternTransaction() throws OemDatabaseException {
        return getInfoItemExternTransaction(InfoKey.DATAVERSION).getQueryTimestamp();
    }

    @TransactionSerializableCreateNew
    public Instant getCurrentDatabaseTimestampNewTransaction() throws OemDatabaseException {
        return getCurrentDatabaseTimestampExternTransaction();
    }
}
