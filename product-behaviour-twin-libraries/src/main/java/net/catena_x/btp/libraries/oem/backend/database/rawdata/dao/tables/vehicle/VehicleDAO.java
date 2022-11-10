package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "vehicles", uniqueConstraints={@UniqueConstraint(columnNames = {"vehicle_id"}),
                                             @UniqueConstraint(columnNames = {"van"}),
                                             @UniqueConstraint(columnNames = {"gearbox_id"})})
@NamedNativeQuery(name = "VehicleDAO.register",
        query = "INSERT INTO vehicles (vehicle_id, van, gearbox_id, production_date, sync_counter) " +
                "VALUES (:vehicle_id, :van, :gearbox_id, :production_date, :sync_counter)")
@NamedNativeQuery(name = "VehicleDAO.updateNewestTelematicsIdByVehicleId",
        query = "UPDATE vehicles SET newest_telematics_id=:telematics_id, " +
                "update_timestamp=CURRENT_TIMESTAMP, sync_counter=:sync_counter WHERE vehicle_id=:vehicle_id")
@NamedNativeQuery(name = "VehicleDAO.deleteByVehilceId",
        query = "DELETE FROM vehicles WHERE vehicle_id=:vehicle_id")
@NamedNativeQuery(name = "VehicleDAO.deleteByVan",
        query = "DELETE FROM vehicles WHERE van=:van")
@NamedNativeQuery(name = "VehicleDAO.queryAll", resultClass = VehicleDAO.class,
        query = "SELECT * FROM vehicles")
@NamedNativeQuery(name = "VehicleDAO.queryByVehicleId", resultClass = VehicleDAO.class,
        query = "SELECT * FROM vehicles WHERE vehicle_id=:vehicle_id")
@NamedNativeQuery(name = "VehicleDAO.queryByVan", resultClass = VehicleDAO.class,
        query = "SELECT * FROM vehicles WHERE van=:van")
@NamedNativeQuery(name = "VehicleDAO.queryByGearboxId", resultClass = VehicleDAO.class,
        query = "SELECT * FROM vehicles WHERE gearbox_id=:gearbox_id")
@NamedNativeQuery(name = "VehicleDAO.queryUpdatedSince", resultClass = VehicleDAO.class,
        query = "SELECT * FROM vehicles WHERE update_timestamp>=:updated_since")
@NamedNativeQuery(name = "VehicleDAO.queryUpdatedSinceSyncCounter", resultClass = VehicleDAO.class,
        query = "SELECT * FROM vehicles WHERE sync_counter>=:updated_since")
@NamedNativeQuery(name = "VehicleDAO.queryByProductionDate", resultClass = VehicleDAO.class,
        query = "SELECT * FROM vehicles WHERE production_date>=:produced_since " +
                "AND production_date<=:produced_until")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDAO {
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

    @Column(name="sync_counter", nullable=false)
    private long syncCounter;

    @Column(name="newest_telematics_id", length=50)
    private String newestTelematicsId;
}
