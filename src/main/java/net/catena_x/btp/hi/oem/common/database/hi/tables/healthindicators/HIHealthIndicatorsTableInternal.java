package net.catena_x.btp.hi.oem.common.database.hi.tables.healthindicators;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.HealthIndicatorOutput;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionDefaultCreateNew;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionDefaultUseExisting;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionSerializableCreateNew;
import net.catena_x.btp.hi.oem.common.database.hi.annotations.HITransactionSerializableUseExisting;
import net.catena_x.btp.hi.oem.common.database.hi.base.HITableBase;
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
public class HIHealthIndicatorsTableInternal extends HITableBase {
    @Autowired private HIHealthIndicatorsRepository hiHealthIndicatorsRepository;

    private final Logger logger = LoggerFactory.getLogger(HIHealthIndicatorsTableInternal.class);

    @HITransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @HITransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @HITransactionSerializableUseExisting
    public String updateHealthIndicatorsGetIdExternalTransaction(
            @NotNull final HealthIndicatorOutput newHealthIndicators,
            @NotNull final String vehicleId,
            @NotNull final Instant calculationTimestamp,
            @NotNull final long calculationSyncCounter) throws OemHIException {
        try {
            final String newId = generateNewId();
            hiHealthIndicatorsRepository.insert(newId, vehicleId, newHealthIndicators.getComponentId(),
                    calculationTimestamp, calculationSyncCounter,
                    arrayToJsonString(newHealthIndicators.getHealthIndicatorValues()));

            return newId;
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Upload health indicators failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionSerializableCreateNew
    public String updateHealthIndicatorsGetIdNewTransaction(
            @NotNull final HealthIndicatorOutput newHealthIndicators,
            @NotNull final String vehicleId,
            @NotNull final Instant calculationTimestamp,
            @NotNull final long calculationSyncCounter) throws OemHIException {

        return updateHealthIndicatorsGetIdExternalTransaction(
                newHealthIndicators, vehicleId, calculationTimestamp, calculationSyncCounter);
    }

    @HITransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemHIException {
        try {
            hiHealthIndicatorsRepository.deleteAll();
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all health indicators failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemHIException {
        deleteAllExternalTransaction();
    }

    @HITransactionDefaultUseExisting
    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        try {
            hiHealthIndicatorsRepository.deleteById(id);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting health indicators by id failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteByIdNewTransaction(@NotNull final String id) throws OemHIException {
        deleteByIdExternalTransaction(id);
    }

    @HITransactionDefaultUseExisting
    public void deleteByVehicleIdExternalTransaction(@NotNull final String vehicleId) throws OemHIException {
        try {
            hiHealthIndicatorsRepository.deleteByVehicleId(vehicleId);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting health indicators by vehicle is failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteByVehicleIdNewTransaction(@NotNull final String vehicleId) throws OemHIException {
        deleteByVehicleIdExternalTransaction(vehicleId);
    }

    @HITransactionDefaultUseExisting
    public void deleteByGearboxIdExternalTransaction(@NotNull final String gearboxId) throws OemHIException {
        try {
            hiHealthIndicatorsRepository.deleteByGearboxId(gearboxId);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting health indicators by gearbox is failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteByGearboxIdNewTransaction(@NotNull final String gearboxId) throws OemHIException {
        deleteByGearboxIdExternalTransaction(gearboxId);
    }

    @HITransactionDefaultUseExisting
    public void deleteCalculatedUntilExternalTransaction(@NotNull final Instant calculatedUntil) throws OemHIException {
        try {
            hiHealthIndicatorsRepository.deleteCalculatedUntil(calculatedUntil);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting health indicators by calculation timestamp is failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteCalculatedUntilNewTransaction(@NotNull final Instant calculatedUntil) throws OemHIException {
        deleteCalculatedUntilExternalTransaction(calculatedUntil);
    }

    @HITransactionDefaultUseExisting
    public void deleteCalculationSyncCounterUntilExternalTransaction(@NotNull final long calculationSyncCounter)
            throws OemHIException {
        try {
            hiHealthIndicatorsRepository.deleteCalculationSyncCounterUntil(calculationSyncCounter);
        } catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting health indicators by calculation sync counter is failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public void deleteCalculationSyncCounterUntilNewTransaction(@NotNull final long calculationSyncCounter)
            throws OemHIException {
        deleteCalculationSyncCounterUntilExternalTransaction(calculationSyncCounter);
    }

    @HITransactionDefaultUseExisting
    public HIHealthIndicatorsDAO getByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryById(id);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for health indicators by id failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public HIHealthIndicatorsDAO getByIdNewTransaction(@NotNull final String id) throws OemHIException {
        return getByIdExternalTransaction(id);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getByVehicleIdExternalTransaction(@NotNull final String vehicleId)
            throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryByVehicleId(vehicleId);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for health indicators by vehicle id failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getByVehicleIdNewTransaction(@NotNull final String vehicleId)
            throws OemHIException {
        return getByVehicleIdExternalTransaction(vehicleId);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getByGearboxIdExternalTransaction(@NotNull final String gearboxId)
            throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryByGearboxId(gearboxId);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for health indicators by gearbox id failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getByGearboxIdNewTransaction(@NotNull final String gearboxId)
            throws OemHIException {
        return getByGearboxIdExternalTransaction(gearboxId);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getByVehicleIdOrderByCalculationSyncCounterExternalTransaction(
            @NotNull final String vehicleId) throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryByVehicleIdOrderByCalculationSyncCounter(vehicleId);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for health indicators by vehicle id failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getByVehicleIdOrderByCalculationSyncCounterNewTransaction(
            @NotNull final String vehicleId) throws OemHIException {
        return getByVehicleIdOrderByCalculationSyncCounterExternalTransaction(vehicleId);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getByGearboxIdOrderByCalculationSyncCounterExternalTransaction(
            @NotNull final String gearboxId) throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryByGearboxIdOrderByCalculationSyncCounter(gearboxId);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for health indicators by vehicle id failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getByGearboxIdOrderByCalculationSyncCounterNewTransaction(
            @NotNull final String gearboxId) throws OemHIException {
        return getByGearboxIdOrderByCalculationSyncCounterExternalTransaction(gearboxId);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getCalculatedSinceExternalTransaction(@NotNull final Instant timestamp)
            throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryByCalculationSince(timestamp);
        }
        catch(final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying database for health indicators by calculation timestamp failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getCalculatedSinceNewTransaction(@NotNull final Instant timestamp)
            throws OemHIException {
        return getCalculatedSinceExternalTransaction(timestamp);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getCalculatedUntilExternalTransaction(@NotNull final Instant timestamp)
            throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryByCalculationUntil(timestamp);
        }
        catch(final Exception exception) {
            throw failed("Querying database for health indicators by calculation timestamp failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getCalculatedUntilNewTransaction(@NotNull final Instant timestamp)
            throws OemHIException {
        return getCalculatedUntilExternalTransaction(timestamp);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getCalculationSyncCounterSinceExternalTransaction(
            @NotNull final long calculationSyncCounterSince) throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryByCalculationSyncCounterSince(calculationSyncCounterSince);
        }
        catch(final Exception exception) {
            throw failed("Querying database for health indicators by calculation timestamp failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getCalculationSyncCounterSinceNewTransaction(
            @NotNull final long calculationSyncCounterSince) throws OemHIException {
        return getCalculationSyncCounterSinceExternalTransaction(calculationSyncCounterSince);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getCalculationSyncCounterUntilExternalTransaction(
            @NotNull final long calculationSyncCounterUntil) throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryByCalculationSyncCounterUntil(calculationSyncCounterUntil);
        }
        catch(final Exception exception) {
            throw failed("Querying database for health indicators by calculation timestamp failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getCalculationSyncCounterUntilNewTransaction(
            @NotNull final long calculationSyncCounterUntil) throws OemHIException {
        return getCalculationSyncCounterUntilExternalTransaction(calculationSyncCounterUntil);
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getAllExternalTransaction() throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryAll();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all health indicators failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getAllNewTransaction() throws OemHIException {
        return getAllExternalTransaction();
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getAllOrderByCalculationTimestampExternalTransaction() throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryAllOrderByCalculationTimestamp();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all health indicators failed! " + exception.getMessage(), exception);
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getAllOrderByCalculationTimestampNewTransaction() throws OemHIException {
        return getAllOrderByCalculationTimestampExternalTransaction();
    }

    @HITransactionDefaultUseExisting
    public List<HIHealthIndicatorsDAO> getAllOrderByCalculationSyncCounterExternalTransaction() throws OemHIException {
        try {
            return hiHealthIndicatorsRepository.queryAllOrderByCalculationSyncCounter();
        }
        catch(final Exception exception) {
            throw failed("Querying database for all health indicators failed!", exception );
        }
    }

    @HITransactionDefaultCreateNew
    public List<HIHealthIndicatorsDAO> getAllOrderByCalculationSyncCounterNewTransaction() throws OemHIException {
        return getAllOrderByCalculationSyncCounterExternalTransaction();
    }
}
