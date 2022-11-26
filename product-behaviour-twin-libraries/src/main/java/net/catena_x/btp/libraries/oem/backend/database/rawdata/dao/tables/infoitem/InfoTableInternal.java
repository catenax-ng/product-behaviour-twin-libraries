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
public class InfoTableInternal extends RawTableBase {
    @Autowired private InfoItemRepository infoItemRepository;

    @TransactionDefaultUseExisting
    public void setInfoItemExternalTransaction(@NotNull final InfoKey key, @NotNull final String value)
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
        setInfoItemExternalTransaction(key, value);
    }

    @TransactionDefaultUseExisting
    public InfoItemDAO getInfoItemExternalTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        try {
            return infoItemRepository.queryByKey(key.toString());
        } catch (final Exception exception) {
            throw failed("Reading info item failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public InfoItemDAO getInfoItemNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return getInfoItemExternalTransaction(key);
    }

    @TransactionDefaultUseExisting
    public List<InfoItemDAO> getAllExternalTransaction() throws OemDatabaseException {
        try {
            return infoItemRepository.queryAll();
        } catch (final Exception exception) {
            throw failed("Reading all info items failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<InfoItemDAO> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternalTransaction();
    }

    @TransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemDatabaseException {
        try {
            infoItemRepository.deleteAll();
        } catch (final Exception exception) {
            throw failed("Deleting all info items failed!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternalTransaction();
    }

    @TransactionDefaultUseExisting
    public String getInfoValueExternalTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return getInfoItemExternalTransaction(key).getValue();
    }

    @TransactionDefaultCreateNew
    public String getInfoValueNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return getInfoValueExternalTransaction(key);
    }

    @TransactionSerializableUseExisting
    public Instant getCurrentDatabaseTimestampExternalTransaction() throws OemDatabaseException {
        return getInfoItemExternalTransaction(InfoKey.DATAVERSION).getQueryTimestamp();
    }

    @TransactionSerializableCreateNew
    public Instant getCurrentDatabaseTimestampNewTransaction() throws OemDatabaseException {
        return getCurrentDatabaseTimestampExternalTransaction();
    }
}
