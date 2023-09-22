package net.catena_x.btp.rul.oem.backend.database.rul.tables.calculation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "rulcalculations")
@NamedNativeQuery(name = "RuLCalculationDAO.insert",
        query = "INSERT INTO rulcalculations (id, requester_notification_id, calculation_timestamp, status, rul, "
                + "requester_url, requester_bpn, requester_result_asset_id) "
                + "VALUES (:id, :requester_notification_id, :calculation_timestamp, :status, :rul, "
                +":requester_url, :requester_bpn, :requester_result_asset_id)")
@NamedNativeQuery(name = "RuLCalculationDAO.createNow",
        query = "INSERT INTO rulcalculations (id, requester_notification_id, calculation_timestamp, status, rul, "
                + "requester_url, requester_bpn, requester_result_asset_id) "
                + "VALUES (:id, :requester_notification_id, CURRENT_TIMESTAMP, :status, :rul, "
                +":requester_url, :requester_bpn, :requester_result_asset_id)")
@NamedNativeQuery(name = "RuLCalculationDAO.updateStatus",
        query = "UPDATE rulcalculations SET status=:status WHERE id=:id")
@NamedNativeQuery(name = "RuLCalculationDAO.updateStatusAndRuL",
        query = "UPDATE rulcalculations SET status=:status, rul=:rul WHERE id=:id")
@NamedNativeQuery(name = "RuLCalculationDAO.deleteAll",
        query = "DELETE FROM rulcalculations")
@NamedNativeQuery(name = "RuLCalculationDAO.deleteById",
        query = "DELETE FROM rulcalculations WHERE id=:id")
@NamedNativeQuery(name = "RuLCalculationDAO.deleteByRequesterNotificationId",
        query = "DELETE FROM rulcalculations WHERE requester_notification_id=:requester_notification_id")
@NamedNativeQuery(name = "RuLCalculationDAO.deleteByStatus",
        query = "DELETE FROM rulcalculations WHERE status=:status")
@NamedNativeQuery(name = "RuLCalculationDAO.deleteCalculatedUntil",
        query = "DELETE FROM rulcalculations WHERE calculation_timestamp<=:calculated_until")
@NamedNativeQuery(name = "RuLCalculationDAO.queryAll", resultClass = RuLCalculationDAO.class,
        query = "SELECT * FROM rulcalculations")
@NamedNativeQuery(name = "RuLCalculationDAO.queryById", resultClass = RuLCalculationDAO.class,
        query = "SELECT * FROM rulcalculations WHERE id=:id")
@NamedNativeQuery(name = "RuLCalculationDAO.queryByRequesterNotificationId", resultClass = RuLCalculationDAO.class,
        query = "SELECT * FROM rulcalculations WHERE requester_notification_id=:requester_notification_id")
@NamedNativeQuery(name = "RuLCalculationDAO.queryByStatus", resultClass = RuLCalculationDAO.class,
        query = "SELECT * FROM rulcalculations WHERE status=:status")
@NamedNativeQuery(name = "RuLCalculationDAO.queryAllOrderByCalculationTimestamp", resultClass = RuLCalculationDAO.class,
        query = "SELECT * FROM rulcalculations ORDER BY calculation_timestamp")
@NamedNativeQuery(name = "RuLCalculationDAO.queryByCalculationSince", resultClass = RuLCalculationDAO.class,
        query = "SELECT * FROM rulcalculations WHERE calculation_timestamp<=:calculation_timestamp_since")
@NamedNativeQuery(name = "RuLCalculationDAO.queryByCalculationUntil", resultClass = RuLCalculationDAO.class,
        query = "SELECT * FROM rulcalculations WHERE calculation_timestamp<=:calculation_timestamp_until")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLCalculationDAO {
    @Id
    @Column(name="id", length=50, updatable=false, nullable=false, unique=true)
    private String id;

    @Column(name="requester_notification_id", length=50, updatable=false, nullable=false)
    private String requesterNotificationId;

    @Column(name="calculation_timestamp", updatable=false, nullable=false)
    private Instant calculationTimestamp;

    @Column(name="status", updatable=true, nullable=false)
    private String status;

    @Column(name="rul", length=10000, updatable=true, nullable=true)
    private String rul;

    @Column(name="requester_url", length=2000, updatable=false, nullable=false)
    private String requesterUrl;

    @Column(name="requester_bpn", length=40, updatable=false, nullable=false)
    private String requesterBpn;

    @Column(name="requester_result_asset_id", length=200, updatable=false, nullable=false)
    private String requesterResultAssetId;
}
