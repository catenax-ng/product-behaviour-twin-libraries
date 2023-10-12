package net.catena_x.btp.sedc.apps.oem.database.tables.peakloadringbuffer;

import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public interface PeakLoadRingBufferRepository extends Repository<PeakLoadRingBufferElementDAO, String> {
    @Modifying void insert(
            @Param("id") @NotNull final String id,
            @Param("timestamp") @NotNull final Instant timestamp,
            @Param("data_collection_period_in_ms") final long dataCollectionPeriodInMs,
            @Param("recuperation") final double recuperation,
            @Param("low_torque_low_revolutions") final double lowTorqueLowRevolutions,
            @Param("low_torque_high_revolutions") final double lowTorqueHighRevolutions,
            @Param("high_torque_low_revolutions") final double highTorqueLowRevolutions,
            @Param("high_torque_high_revolutions") final double highTorqueHighRevolutions,
            @Param("peak_load") final double peakLoad,
            @Param("average_environment_temperature_in_c") final double averageEnvironmentTemperatureInC,
            @Param("state_of_charge_at_start_normalized") final double stateOfChargeAtStartNormalized,
            @Param("state_of_charge_at_end_normalized") final double stateOfChargeAtEndNormalized,
            @Param("peak_load_capability") @Nullable final Long peakLoadCapability);

    @Modifying void setPeakLoadCapability(@Param("id") @NotNull final String id,
                                          @Param("peak_load_capability") final long peakLoadCapability);

    @Modifying void deleteAll();
    @Modifying void deleteById(@Param("id") @NotNull final String id);

    List<PeakLoadRingBufferElementDAO> queryAllSortByTimestamp();
    PeakLoadRingBufferElementDAO queryById(@Param("id") @NotNull final String id);
}