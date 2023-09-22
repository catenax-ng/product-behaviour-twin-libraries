package net.catena_x.btp.hi.oem.common.database.hi.tables.healthindicators;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public interface HIHealthIndicatorsRepository extends Repository<HIHealthIndicatorsDAO, String> {
    @Modifying void insert(@Param("id") @NotNull final String id,
                           @Param("vehicle_id") @NotNull final String vehicleId,
                           @Param("gearbox_id") @NotNull final String gearboxId,
                           @Param("calculation_timestamp") @NotNull final Instant calculationTimestamp,
                           @Param("calculation_sync_counter") @NotNull final long calculationSyncCounter,
                           @Param("values") @NotNull final String values);
    @Modifying void deleteAll();
    @Modifying void deleteById(@Param("id") @NotNull final String id);
    @Modifying void deleteByVehicleId(@Param("vehicle_id") @NotNull final String vehicleId);
    @Modifying void deleteByGearboxId(@Param("gearbox_id") @NotNull final String gearboxId);
    @Modifying void deleteCalculatedUntil(@Param("calculated_until") @NotNull final Instant calculatedUntil);
    @Modifying void deleteCalculationSyncCounterUntil(
            @Param("calculation_sync_counter") @NotNull final long calculationSyncCounter);
    List<HIHealthIndicatorsDAO> queryAll();
    HIHealthIndicatorsDAO queryById(@Param("id") @NotNull final String id);
    List<HIHealthIndicatorsDAO> queryAllOrderByCalculationTimestamp();
    List<HIHealthIndicatorsDAO> queryAllOrderByCalculationSyncCounter();
    List<HIHealthIndicatorsDAO> queryByVehicleId(@Param("vehicle_id") @NotNull final String vehicleId);
    List<HIHealthIndicatorsDAO> queryByGearboxId(@Param("gearbox_id") @NotNull final String gearboxId);
    List<HIHealthIndicatorsDAO> queryByVehicleIdOrderByCalculationSyncCounter(
            @Param("vehicle_id") @NotNull final String vehicleId);
    List<HIHealthIndicatorsDAO> queryByGearboxIdOrderByCalculationSyncCounter(
            @Param("gearbox_id") @NotNull final String gearboxId);
    List<HIHealthIndicatorsDAO> queryByCalculationSince(
            @Param("calculation_timestamp_since") @NotNull final Instant calculationTimestampSince);
    List<HIHealthIndicatorsDAO> queryByCalculationUntil(
            @Param("calculation_timestamp_until") @NotNull final Instant calculationTimestampUntil);
    List<HIHealthIndicatorsDAO> queryByCalculationSyncCounterSince(
            @Param("calculation_sync_counter_since") @NotNull final long calculationSyncCounterSince);
    List<HIHealthIndicatorsDAO> queryByCalculationSyncCounterUntil(
            @Param("calculation_sync_counter_until") @NotNull final long calculationSyncCounterUntil);
}
