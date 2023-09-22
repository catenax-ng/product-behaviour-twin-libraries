package net.catena_x.btp.hi.oem.common.model.dto.vehicle;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.HealthIndicatorOutput;
import net.catena_x.btp.hi.oem.common.database.hi.tables.vehicle.HIVehicleTableInternal;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@Component
public class HIVehicleTable {
    @Autowired private HIVehicleTableInternal internal;
    @Autowired private HIVehicleConverter hiVehicleConverter;

    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableNewTransaction(function);
    }

    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableExternalTransaction(function);
    }

    public void insertVehicleNewTransaction(@NotNull final Vehicle newVehicle) throws OemHIException {
        internal.insertVehicleNewTransaction(newVehicle);
    }

    public void insertVehicleExternalTransaction(@NotNull final Vehicle newVehicle) throws OemHIException {
        internal.insertVehicleExternalTransaction(newVehicle);
    }

    public void insertIfNewNewTransaction(@NotNull final Vehicle vehicle) throws OemHIException {
        internal.insertIfNewNewTransaction(vehicle);
    }

    public void insertIfNewExternalTransaction(@NotNull final Vehicle vehicle) throws OemHIException {
        internal.insertIfNewExternalTransaction(vehicle);
    }

    public void deleteAllNewTransaction() throws OemHIException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws OemHIException {
        internal.deleteAllExternalTransaction();
    }

    public void appendHealthindicatorsNewTransaction(
            @NotNull String vehicleId, @NotNull final HealthIndicatorOutput newHealthIndicators,
            @NotNull final Instant calculationTimestamp, @NotNull final long calculationSyncCounter)
            throws OemHIException {
        internal.appendHealthindicatorsNewTransaction(vehicleId, newHealthIndicators,
                calculationTimestamp, calculationSyncCounter);
    }

    public void appendHealthindicatorsExternalTransaction(
            @NotNull String vehicleId, @NotNull final HealthIndicatorOutput newHealthIndicators,
            @NotNull final Instant calculationTimestamp, @NotNull final long calculationSyncCounter)
            throws OemHIException {
        internal.appendHealthindicatorsExternalTransaction(vehicleId, newHealthIndicators,
                calculationTimestamp, calculationSyncCounter);
    }

    public void deleteByIdNewTransaction(@NotNull final String id) throws OemHIException {
        internal.deleteByIdNewTransaction(id);
    }

    public void deleteByIdExternalTransaction(@NotNull final String id) throws OemHIException {
        internal.deleteByIdExternalTransaction(id);
    }

    public void deleteByVanNewTransaction(@NotNull final String van) throws OemHIException {
        internal.deleteByVanNewTransaction(van);
    }

    public void deleteByVanExternalTransaction(@NotNull final String van) throws OemHIException {
        internal.deleteByVanExternalTransaction(van);
    }

    public HIVehicle getByIdNewTransaction(@NotNull final String vehicleId) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getByIdNewTransaction(vehicleId));
    }

    public HIVehicle getByIdExternalTransaction(@NotNull final String vehicleId) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getByIdExternalTransaction(vehicleId));
    }

    public HIVehicle getByIdWithHealthIndicatorsNewTransaction(@NotNull final String vehicleId) throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getByIdWithHealthIndicatorsNewTransaction(vehicleId));
    }

    public HIVehicle getByIdWithHealthIndicatorsExternalTransaction(@NotNull final String vehicleId)
            throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getByIdWithHealthIndicatorsExternalTransaction(vehicleId));
    }

    public HIVehicle getByVanNewTransaction(@NotNull final String van) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getByVanNewTransaction(van));
    }

    public HIVehicle getByVanExternalTransaction(@NotNull final String van) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getByVanExternalTransaction(van));
    }

    public HIVehicle getByVanWithHealthIndicatorsNewTransaction(@NotNull final String van) throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(internal.getByVanWithHealthIndicatorsNewTransaction(van));
    }

    public HIVehicle getByVanWithHealthIndicatorsExternalTransaction(@NotNull final String van) throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getByVanWithHealthIndicatorsExternalTransaction(van));
    }

    public HIVehicle getByGearboxIdNewTransaction(@NotNull final String gearboxId) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getByGearboxIdNewTransaction(gearboxId));
    }

    public HIVehicle getByGearboxIdExternalTransaction(@NotNull final String gearboxId) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getByGearboxIdExternalTransaction(gearboxId));
    }

    public HIVehicle getByGearboxIdWithHealthIndicatorsNewTransaction(@NotNull final String gearboxId)
            throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getByGearboxIdWithHealthIndicatorsNewTransaction(gearboxId));
    }

    public HIVehicle getByGearboxIdWithHealthIndicatorsExternalTransaction(@NotNull final String gearboxId)
            throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getByGearboxIdWithHealthIndicatorsExternalTransaction(gearboxId));
    }

    public List<HIVehicle> getAllNewTransaction() throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getAllNewTransaction());
    }

    public List<HIVehicle> getAllExternalTransaction() throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getAllExternalTransaction());
    }

    public List<HIVehicle> getAllWithHealthIndicatorsNewTransaction() throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(internal.getAllWithHealthIndicatorsNewTransaction());
    }

    public List<HIVehicle> getAllWithHealthIndicatorsExternalTransaction() throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(internal.getAllWithHealthIndicatorsExternalTransaction());
    }

    public List<HIVehicle> getUpdatedSinceNewTransaction(@NotNull final Instant updatedSince) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getUpdatedSinceNewTransaction(updatedSince));
    }

    public List<HIVehicle> getUpdatedSinceExternalTransaction(@NotNull final Instant updatedSince)
            throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getUpdatedSinceExternalTransaction(updatedSince));
    }

    public List<HIVehicle> getUpdatedSinceWithHealthIndicatorsNewTransaction(
            @NotNull final Instant updatedSince) throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getUpdatedSinceWithHealthIndicatorsNewTransaction(updatedSince));
    }

    public List<HIVehicle> getUpdatedSinceWithHealthIndicatorsExternalTransaction(
            @NotNull final Instant updatedSince) throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getUpdatedSinceWithHealthIndicatorsExternalTransaction(updatedSince));
    }

    public List<HIVehicle> getProducedBetweenNewTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getProducedBetweenNewTransaction(producedSince, producedUntil));
    }

    public List<HIVehicle> getProducedBetweenExternalTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemHIException {
        return hiVehicleConverter.toDTO(internal.getProducedBetweenExternalTransaction(producedSince, producedUntil));
    }

    public List<HIVehicle> getProducedBetweenWithHealthIndicatorsNewTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getProducedBetweenWithHealthIndicatorsNewTransaction(producedSince, producedUntil));
    }

    public List<HIVehicle> getProducedBetweenWithHealthIndicatorsExternalTransaction(
            @NotNull final Instant producedSince, @NotNull final Instant producedUntil) throws OemHIException {
        return hiVehicleConverter.toDTOWithHealthIndicators(
                internal.getProducedBetweenWithHealthIndicatorsExternalTransaction(producedSince, producedUntil));
    }
}
