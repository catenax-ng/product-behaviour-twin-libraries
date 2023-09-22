package net.catena_x.btp.hi.oem.common.database.hi.tables.infoitem;

import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionDefaultCreateNew;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionDefaultUseExisting;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionSerializableCreateNew;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionSerializableUseExisting;
import net.catena_x.btp.hi.oem.common.database.hi.base.HITableBase;
import net.catena_x.btp.hi.oem.common.model.enums.HIInfoKey;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;

@Component
public class HIInfoTableInternal extends HITableBase {
    @Autowired private HIInfoItemRepository hiInfoItemRepository;

    private final Logger logger = LoggerFactory.getLogger(HIInfoTableInternal.class);

    @HITransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @HITransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @HITransactionDefaultUseExisting
    public void setInfoItemExternalTransaction(@NotNull final HIInfoKey key, @NotNull final String value)
            throws OemHIException {
        try {
            hiInfoItemRepository.insert(key.toString(), value);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting info value failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void setInfoItemNewTransaction(@NotNull final HIInfoKey key, @NotNull final String value)
            throws OemHIException {
        setInfoItemExternalTransaction(key, value);
    }
    
    @HITransactionDefaultUseExisting
    public void updateInfoItemExternalTransaction(@NotNull final HIInfoKey key, @NotNull final String value)
            throws OemHIException {
        try {
            hiInfoItemRepository.update(key.toString(), value);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Updating info value failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void updateInfoItemNewTransaction(@NotNull final HIInfoKey key, @NotNull final String value)
            throws OemHIException {
        updateInfoItemExternalTransaction(key, value);
    }
    
    @HITransactionDefaultUseExisting
    public HIInfoItemDAO getInfoItemExternalTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        try {
            return hiInfoItemRepository.queryByKey(key.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Reading info item failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public HIInfoItemDAO getInfoItemNewTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        return getInfoItemExternalTransaction(key);
    }

    @HITransactionDefaultUseExisting
    public List<HIInfoItemDAO> getAllExternalTransaction() throws OemHIException {
        try {
            return hiInfoItemRepository.queryAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Reading all info items failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIInfoItemDAO> getAllNewTransaction() throws OemHIException {
        return getAllExternalTransaction();
    }

    @HITransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemHIException {
        try {
            hiInfoItemRepository.deleteAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all info items failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemHIException {
        deleteAllExternalTransaction();
    }

    @HITransactionDefaultUseExisting
    public void deleteExternalTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        try {
            hiInfoItemRepository.delete(key.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting info item failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteNewTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        deleteExternalTransaction(key);
    }

    @HITransactionDefaultUseExisting
    public String getInfoValueExternalTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        return getInfoItemExternalTransaction(key).getValue();
    }

    @HITransactionDefaultCreateNew
    public String getInfoValueNewTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        return getInfoValueExternalTransaction(key);
    }
}
