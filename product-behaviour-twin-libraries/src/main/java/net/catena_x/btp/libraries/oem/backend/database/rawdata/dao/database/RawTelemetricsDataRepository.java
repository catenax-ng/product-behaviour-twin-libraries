package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface RawTelemetricsDataRepository extends JpaRepository<TelemetricsData, String> {
    List<TelemetricsData> findByVehicleId(String vehicleId);
    List<TelemetricsData> findByStorageTimestampGreaterThanEqual(Instant storageTimestamp);
    List<TelemetricsData> findAllByOrderByStorageTimestampAsc();
}
