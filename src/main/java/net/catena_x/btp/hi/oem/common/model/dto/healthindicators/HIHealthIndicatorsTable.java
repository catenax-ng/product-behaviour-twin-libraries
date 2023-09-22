package net.catena_x.btp.hi.oem.common.model.dto.healthindicators;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.HealthIndicatorOutput;
import net.catena_x.btp.hi.oem.common.database.hi.tables.healthindicators.HIHealthIndicatorsTableInternal;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@Component
public class HIHealthIndicatorsTable {
    @Autowired private HIHealthIndicatorsTableInternal internal;
    @Autowired private HIHealthIndicatorsConverter healthIndicatorsConverter;

    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableNewTransaction(function);
    }

    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableExternalTransaction(function);
    }

    public String updateHealthIndicatorsGetIdNewTransaction(
            @NotNull final HealthIndicatorOutput newHealthIndicators, @NotNull final String vehicleId,
            @NotNull final Instant calculationTimestamp, @NotNull final long calculationSyncCounter)
            throws OemHIException {
        return internal.updateHealthIndicatorsGetIdNewTransaction(
                newHealthIndicators, vehicleId, calculationTimestamp, calculationSyncCounter);
    }

    public String updateHealthIndicatorsGetIdExternalTransaction(
            @NotNull final HealthIndicatorOutput newHealthIndicators, @NotNull final String vehicleId,
            @NotNull final Instant calculationTimestamp, @NotNull final long calculationSyncCounter)
            throws OemHIException {
        return internal.updateHealthIndicatorsGetIdExternalTransaction(
                newHealthIndicators, vehicleId, calculationTimestamp, calculationSyncCounter);
    }

    public void deleteAllNewTransaction() throws OemHIException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws OemHIException {
        internal.deleteAllExternalTransaction();
    }

    public void deleteByIdNewTransaction(@NotNull final String id) throws OemHIException {
        internal.deleteByIdNewTransaction(id);
    }

    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        internal.deleteByIdExternalTransaction(id);
    }

    public void deleteByVehicleIdNewTransaction(@NotNull final String vehicleId) throws OemHIException {
        internal.deleteByVehicleIdNewTransaction(vehicleId);
    }

    public void deleteByVehicleIdExternalTransaction(@NotNull final String vehicleId) throws OemHIException {
        internal.deleteByVehicleIdExternalTransaction(vehicleId);
    }

    public void deleteByGearboxIdNewTransaction(@NotNull final String gearboxId) throws OemHIException {
        internal.deleteByGearboxIdNewTransaction(gearboxId);
    }

    public void deleteByGearboxIdExternalTransaction(@NotNull final String gearboxId) throws OemHIException {
        internal.deleteByGearboxIdExternalTransaction(gearboxId);
    }

    public void deleteCalculatedUntilNewTransaction(@NotNull final Instant calculatedUntil) throws OemHIException {
        internal.deleteCalculatedUntilNewTransaction(calculatedUntil);
    }

    public void deleteCalculatedUntilExternalTransaction(@NotNull final Instant calculatedUntil) throws OemHIException {
        internal.deleteCalculatedUntilExternalTransaction(calculatedUntil);
    }

    public void deleteCalculationSyncCounterUntilNewTransaction(@NotNull final long calculationSyncCounter)
            throws OemHIException {
        internal.deleteCalculationSyncCounterUntilNewTransaction(calculationSyncCounter);
    }

    public void deleteCalculationSyncCounterUntilExternalTransaction(@NotNull final long calculationSyncCounter)
            throws OemHIException {
        internal.deleteCalculationSyncCounterUntilExternalTransaction(calculationSyncCounter);
    }

    public HIHealthIndicators getByIdNewTransaction(@NotNull final String id) throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getByIdNewTransaction(id));
    }

    public HIHealthIndicators getByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getByIdExternalTransaction(id));
    }

    public List<HIHealthIndicators> getByVehicleIdNewTransaction(@NotNull final String vehicleId)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getByVehicleIdNewTransaction(vehicleId));
    }

    public List<HIHealthIndicators> getByVehicleIdExternalTransaction(@NotNull final String vehicleId)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getByVehicleIdExternalTransaction(vehicleId));
    }

    public List<HIHealthIndicators> getByGearboxIdNewTransaction(@NotNull final String gearboxId)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getByGearboxIdNewTransaction(gearboxId));
    }

    public List<HIHealthIndicators> getByGearboxIdExternalTransaction(@NotNull final String gearboxId)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getByGearboxIdExternalTransaction(gearboxId));
    }

    public List<HIHealthIndicators> getByVehicleIdOrderByCalculationSyncCounterNewTransaction(
            @NotNull final String vehicleId)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(
                internal.getByVehicleIdOrderByCalculationSyncCounterNewTransaction(vehicleId));
    }

    public List<HIHealthIndicators> getByVehicleIdOrderByCalculationSyncCounterExternalTransaction(
            @NotNull final String vehicleId)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(
                internal.getByVehicleIdOrderByCalculationSyncCounterExternalTransaction(vehicleId));
    }

    public List<HIHealthIndicators> getByGearboxIdOrderByCalculationSyncCounterNewTransaction(
            @NotNull final String gearboxId) throws OemHIException {
        return healthIndicatorsConverter.toDTO(
                internal.getByGearboxIdOrderByCalculationSyncCounterNewTransaction(gearboxId));
    }

    public List<HIHealthIndicators> getByGearboxIdOrderByCalculationSyncCounterExternalTransaction(
            @NotNull final String gearboxId) throws OemHIException {
        return healthIndicatorsConverter.toDTO(
                internal.getByGearboxIdOrderByCalculationSyncCounterExternalTransaction(gearboxId));
    }

    public List<HIHealthIndicators> getCalculatedSinceNewTransaction(@NotNull final Instant timestamp)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getCalculatedSinceNewTransaction(timestamp));
    }

    public List<HIHealthIndicators> getCalculatedSinceExternalTransaction(@NotNull final Instant timestamp)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getCalculatedSinceExternalTransaction(timestamp));
    }

    public List<HIHealthIndicators> getCalculatedUntilNewTransaction(@NotNull final Instant timestamp)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getCalculatedUntilNewTransaction(timestamp));
    }

    public List<HIHealthIndicators> getCalculatedUntilExternalTransaction(@NotNull final Instant timestamp)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getCalculatedUntilExternalTransaction(timestamp));
    }

    public List<HIHealthIndicators> getCalculationSyncCounterSinceNewTransaction(
            @NotNull final long calculationSyncCounterSince) throws OemHIException {
        return healthIndicatorsConverter.toDTO(
                internal.getCalculationSyncCounterSinceNewTransaction(calculationSyncCounterSince));
    }

    public List<HIHealthIndicators> getCalculationSyncCounterSinceExternalTransaction(
            @NotNull final long calculationSyncCounterSince)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(
                internal.getCalculationSyncCounterSinceExternalTransaction(calculationSyncCounterSince));
    }

    public List<HIHealthIndicators> getCalculationSyncCounterUntilNewTransaction(
            @NotNull final long calculationSyncCounterUntil)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(
                internal.getCalculationSyncCounterUntilNewTransaction(calculationSyncCounterUntil));
    }

    public List<HIHealthIndicators> getCalculationSyncCounterUntilExternalTransaction(
            @NotNull final long calculationSyncCounterUntil)
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(
                internal.getCalculationSyncCounterUntilExternalTransaction(calculationSyncCounterUntil));
    }

    public List<HIHealthIndicators> getAllNewTransaction() throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getAllNewTransaction());
    }

    public List<HIHealthIndicators> getAllExternalTransaction() throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getAllExternalTransaction());
    }

    public List<HIHealthIndicators> getAllOrderByCalculationTimestampNewTransaction()
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getAllOrderByCalculationTimestampNewTransaction());
    }

    public List<HIHealthIndicators> getAllOrderByCalculationTimestampExternalTransaction()
            throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getAllOrderByCalculationTimestampExternalTransaction());
    }

    public List<HIHealthIndicators> getAllOrderByCalculationSyncCounterNewTransaction() throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getAllOrderByCalculationSyncCounterNewTransaction());
    }

    public List<HIHealthIndicators> getAllOrderByCalculationSyncCounterExternalTransaction() throws OemHIException {
        return healthIndicatorsConverter.toDTO(internal.getAllOrderByCalculationSyncCounterExternalTransaction());
    }
}
