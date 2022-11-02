package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawTelemetricsDataRepository extends JpaRepository<TelemetricsData, String> {}
