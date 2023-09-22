package net.catena_x.btp.hi.oem.common.database.hi.tables.calculation;

import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionDefaultCreateNew;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionDefaultUseExisting;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionSerializableCreateNew;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionSerializableUseExisting;
import net.catena_x.btp.hi.oem.common.database.hi.base.HITableBase;
import net.catena_x.btp.hi.oem.common.database.hi.tables.healthindicators.HIHealthIndicatorsTableInternal;
import net.catena_x.btp.hi.oem.common.database.hi.tables.vehicle.HIVehicleTableInternal;
import net.catena_x.btp.hi.oem.common.model.enums.HICalculationStatus;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@Component
public class HICalculationTableInternal extends HITableBase {
    @Autowired private HICalculationRepository hiCalculationRepository;
    @Autowired private HIVehicleTableInternal hiVehicleTable;
    @Autowired private HIHealthIndicatorsTableInternal hiHealthIndicatorsTable;

    private final Logger logger = LoggerFactory.getLogger(HICalculationTableInternal.class);

    @HITransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @HITransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @HITransactionSerializableUseExisting
    public void resetDbExternalTransaction() throws OemHIException {
        deleteAllExternalTransaction();
        hiVehicleTable.deleteAllExternalTransaction();
        hiHealthIndicatorsTable.deleteAllExternalTransaction();
    }

    @HITransactionSerializableCreateNew
    public void resetDbNewTransaction() throws OemHIException {
        resetDbExternalTransaction();
    }

    @HITransactionSerializableUseExisting
    public void insertExternalTransaction(@NotNull final String id, @NotNull final Instant calculationTimestamp,
                                          @NotNull final long calculationSyncCounterMin,
                                          @NotNull final long calculationSyncCounterMax,
                                          @NotNull final HICalculationStatus status) throws OemHIException {
        try {
            hiCalculationRepository.insert(id, calculationTimestamp, calculationSyncCounterMin,
                    calculationSyncCounterMax, status.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public void insertNewTransaction(@NotNull final String id, @NotNull final Instant calculationTimestamp,
                                     @NotNull final long calculationSyncCounterMin,
                                     @NotNull final long calculationSyncCounterMax,
                                     @NotNull final HICalculationStatus status) throws OemHIException {
        insertExternalTransaction(id, calculationTimestamp, calculationSyncCounterMin,
                calculationSyncCounterMax, status);
    }

    @HITransactionSerializableUseExisting
    public String insertGetIdExternalTransaction(@NotNull final Instant calculationTimestamp,
                                                 @NotNull final long calculationSyncCounterMin,
                                                 @NotNull final long calculationSyncCounterMax,
                                                 @NotNull final HICalculationStatus status) throws OemHIException {
        try {
            final String id = generateNewId();
            hiCalculationRepository.insert(id, calculationTimestamp, calculationSyncCounterMin,
                    calculationSyncCounterMax, status.toString());
            return id;
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public String insertGetIdNewTransaction(@NotNull final Instant calculationTimestamp,
                                            @NotNull final long calculationSyncCounterMin,
                                            @NotNull final long calculationSyncCounterMax,
                                            @NotNull final HICalculationStatus status) throws OemHIException {
        return insertGetIdExternalTransaction(calculationTimestamp, calculationSyncCounterMin,
                calculationSyncCounterMax, status);
    }

    @HITransactionSerializableUseExisting
    public void createNowExternalTransaction(@NotNull final String id, @NotNull final long calculationSyncCounterMin,
                                             @NotNull final long calculationSyncCounterMax) throws OemHIException {
        try {
            hiCalculationRepository.createNow(id, calculationSyncCounterMin, calculationSyncCounterMax,
                    HICalculationStatus.CREATED.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public void createNowNewTransaction(@NotNull final String id, @NotNull final long calculationSyncCounterMin,
                                        @NotNull final long calculationSyncCounterMax) throws OemHIException {
        createNowExternalTransaction(id, calculationSyncCounterMin, calculationSyncCounterMax);
    }

    @HITransactionSerializableUseExisting
    public String createNowGetIdExternalTransaction(@NotNull final long calculationSyncCounterMin,
                                                    @NotNull final long calculationSyncCounterMax)
            throws OemHIException {
        try {
            final String id = generateNewId();
            hiCalculationRepository.createNow(id, calculationSyncCounterMin, calculationSyncCounterMax,
                    HICalculationStatus.CREATED.toString());
            return id;
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public String createNowGetIdNewTransaction(@NotNull final long calculationSyncCounterMin,
                                               @NotNull final long calculationSyncCounterMax)
            throws OemHIException {
        return createNowGetIdExternalTransaction(calculationSyncCounterMin, calculationSyncCounterMax);
    }

    @HITransactionSerializableUseExisting
    public void updateStatusExternalTransaction(@NotNull final String id, @NotNull final HICalculationStatus newStatus)
            throws OemHIException {
        try {
            hiCalculationRepository.updateStatus(id, newStatus.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Updating calculation status failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public void updateStatusNewTransaction(@NotNull final String id, @NotNull final HICalculationStatus newStatus)
            throws OemHIException {
        updateStatusExternalTransaction(id, newStatus);
    }

    @HITransactionSerializableUseExisting
    public void setMessageExternalTransaction(@NotNull final String id, @NotNull final String message)
            throws OemHIException {
        try {
            hiCalculationRepository.setMessage(id, message);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Updating message for calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public void setMessageNewTransaction(@NotNull final String id, @NotNull final String message)
            throws OemHIException {
        setMessageExternalTransaction(id, message);
    }

    @HITransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemHIException {
        try {
            hiCalculationRepository.deleteAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all calculations failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemHIException {
        deleteAllExternalTransaction();
    }

    @HITransactionSerializableUseExisting
    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        try {
            hiCalculationRepository.deleteById(id);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public void deleteByIdNewTransaction(@NotNull final String id) throws OemHIException {
        deleteByIdExternalTransaction(id);
    }

    @HITransactionSerializableUseExisting
    public void deleteByStatusExternalTransaction(@NotNull final HICalculationStatus status)
            throws OemHIException {
        try {
            hiCalculationRepository.deleteByStatus(status.toString());
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public void deleteByStatusNewTransaction(@NotNull final HICalculationStatus status) throws OemHIException {
        deleteByStatusExternalTransaction(status);
    }

    @HITransactionSerializableUseExisting
    public void deleteCalculatedUntilExternalTransaction(@NotNull final Instant calculatedUntil) throws OemHIException {
        try {
            hiCalculationRepository.deleteCalculatedUntil(calculatedUntil);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public void deleteCalculatedUntilNewTransaction(@NotNull final Instant calculatedUntil)
            throws OemHIException {
        deleteCalculatedUntilExternalTransaction(calculatedUntil);
    }

    @HITransactionSerializableUseExisting
    public void deleteCalculationSyncCounterUntilExternalTransaction(@NotNull final long calculationSyncCounter)
            throws OemHIException {
        try {
            hiCalculationRepository.deleteCalculationSyncCounterUntil(calculationSyncCounter);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting calculation failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public void deleteCalculationSyncCounterUntilNewTransaction(@NotNull final long calculationSyncCounter)
            throws OemHIException {
        deleteCalculationSyncCounterUntilExternalTransaction(calculationSyncCounter);
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getAllExternalTransaction() throws OemHIException {
        try {
            return hiCalculationRepository.queryAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying calculations failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getAllNewTransaction() throws OemHIException {
        return getAllExternalTransaction();
    }

    @HITransactionSerializableUseExisting
    public HICalculationDAO getByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        try {
            return hiCalculationRepository.queryById(id);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying calculation by id failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public HICalculationDAO getByIdNewTransaction(@NotNull final String id) throws OemHIException {
        return getByIdExternalTransaction(id);
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getByStatusExternalTransaction(@NotNull final HICalculationStatus status)
            throws OemHIException {
        try {
            return hiCalculationRepository.queryByStatus(status.toString());
        } catch (final Exception exception) {
            throw failed("Querying calculations by status failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getByStatusNewTransaction(@NotNull final HICalculationStatus status)
            throws OemHIException {
        return getByStatusExternalTransaction(status);
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getByStatusOrderByCalculationSyncCounterExternalTransaction(
            @NotNull final HICalculationStatus status) throws OemHIException {
        try {
            return hiCalculationRepository.queryByStatusOrderByCalculationSyncCounter(status.toString());
        } catch (final Exception exception) {
            throw failed("Querying calculations by status failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getByStatusOrderByCalculationSyncCounterNewTransaction(
            @NotNull final HICalculationStatus status) throws OemHIException {
        return getByStatusOrderByCalculationSyncCounterExternalTransaction(status);
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getAllOrderByCalculationTimestampExternalTransaction() throws OemHIException {
        try {
            return hiCalculationRepository.queryAllOrderByCalculationTimestamp();
        } catch (final Exception exception) {
            throw failed("Querying calculations failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getAllOrderByCalculationTimestampNewTransaction() throws OemHIException {
        return getAllOrderByCalculationTimestampExternalTransaction();
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getAllOrderByCalculationSyncCounterExternalTransaction() throws OemHIException {
        try {
            return hiCalculationRepository.queryAllOrderByCalculationSyncCounter();
        } catch (final Exception exception) {
            throw failed("Querying calculations failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getAllOrderByCalculationSyncCounterNewTransaction() throws OemHIException {
        return getAllOrderByCalculationSyncCounterExternalTransaction();
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getByCalculationSinceExternalTransaction(
            @NotNull final Instant calculationTimestampSince) throws OemHIException {
        try {
            return hiCalculationRepository.queryByCalculationSince(calculationTimestampSince);
        } catch (final Exception exception) {
            throw failed("Querying calculations failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getByCalculationSinceNewTransaction(
            @NotNull final Instant calculationTimestampSince) throws OemHIException {
        return getByCalculationSinceExternalTransaction(calculationTimestampSince);
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getByCalculationUntilExternalTransaction(
            @NotNull final Instant calculationTimestampUntil) throws OemHIException {
        try {
            return hiCalculationRepository.queryByCalculationUntil(calculationTimestampUntil);
        } catch (final Exception exception) {
            throw failed("Querying calculations failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getByCalculationUntilNewTransaction(
            @NotNull final Instant calculationTimestampUntil) throws OemHIException {
        return getByCalculationUntilExternalTransaction(calculationTimestampUntil);
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getByCalculationSyncCounterSinceExternalTransaction(
            @NotNull final long calculationSyncCounterSince) throws OemHIException {
        try {
            return hiCalculationRepository.queryByCalculationSyncCounterSince(calculationSyncCounterSince);
        } catch (final Exception exception) {
            throw failed("Querying calculations failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getByCalculationSyncCounterSinceNewTransaction(
            @NotNull final long calculationSyncCounterSince) throws OemHIException {
        return getByCalculationSyncCounterSinceExternalTransaction(calculationSyncCounterSince);
    }

    @HITransactionSerializableUseExisting
    public List<HICalculationDAO> getByCalculationSyncCounterUntilExternalTransaction(
            @NotNull final long calculationSyncCounterUntil) throws OemHIException {
        try {
            return hiCalculationRepository.queryByCalculationSyncCounterUntil(calculationSyncCounterUntil);
        } catch (final Exception exception) {
            throw failed("Querying calculations failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public List<HICalculationDAO> getByCalculationSyncCounterUntilNewTransaction(
            @NotNull final long calculationSyncCounterUntil) throws OemHIException {
        return getByCalculationSyncCounterUntilExternalTransaction(calculationSyncCounterUntil);
    }
}
