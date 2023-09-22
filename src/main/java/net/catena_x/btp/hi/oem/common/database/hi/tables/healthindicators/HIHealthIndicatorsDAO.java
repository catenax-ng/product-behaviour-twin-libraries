package net.catena_x.btp.hi.oem.common.database.hi.tables.healthindicators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "hihealthindicators")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.insert",
        query = "INSERT INTO hihealthindicators (id, vehicle_id, gearbox_id, calculation_timestamp, " +
                "calculation_sync_counter, values) "
                + "VALUES (:id, :vehicle_id, :gearbox_id, :calculation_timestamp, :calculation_sync_counter, :values)")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.deleteAll",
        query = "DELETE FROM hihealthindicators")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.deleteById",
        query = "DELETE FROM hihealthindicators WHERE id=:id")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.deleteByVehicleId",
        query = "DELETE FROM hihealthindicators WHERE vehicleId=:vehicle_id")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.deleteByGearboxId",
        query = "DELETE FROM hihealthindicators WHERE gearbox_id=:gearbox_id")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.deleteCalculatedUntil",
        query = "DELETE FROM hihealthindicators WHERE calculation_timestamp<=:calculated_until")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.deleteCalculationSyncCounterUntil",
        query = "DELETE FROM hihealthindicators WHERE calculation_sync_counter<=:calculation_sync_counter")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryAll", resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryById", resultClass = HIHealthIndicatorsDAO.class,
        query = "DELETE * FROM hihealthindicators WHERE id=:id")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryAllOrderByCalculationTimestamp",
        resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators ORDER BY calculation_timestamp")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryAllOrderByCalculationSyncCounter",
        resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators ORDER BY calculation_sync_counter")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryByVehicleId", resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators WHERE vehicle_id=:vehicle_id")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryByGearboxId", resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators WHERE gearbox_id=:gearbox_id")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryByVehicleIdOrderByCalculationSyncCounter",
        resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators WHERE vehicle_id=:vehicle_id ORDER BY calculation_sync_counter")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryByGearboxIdOrderByCalculationSyncCounter",
        resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators WHERE gearbox_id=:gearbox_id ORDER BY calculation_sync_counter")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryByCalculationSince", resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators WHERE calculation_timestamp<=:calculation_timestamp_since")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryByCalculationUntil", resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators WHERE calculation_timestamp<=:calculation_timestamp_until")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryByCalculationSyncCounterSince",
        resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators WHERE calculation_sync_counter>=:calculation_sync_counter_since")
@NamedNativeQuery(name = "HIHealthIndicatorsDAO.queryByCalculationSyncCounterUntil",
        resultClass = HIHealthIndicatorsDAO.class,
        query = "SELECT * FROM hihealthindicators WHERE calculation_sync_counter<=:calculation_sync_counter_until")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIHealthIndicatorsDAO {
    @Id
    @Column(name="id", length=50, updatable=false, nullable=false, unique=true)
    private String id;

    @Column(name="calculation_timestamp", updatable=false, nullable=false)
    private Instant calculationTimestamp;

    @Column(name="calculation_sync_counter", updatable=false, nullable=false)
    private long calculationSyncCounter;

    @Column(name="vehicle_id", length=50, updatable=false, nullable=false)
    private String vehicleId;

    @Column(name="gearbox_id", length=50, updatable=false, nullable=false)
    private String gearboxId;

    @Column(name="values", length=65000, updatable=false, nullable=false)
    private String values;
}
