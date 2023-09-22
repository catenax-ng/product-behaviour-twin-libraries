package net.catena_x.btp.rul.oem.backend.database.rul.tables.serviceoption;

import net.catena_x.btp.rul.oem.backend.database.rul.annotations.RuLTransactionDefaultCreateNew;
import net.catena_x.btp.rul.oem.backend.database.rul.annotations.RuLTransactionDefaultUseExisting;
import net.catena_x.btp.rul.oem.backend.database.rul.annotations.RuLTransactionSerializableCreateNew;
import net.catena_x.btp.rul.oem.backend.database.rul.annotations.RuLTransactionSerializableUseExisting;
import net.catena_x.btp.rul.oem.backend.database.rul.base.RuLTableBase;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLServiceOptionType;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;

@Component
public class RuLServiceOptionTableInternal extends RuLTableBase {
    @Autowired private RuLServiceOptionRepository rulServiceOptionRepository;

    private final Logger logger = LoggerFactory.getLogger(RuLServiceOptionTableInternal.class);

    @RuLTransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @RuLTransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @RuLTransactionDefaultUseExisting
    public void setServiceOptionExternalTransaction(@NotNull final RuLServiceOptionType key, @NotNull final String value)
            throws OemRuLException {
        try {
            rulServiceOptionRepository.insert(key.toString(), value);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting service option failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public void setServiceOptionNewTransaction(@NotNull final RuLServiceOptionType key, @NotNull final String value)
            throws OemRuLException {
        setServiceOptionExternalTransaction(key, value);
    }

    @RuLTransactionDefaultUseExisting
    public void updateServiceOptionExternalTransaction(@NotNull final RuLServiceOptionType key, @NotNull final String value)
            throws OemRuLException {
        try {
            rulServiceOptionRepository.update(key.toString(), value);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Updating service option failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public void updateServiceOptionNewTransaction(@NotNull final RuLServiceOptionType key, @NotNull final String value)
            throws OemRuLException {
        updateServiceOptionExternalTransaction(key, value);
    }

    @RuLTransactionDefaultUseExisting
    public RuLServiceOptionDAO getServiceOptionExternalTransaction(@NotNull final RuLServiceOptionType key) throws OemRuLException {
        try {
            return rulServiceOptionRepository.queryByKey(key.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Reading service option failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public RuLServiceOptionDAO getServiceOptionNewTransaction(@NotNull final RuLServiceOptionType key) throws OemRuLException {
        return getServiceOptionExternalTransaction(key);
    }

    @RuLTransactionDefaultUseExisting
    public List<RuLServiceOptionDAO> getAllExternalTransaction() throws OemRuLException {
        try {
            return rulServiceOptionRepository.queryAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Reading all service options failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public List<RuLServiceOptionDAO> getAllNewTransaction() throws OemRuLException {
        return getAllExternalTransaction();
    }

    @RuLTransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemRuLException {
        try {
            rulServiceOptionRepository.deleteAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all service options failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemRuLException {
        deleteAllExternalTransaction();
    }

    @RuLTransactionDefaultUseExisting
    public void deleteExternalTransaction(@NotNull final RuLServiceOptionType key) throws OemRuLException {
        try {
            rulServiceOptionRepository.delete(key.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting service option failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public void deleteNewTransaction(@NotNull final RuLServiceOptionType key) throws OemRuLException {
        deleteExternalTransaction(key);
    }

    @RuLTransactionDefaultUseExisting
    public String getServiceOptionValueExternalTransaction(@NotNull final RuLServiceOptionType key)
            throws OemRuLException {
        final RuLServiceOptionDAO element = getServiceOptionExternalTransaction(key);
        if(element == null) {
            return null;
        }

        return element.getValue();
    }

    @RuLTransactionDefaultCreateNew
    public String getServiceOptionValueNewTransaction(@NotNull final RuLServiceOptionType key)
            throws OemRuLException {
        return getServiceOptionValueExternalTransaction(key);
    }
}
