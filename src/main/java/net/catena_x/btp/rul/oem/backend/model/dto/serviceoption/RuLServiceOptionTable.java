package net.catena_x.btp.rul.oem.backend.model.dto.serviceoption;

import net.catena_x.btp.rul.oem.backend.database.rul.tables.serviceoption.RuLServiceOptionTableInternal;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLServiceOptionType;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;

@Component
public class RuLServiceOptionTable {
    @Autowired private RuLServiceOptionTableInternal internal;
    @Autowired private RuLServiceOptionConverter rulServiceOptionConverter;

    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableNewTransaction(function);
    }

    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableExternalTransaction(function);
    }

    public void setServiceOptionNewTransaction(@NotNull final RuLServiceOptionType key, @NotNull final String value)
            throws OemRuLException {
        internal.setServiceOptionNewTransaction(key, value);
    }

    public void setServiceOptionExternalTransaction(@NotNull final RuLServiceOptionType key,
                                                    @NotNull final String value) throws OemRuLException {
        internal.setServiceOptionExternalTransaction(key, value);
    }

    public void updateServiceOptionNewTransaction(@NotNull final RuLServiceOptionType key,
                                                  @NotNull final String value)
            throws OemRuLException {
        internal.updateServiceOptionNewTransaction(key, value);
    }

    public void updateServiceOptionExternalTransaction(@NotNull final RuLServiceOptionType key,
                                                       @NotNull final String value) throws OemRuLException {
        internal.updateServiceOptionExternalTransaction(key, value);
    }

    public void deleteAllNewTransaction() throws OemRuLException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws OemRuLException {
        internal.deleteAllExternalTransaction();
    }

    public void deleteNewTransaction(@NotNull final RuLServiceOptionType key) throws OemRuLException {
        internal.deleteNewTransaction(key);
    }

    public void deleteExternalTransaction(@NotNull final RuLServiceOptionType key) throws OemRuLException {
        internal.deleteExternalTransaction(key);
    }

    public String getServiceOptionValueNewTransaction(@NotNull final RuLServiceOptionType key) throws OemRuLException {
        return internal.getServiceOptionValueNewTransaction(key);
    }

    public String getServiceOptionValueExternalTransaction(@NotNull final RuLServiceOptionType key)
            throws OemRuLException {
        return internal.getServiceOptionValueExternalTransaction(key);
    }

    public RuLServiceOption getServiceOptionNewTransaction(@NotNull final RuLServiceOptionType key)
            throws OemRuLException {
        return rulServiceOptionConverter.toDTO(internal.getServiceOptionNewTransaction(key));
    }

    public RuLServiceOption getServiceOptionExternalTransaction(@NotNull final RuLServiceOptionType key)
            throws OemRuLException {
        return rulServiceOptionConverter.toDTO(internal.getServiceOptionExternalTransaction(key));
    }

    public List<RuLServiceOption> getAllNewTransaction() throws OemRuLException {
        return rulServiceOptionConverter.toDTO(internal.getAllNewTransaction());
    }

    public List<RuLServiceOption> getAllExternalTransaction() throws OemRuLException {
        return rulServiceOptionConverter.toDTO(internal.getAllExternalTransaction());
    }
}
