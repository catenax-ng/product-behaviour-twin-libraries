package net.catena_x.btp.hi.oem.common.database.hi.tables.calculation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "hicalculations")
@NamedNativeQuery(name = "HICalculationDAO.insert",
        query = "INSERT INTO hicalculations (id, calculation_timestamp, "
                + "calculation_sync_counter_min, calculation_sync_counter_max, status) "
                + "VALUES (:id, :calculation_timestamp, "
                + ":calculation_sync_counter_min, :calculation_sync_counter_max, :status)")
@NamedNativeQuery(name = "HICalculationDAO.createNow",
        query = "INSERT INTO hicalculations (id, calculation_timestamp, "
                + "calculation_sync_counter_min, calculation_sync_counter_max, status) "
                + "VALUES (:id, CURRENT_TIMESTAMP, :calculation_sync_counter_min, :calculation_sync_counter_max, "
                + ":status)")
@NamedNativeQuery(name = "HICalculationDAO.updateStatus",
        query = "UPDATE hicalculations SET status=:status WHERE id=:id")
@NamedNativeQuery(name = "HICalculationDAO.setMessage",
        query = "UPDATE hicalculations SET message=:message WHERE id=:id")
@NamedNativeQuery(name = "HICalculationDAO.deleteAll",
        query = "DELETE FROM hicalculations")
@NamedNativeQuery(name = "HICalculationDAO.deleteById",
        query = "DELETE FROM hicalculations WHERE id=:id")
@NamedNativeQuery(name = "HICalculationDAO.deleteByStatus",
        query = "DELETE FROM hicalculations WHERE status=:status")
@NamedNativeQuery(name = "HICalculationDAO.deleteCalculatedUntil",
        query = "DELETE FROM hicalculations WHERE calculation_timestamp<=:calculated_until")
@NamedNativeQuery(name = "HICalculationDAO.deleteCalculationSyncCounterUntil",
        query = "DELETE FROM hicalculations WHERE calculation_sync_counter_max<=:calculation_sync_counter_until")
@NamedNativeQuery(name = "HICalculationDAO.queryAll", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations")
@NamedNativeQuery(name = "HICalculationDAO.queryById", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations WHERE id=:id")
@NamedNativeQuery(name = "HICalculationDAO.queryByStatus", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations WHERE status=:status")
@NamedNativeQuery(name = "HICalculationDAO.queryByStatusOrderByCalculationSyncCounter",
        resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations WHERE status=:status ORDER BY calculation_sync_counter_max")
@NamedNativeQuery(name = "HICalculationDAO.queryAllOrderByCalculationTimestamp", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations ORDER BY calculation_timestamp")
@NamedNativeQuery(name = "HICalculationDAO.queryAllOrderByCalculationSyncCounter", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations ORDER BY calculation_sync_counter_max")
@NamedNativeQuery(name = "HICalculationDAO.queryByCalculationSince", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations WHERE calculation_timestamp<=:calculation_timestamp_since")
@NamedNativeQuery(name = "HICalculationDAO.queryByCalculationUntil", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations WHERE calculation_timestamp<=:calculation_timestamp_until")
@NamedNativeQuery(name = "HICalculationDAO.queryByCalculationSyncCounterSince", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations WHERE calculation_sync_counter_max>=:calculation_sync_counter_since")
@NamedNativeQuery(name = "HICalculationDAO.queryByCalculationSyncCounterUntil", resultClass = HICalculationDAO.class,
        query = "SELECT * FROM hicalculations WHERE calculation_sync_counter_max>=:calculation_sync_counter_until")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HICalculationDAO {
    @Id
    @Column(name="id", length=50, updatable=false, nullable=false, unique=true)
    private String id;

    @Column(name="calculation_timestamp", updatable=false, nullable=false)
    private Instant calculationTimestamp;

    @Column(name="calculation_sync_counter_min", updatable=false, nullable=false)
    private long calculationSyncCounterMin;

    @Column(name="calculation_sync_counter_max", updatable=false, nullable=false)
    private long calculationSyncCounterMax;

    @Column(name="status", updatable=true, nullable=false)
    private String status;

    @Column(name="message", length=65000, updatable=true, nullable=true)
    private String message;
}
