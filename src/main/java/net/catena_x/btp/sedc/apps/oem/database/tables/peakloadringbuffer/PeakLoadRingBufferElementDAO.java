package net.catena_x.btp.sedc.apps.oem.database.tables.peakloadringbuffer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "peakloadringbuffer")
@NamedNativeQuery(name = "PeakLoadRingBufferElementDAO.insert",
        query = "INSERT INTO peakloadringbuffer (id, timestamp, data_collection_period_in_ms, recuperation, " +
                "low_torque_low_revolutions, low_torque_high_revolutions, high_torque_low_revolutions, " +
                "high_torque_high_revolutions, peak_load, average_environment_temperature_in_c, " +
                "state_of_charge_at_start_normalized, state_of_charge_at_end_normalized, peak_load_capability) "
                + "VALUES (:id, :timestamp, :data_collection_period_in_ms, :recuperation, " +
                ":low_torque_low_revolutions, :low_torque_high_revolutions, :high_torque_low_revolutions, " +
                ":high_torque_high_revolutions, :peak_load, :average_environment_temperature_in_c, " +
                ":state_of_charge_at_start_normalized, :state_of_charge_at_end_normalized, :peak_load_capability)")
@NamedNativeQuery(name = "PeakLoadRingBufferElementDAO.setPeakLoadCapability",
        query = "UPDATE peakloadringbuffer SET peak_load_capability=:peak_load_capability WHERE id=:id")
@NamedNativeQuery(name = "PeakLoadRingBufferElementDAO.deleteAll",
        query = "DELETE FROM peakloadringbuffer")
@NamedNativeQuery(name = "PeakLoadRingBufferElementDAO.deleteById",
        query = "DELETE FROM peakloadringbuffer WHERE id=:id")
@NamedNativeQuery(name = "PeakLoadRingBufferElementDAO.queryAllSortByTimestamp",
        resultClass = PeakLoadRingBufferElementDAO.class,
        query = "SELECT * FROM peakloadringbuffer ORDER BY timestamp")
@NamedNativeQuery(name = "PeakLoadRingBufferElementDAO.queryById", resultClass = PeakLoadRingBufferElementDAO.class,
        query = "SELECT * FROM peakloadringbuffer WHERE id=:id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeakLoadRingBufferElementDAO {
    @Id
    @Column(name="id", length=50, updatable=false, nullable=false, unique=true)
    private String id;

    @Column(name="timestamp", updatable=false, nullable=false)
    private Instant timestamp;

    @Column(name="data_collection_period_in_ms", updatable=false, nullable=false)
    private long dataCollectionPeriodInMs;

    @Column(name="recuperation", updatable=false, nullable=false)
    private double recuperation;

    @Column(name="low_torque_low_revolutions", updatable=false, nullable=false)
    private double lowTorqueLowRevolutions;

    @Column(name="low_torque_high_revolutions", updatable=false, nullable=false)
    private double lowTorqueHighRevolutions;

    @Column(name="high_torque_low_revolutions", updatable=false, nullable=false)
    private double highTorqueLowRevolutions;

    @Column(name="high_torque_high_revolutions", updatable=false, nullable=false)
    private double highTorqueHighRevolutions;

    @Column(name="peak_load", updatable=false, nullable=false)
    private double peakLoad;

    @Column(name="average_environment_temperature_in_c", updatable=false, nullable=false)
    private double averageEnvironmentTemperatureInC;

    @Column(name="state_of_charge_at_start_normalized", updatable=false, nullable=false)
    private double stateOfChargeAtStartNormalized;

    @Column(name="state_of_charge_at_end_normalized", updatable=false, nullable=false)
    private double stateOfChargeAtEndNormalized;

    @Column(name="peak_load_capability", updatable=true, nullable=true)
    private Long peakLoadCapability;
}