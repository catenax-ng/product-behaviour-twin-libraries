package net.catena_x.btp.rul.oem.backend.model.dto.vinrelation;

import net.catena_x.btp.rul.oem.backend.database.rul.tables.vinrelation.RuLVinRelationTableInternal;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;

@Component
public class RuLVinRelationTable {
    @Autowired private RuLVinRelationTableInternal internal;
    @Autowired private RuLVinRelationConverter rulVinRelationConverter;

    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableNewTransaction(function);
    }

    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableExternalTransaction(function);
    }

    public void insertNewTransaction(@NotNull final String vin, @NotNull final String refId,
                                     @NotNull final boolean noData) throws OemRuLException {
        internal.insertNewTransaction(vin, refId, noData);
    }

    public void insertExternalTransaction(@NotNull final String vin, @NotNull final String refId,
                                          @NotNull final boolean noData) throws OemRuLException {
        internal.insertExternalTransaction(vin, refId, noData);
    }

    public void deleteAllNewTransaction() throws OemRuLException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws OemRuLException {
        internal.deleteAllExternalTransaction();
    }

    public void deleteByVinNewTransaction(@NotNull final String vin) throws OemRuLException {
        internal.deleteByVinNewTransaction(vin);
    }

    public void deleteByVinExternalTransaction(@NotNull final String vin) throws OemRuLException {
        internal.deleteByVinExternalTransaction(vin);
    }

    public void deleteByRefIdNewTransaction(@NotNull final String refId) throws OemRuLException {
        internal.deleteByRefIdNewTransaction(refId);
    }

    public void deleteByRefIdExternalTransaction(@NotNull final String refId) throws OemRuLException {
        internal.deleteByRefIdExternalTransaction(refId);
    }

    public void deleteByNoDataNewTransaction(@NotNull final boolean noData) throws OemRuLException {
        internal.deleteByNoDataNewTransaction(noData);
    }

    public void deleteByNoDatadExternalTransaction(@NotNull final boolean noData) throws OemRuLException {
        internal.deleteByNoDataExternalTransaction(noData);
    }

    public List<RuLVinRelation> getAllNewTransaction() throws OemRuLException {
        return rulVinRelationConverter.toDTO(internal.getAllNewTransaction());
    }

    public List<RuLVinRelation> getAllExternalTransaction() throws OemRuLException {
        return rulVinRelationConverter.toDTO(internal.getAllExternalTransaction());
    }

    public RuLVinRelation getByVinNewTransaction(@NotNull final String vin) throws OemRuLException {
        return rulVinRelationConverter.toDTO(internal.getByVinNewTransaction(vin));
    }

    public RuLVinRelation getByVinExternalTransaction(@NotNull final String vin) throws OemRuLException {
        return rulVinRelationConverter.toDTO(internal.getByVinExternalTransaction(vin));
    }

    public RuLVinRelation getByRefIdNewTransaction(@NotNull final String refId) throws OemRuLException {
        return rulVinRelationConverter.toDTO(internal.getByRefIdNewTransaction(refId));
    }

    public RuLVinRelation getByRefIdExternalTransaction(@NotNull final String refId) throws OemRuLException {
        return rulVinRelationConverter.toDTO(internal.getByRefIdExternalTransaction(refId));
    }

    public RuLVinRelation getByNoDataNewTransaction(@NotNull final boolean noData) throws OemRuLException {
        return rulVinRelationConverter.toDTO(internal.getByNoDataNewTransaction(noData));
    }

    public RuLVinRelation getByNoDataExternalTransaction(@NotNull final boolean noData) throws OemRuLException {
        return rulVinRelationConverter.toDTO(internal.getByNoDataExternalTransaction(noData));
    }
}
