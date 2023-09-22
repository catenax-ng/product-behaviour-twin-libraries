package net.catena_x.btp.hi.oem.common.model.dto.infoitem;

import net.catena_x.btp.hi.oem.common.database.hi.tables.infoitem.HIInfoTableInternal;
import net.catena_x.btp.hi.oem.common.model.enums.HIInfoKey;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;

@Component
public class HIInfoTable {
    @Autowired private HIInfoTableInternal internal;
    @Autowired private HIInfoItemConverter hiInfoItemConverter;

    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableNewTransaction(function);
    }

    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableExternalTransaction(function);
    }

    public void setInfoItemNewTransaction(@NotNull final HIInfoKey key, @NotNull final String value)
            throws OemHIException {
        internal.setInfoItemNewTransaction(key, value);
    }

    public void setInfoItemExternalTransaction(@NotNull final HIInfoKey key, @NotNull final String value)
            throws OemHIException {
        internal.setInfoItemExternalTransaction(key, value);
    }

    public void updateInfoItemNewTransaction(@NotNull final HIInfoKey key, @NotNull final String value)
            throws OemHIException {
        internal.updateInfoItemNewTransaction(key, value);
    }

    public void updateInfoItemExternalTransaction(@NotNull final HIInfoKey key, @NotNull final String value)
            throws OemHIException {
        internal.updateInfoItemExternalTransaction(key, value);
    }

    public void deleteAllNewTransaction() throws OemHIException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws OemHIException {
        internal.deleteAllExternalTransaction();
    }

    public void deleteNewTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        internal.deleteNewTransaction(key);
    }

    public void deleteExternalTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        internal.deleteExternalTransaction(key);
    }

    public String getInfoValueNewTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        return internal.getInfoValueNewTransaction(key);
    }

    public String getInfoValueExternalTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        return internal.getInfoValueExternalTransaction(key);
    }

    public HIInfoItem getInfoItemNewTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        return hiInfoItemConverter.toDTO(internal.getInfoItemNewTransaction(key));
    }

    public HIInfoItem getInfoItemExternalTransaction(@NotNull final HIInfoKey key) throws OemHIException {
        return hiInfoItemConverter.toDTO(internal.getInfoItemExternalTransaction(key));
    }

    public List<HIInfoItem> getAllNewTransaction() throws OemHIException {
        return hiInfoItemConverter.toDTO(internal.getAllNewTransaction());
    }

    public List<HIInfoItem> getAllExternalTransaction() throws OemHIException {
        return hiInfoItemConverter.toDTO(internal.getAllExternalTransaction());
    }
}
