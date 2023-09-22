package net.catena_x.btp.hi.oem.common.model.dto.calculation;

import net.catena_x.btp.hi.oem.common.database.hi.tables.calculation.HICalculationTableInternal;
import net.catena_x.btp.hi.oem.common.model.enums.HICalculationStatus;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@Component
public class HICalculationTable {
    @Autowired private HICalculationTableInternal internal;
    @Autowired private HICalculationConverter hiCalculationConverter;

    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableNewTransaction(function);
    }

    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableExternalTransaction(function);
    }

    public void resetDbNewTransaction() throws OemHIException {
        internal.resetDbNewTransaction();
    }

    public void resetDbExternalTransaction() throws OemHIException {
        internal.resetDbExternalTransaction();
    }

    public void insertNewTransaction(@NotNull final String id, @NotNull final Instant calculationTimestamp,
                                     @NotNull final long calculationSyncCounterMin,
                                     @NotNull final long calculationSyncCounterMax,
                                     @NotNull final HICalculationStatus status) throws OemHIException {
        internal.insertNewTransaction(id, calculationTimestamp, calculationSyncCounterMin,
                calculationSyncCounterMax, status);
    }

    public void insertExternalTransaction(@NotNull final String id, @NotNull final Instant calculationTimestamp,
                                          @NotNull final long calculationSyncCounterMin,
                                          @NotNull final long calculationSyncCounterMax,
                                          @NotNull final HICalculationStatus status) throws OemHIException {
        internal.insertExternalTransaction(id, calculationTimestamp, calculationSyncCounterMin,
                calculationSyncCounterMax, status);
    }

    public String insertGetIdNewTransaction(@NotNull final Instant calculationTimestamp,
                                            @NotNull final long calculationSyncCounterMin,
                                            @NotNull final long calculationSyncCounterMax,
                                            @NotNull final HICalculationStatus status) throws OemHIException {
        return internal.insertGetIdNewTransaction(calculationTimestamp, calculationSyncCounterMin,
                calculationSyncCounterMax, status);
    }

    public String insertGetIdExternalTransaction(@NotNull final Instant calculationTimestamp,
                                                 @NotNull final long calculationSyncCounterMin,
                                                 @NotNull final long calculationSyncCounterMax,
                                                 @NotNull final HICalculationStatus status) throws OemHIException {
        return internal.insertGetIdExternalTransaction(calculationTimestamp, calculationSyncCounterMin,
                calculationSyncCounterMax, status);
    }

    public void createNowNewTransaction(@NotNull final String id, @NotNull final long calculationSyncCounterMin,
                                        @NotNull final long calculationSyncCounterMax)
            throws OemHIException {
        internal.createNowNewTransaction(id, calculationSyncCounterMin, calculationSyncCounterMax);
    }

    public void createNowExternalTransaction(@NotNull final String id, @NotNull final long calculationSyncCounterMin,
                                             @NotNull final long calculationSyncCounterMax) throws OemHIException {
        internal.createNowExternalTransaction(id, calculationSyncCounterMin, calculationSyncCounterMax);
    }

    public String createNowGetIdNewTransaction(@NotNull final long calculationSyncCounterMin,
                                               @NotNull final long calculationSyncCounterMax)
            throws OemHIException {
        return internal.createNowGetIdNewTransaction(calculationSyncCounterMin, calculationSyncCounterMax);
    }

    public String createNowGetIdExternalTransaction(@NotNull final long calculationSyncCounterMin,
                                                    @NotNull final long calculationSyncCounterMax)
            throws OemHIException {
        return internal.createNowGetIdExternalTransaction(calculationSyncCounterMin, calculationSyncCounterMax);
    }

    public void updateStatusNewTransaction(@NotNull final String id, @NotNull final HICalculationStatus newStatus)
            throws OemHIException {
        internal.updateStatusNewTransaction(id, newStatus);
    }

    public void updateStatusExternalTransaction(@NotNull final String id, @NotNull final HICalculationStatus newStatus)
            throws OemHIException {
        internal.updateStatusExternalTransaction(id, newStatus);
    }

    public void setMessageNewTransaction(@NotNull final String id, @NotNull final String message)
            throws OemHIException {
        internal.setMessageNewTransaction(id, message);
    }

    public void setMessageExternalTransaction(@NotNull final String id, @NotNull final String message)
            throws OemHIException {
        internal.setMessageExternalTransaction(id, message);
    }

    public void deleteByIdNewTransaction(@NotNull final String id) throws OemHIException {
        internal.deleteByIdNewTransaction(id);
    }

    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        internal.deleteByIdExternalTransaction(id);
    }

    public void deleteByStatusNewTransaction(@NotNull final HICalculationStatus status) throws OemHIException {
        internal.deleteByStatusNewTransaction(status);
    }

    public void deleteByStatusExternalTransaction(@NotNull final HICalculationStatus status) throws OemHIException {
        internal.deleteByStatusExternalTransaction(status);
    }

    public void deleteCalculationSyncCounterUntilNewTransaction(@NotNull final long calculationSyncCounter)
            throws OemHIException {
        internal.deleteCalculationSyncCounterUntilNewTransaction(calculationSyncCounter);
    }

    public void deleteCalculationSyncCounterUntilExternalTransaction(@NotNull final long calculationSyncCounter)
            throws OemHIException {
        internal.deleteCalculationSyncCounterUntilExternalTransaction(calculationSyncCounter);
    }

    public HICalculation getByIdNewTransaction(@NotNull final String id) throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByIdNewTransaction(id));
    }

    public HICalculation getByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByIdExternalTransaction(id));
    }

    public List<HICalculation> getAllNewTransaction()
            throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getAllNewTransaction());
    }

    public List<HICalculation> getAllExternalTransaction()
            throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getAllExternalTransaction());
    }

    public List<HICalculation> getByStatusNewTransaction(@NotNull final HICalculationStatus status)
            throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByStatusNewTransaction(status));
    }

    public List<HICalculation> getByStatusExternalTransaction(@NotNull final HICalculationStatus status)
            throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByStatusExternalTransaction(status));
    }

    public List<HICalculation> getByStatusOrderByCalculationSyncCounterNewTransaction(
            @NotNull final HICalculationStatus status) throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByStatusOrderByCalculationSyncCounterNewTransaction(status));
    }

    public List<HICalculation> getByStatusOrderByCalculationSyncCounterExternalTransaction(
            @NotNull final HICalculationStatus status) throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByStatusOrderByCalculationSyncCounterExternalTransaction(
                status));
    }

    public List<HICalculation> getAllOrderByCalculationTimestampNewTransaction() throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getAllOrderByCalculationTimestampNewTransaction());
    }

    public List<HICalculation> getAllOrderByCalculationTimestampExternalTransaction() throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getAllOrderByCalculationTimestampExternalTransaction());
    }

    public List<HICalculation> getAllOrderByCalculationSyncCounterNewTransaction() throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getAllOrderByCalculationSyncCounterNewTransaction());
    }

    public List<HICalculation> getAllOrderByCalculationSyncCounterExternalTransaction() throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getAllOrderByCalculationSyncCounterExternalTransaction());
    }

    public List<HICalculation> getByCalculationSinceNewTransaction(@NotNull final Instant calculationTimestampSince)
            throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByCalculationSinceNewTransaction(calculationTimestampSince));
    }

    public List<HICalculation> getByCalculationSinceExternalTransaction(
            @NotNull final Instant calculationTimestampSince) throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByCalculationSinceExternalTransaction(calculationTimestampSince));
    }

    public List<HICalculation> getByCalculationUntilNewTransaction(@NotNull final Instant calculationTimestampUntil)
            throws OemHIException {
        return hiCalculationConverter.toDTO(internal.getByCalculationUntilNewTransaction(calculationTimestampUntil));
    }

    public List<HICalculation> getByCalculationUntilExternalTransaction(
            @NotNull final Instant calculationTimestampUntil) throws OemHIException {
        return hiCalculationConverter.toDTO(
                internal.getByCalculationUntilExternalTransaction(calculationTimestampUntil));
    }

    public List<HICalculation> getByCalculationSyncCounterSinceNewTransaction(
            @NotNull final long calculationSyncCounterSince) throws OemHIException {
        return hiCalculationConverter.toDTO(
                internal.getByCalculationSyncCounterSinceNewTransaction(calculationSyncCounterSince));
    }

    public List<HICalculation> getByCalculationSyncCounterSinceExternalTransaction(
            @NotNull final long calculationSyncCounterSince) throws OemHIException {
        return hiCalculationConverter.toDTO(
                internal.getByCalculationSyncCounterSinceExternalTransaction(calculationSyncCounterSince));
    }

    public List<HICalculation> getByCalculationSyncCounterUntilNewTransaction(
            @NotNull final long calculationSyncCounterUntil) throws OemHIException {
        return hiCalculationConverter.toDTO(
                internal.getByCalculationSyncCounterUntilNewTransaction(calculationSyncCounterUntil));
    }

    public List<HICalculation> getByCalculationSyncCounterUntilExternalTransaction(
            @NotNull final long calculationSyncCounterUntil) throws OemHIException {
        return hiCalculationConverter.toDTO(
                internal.getByCalculationSyncCounterUntilExternalTransaction(calculationSyncCounterUntil));
    }
}
