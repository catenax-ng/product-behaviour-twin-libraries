package net.catena_x.btp.hi.oem.common.database.hi.tables.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "hivehicles", uniqueConstraints={@UniqueConstraint(columnNames = {"vehicle_id"}),
        @UniqueConstraint(columnNames = {"van"}),
        @UniqueConstraint(columnNames = {"gearbox_id"})})
@NamedNativeQuery(name = "HIVehicleDAO.insert",
        query = "INSERT INTO hivehicles (vehicle_id, van, gearbox_id, production_date) " +
                "VALUES (:vehicle_id, :van, :gearbox_id, :production_date)")
@NamedNativeQuery(name = "HIVehicleDAO.updateNewestHealthindicatorsIdByVehicleId",
        query = "UPDATE hivehicles SET newest_healthindicators_id=:newest_healthindicators_id, " +
                "update_timestamp=CURRENT_TIMESTAMP WHERE vehicle_id=:vehicle_id")
@NamedNativeQuery(name = "HIVehicleDAO.deleteAll",
        query = "DELETE FROM hivehicles")
@NamedNativeQuery(name = "HIVehicleDAO.deleteByVehilceId",
        query = "DELETE FROM hivehicles WHERE vehicle_id=:vehicle_id")
@NamedNativeQuery(name = "HIVehicleDAO.deleteByVan",
        query = "DELETE FROM hivehicles WHERE van=:van")
@NamedNativeQuery(name = "HIVehicleDAO.queryAll", resultClass = HIVehicleDAO.class,
        query = "SELECT * FROM hivehicles")
@NamedNativeQuery(name = "HIVehicleDAO.queryByVehicleId", resultClass = HIVehicleDAO.class,
        query = "SELECT * FROM hivehicles WHERE vehicle_id=:vehicle_id")
@NamedNativeQuery(name = "HIVehicleDAO.queryByVan", resultClass = HIVehicleDAO.class,
        query = "SELECT * FROM hivehicles WHERE van=:van")
@NamedNativeQuery(name = "HIVehicleDAO.queryByGearboxId", resultClass = HIVehicleDAO.class,
        query = "SELECT * FROM hivehicles WHERE gearbox_id=:gearbox_id")
@NamedNativeQuery(name = "HIVehicleDAO.queryUpdatedSince", resultClass = HIVehicleDAO.class,
        query = "SELECT * FROM hivehicles WHERE update_timestamp>=:updated_since")
@NamedNativeQuery(name = "HIVehicleDAO.queryByProductionDate", resultClass = HIVehicleDAO.class,
        query = "SELECT * FROM hivehicles WHERE production_date>=:produced_since " +
                "AND production_date<=:produced_until")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIVehicleDAO {
    @Id
    @Column(name="vehicle_id", length=50, nullable=false)
    private String vehicleId;

    @Column(name="van", length=50, nullable=false)
    private String van;

    @Column(name="gearbox_id", length=50, nullable=false)
    private String gearboxId;

    @Column(name="production_date", nullable=false)
    private Instant productionDate;

    @Column(name="update_timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable=false)
    private Instant updateTimestamp;

    @Column(name="newest_healthindicators_id", length=50)
    private String newestHealthindicatorsId;
}
