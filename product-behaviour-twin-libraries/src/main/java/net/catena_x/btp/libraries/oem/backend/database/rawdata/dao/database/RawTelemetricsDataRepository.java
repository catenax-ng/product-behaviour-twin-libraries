package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface RawTelemetricsDataRepository extends JpaRepository<TelemetricsData, String> {
    List<TelemetricsData> findByStorageTimestampGreaterThanEqual(Instant storageTimestamp);
}
