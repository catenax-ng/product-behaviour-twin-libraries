package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionSerializableCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations.RDTransactionSerializableUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@Component
public class InfoTableInternal extends RawTableBase {
    @Autowired private InfoItemRepository infoItemRepository;

    private final Logger logger = LoggerFactory.getLogger(InfoTableInternal.class);

    @RDTransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @RDTransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @RDTransactionDefaultUseExisting
    public void setInfoItemExternalTransaction(@NotNull final InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        try {
            infoItemRepository.insert(key.toString(), value);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting info value failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void setInfoItemNewTransaction(@NotNull final InfoKey key, @NotNull final String value)
            throws OemDatabaseException {
        setInfoItemExternalTransaction(key, value);
    }

    @RDTransactionDefaultUseExisting
    public InfoItemDAO getInfoItemExternalTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        try {
            return infoItemRepository.queryByKey(key.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Reading info item failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public InfoItemDAO getInfoItemNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return getInfoItemExternalTransaction(key);
    }

    @RDTransactionDefaultUseExisting
    public List<InfoItemDAO> getAllExternalTransaction() throws OemDatabaseException {
        try {
            return infoItemRepository.queryAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Reading all info items failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public List<InfoItemDAO> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternalTransaction();
    }

    @RDTransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemDatabaseException {
        try {
            infoItemRepository.deleteAll();
        } catch (final Exception exception) {
            exception.printStackTrace();
            throw failed("Deleting all info items failed! " + exception.getMessage(), exception);
        }
    }

    @RDTransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternalTransaction();
    }

    @RDTransactionDefaultUseExisting
    public String getInfoValueExternalTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        final InfoItemDAO info = getInfoItemExternalTransaction(key);
        if(info == null) {
            return null;
        }

        return info.getValue();
    }

    @RDTransactionDefaultCreateNew
    public String getInfoValueNewTransaction(@NotNull final InfoKey key) throws OemDatabaseException {
        return getInfoValueExternalTransaction(key);
    }

    @RDTransactionSerializableUseExisting
    public Instant getCurrentDatabaseTimestampExternalTransaction() throws OemDatabaseException {
        final InfoItemDAO info = getInfoItemExternalTransaction(InfoKey.DATAVERSION);
        if(info == null) {
            return null;
        }

        return info.getQueryTimestamp();
    }

    @RDTransactionSerializableCreateNew
    public Instant getCurrentDatabaseTimestampNewTransaction() throws OemDatabaseException {
        return getCurrentDatabaseTimestampExternalTransaction();
    }
}
