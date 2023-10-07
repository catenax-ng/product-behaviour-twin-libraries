package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "telematics_data")
@NamedNativeQuery(name = "TelematicsDataDAO.insert",
        query = "INSERT INTO telematics_data (id, vehicle_id, load_spectra, adaption_values, sync_counter) " +
                "VALUES (:id, :vehicle_id, :load_spectra, :adaption_values, :sync_counter)")
@NamedNativeQuery(name = "TelematicsDataDAO.deleteAll",
        query = "DELETE FROM telematics_data")
@NamedNativeQuery(name = "TelematicsDataDAO.deleteById",
        query = "DELETE FROM telematics_data WHERE id=:id")
@NamedNativeQuery(name = "TelematicsDataDAO.deleteByVehicleId",
        query = "DELETE FROM telematics_data WHERE vehicleId=:vehicle_id")
@NamedNativeQuery(name = "TelematicsDataDAO.deleteStoredUntil",
        query = "DELETE FROM telematics_data WHERE storage_timestamp<=:stored_until")
@NamedNativeQuery(name = "TelematicsDataDAO.deleteSyncCounterUntil",
        query = "DELETE FROM telematics_data WHERE sync_counter<=:sync_counter")
@NamedNativeQuery(name = "TelematicsDataDAO.queryAll", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data")
@NamedNativeQuery(name = "TelematicsDataDAO.queryById", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data WHERE id=:id")
@NamedNativeQuery(name = "TelematicsDataDAO.queryAllOrderByStorageTimestamp", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data ORDER BY storage_timestamp")
@NamedNativeQuery(name = "TelematicsDataDAO.queryAllOrderBySyncCounter", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data ORDER BY sync_counter")
@NamedNativeQuery(name = "TelematicsDataDAO.queryByVehicleId", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data WHERE vehicle_id=:vehicle_id")
@NamedNativeQuery(name = "TelematicsDataDAO.queryByVehicleIdOrderBySyncCounter", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data WHERE vehicle_id=:vehicle_id ORDER BY sync_counter")
@NamedNativeQuery(name = "TelematicsDataDAO.queryByStorageSince", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data WHERE storage_timestamp<=:storage_timestamp_since")
@NamedNativeQuery(name = "TelematicsDataDAO.queryByStorageUntil", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data WHERE storage_timestamp<=:storage_timestamp_until")
@NamedNativeQuery(name = "TelematicsDataDAO.queryBySyncCounterSince", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data WHERE sync_counter>=:sync_counter_since")
@NamedNativeQuery(name = "TelematicsDataDAO.queryBySyncCounterUntil", resultClass = TelematicsDataDAO.class,
        query = "SELECT * FROM telematics_data WHERE sync_counter<=:sync_counter_until")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelematicsDataDAO {
    @Id
    @Column(name="id", length=50, updatable=false, nullable=false, unique=true)
    private String id;

    @Column(name="storage_timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            insertable=false, updatable=false, nullable=false)
    private Instant storageTimestamp;

    @Column(name="sync_counter", updatable=false, nullable=false)
    private long syncCounter;

    @Column(name="vehicle_id", length=50, updatable=false, nullable=false)
    private String vehicleId;

    @Column(name="load_spectra", length=65000, updatable=false, nullable=false)
    private String loadSpectra;

    @Column(name="adaption_values", length=65000, updatable=false, nullable=false)
    private String adaptionValues;
}
