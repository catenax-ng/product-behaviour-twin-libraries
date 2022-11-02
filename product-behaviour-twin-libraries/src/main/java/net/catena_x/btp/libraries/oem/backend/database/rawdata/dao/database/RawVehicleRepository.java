package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RawVehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByUpdateTimestampGreaterThan(Instant updateTimestamp);
    List<Vehicle> findByProductionDateBetween(Instant producedSince, Instant producedUntil);
    void deleteById(String id);
    void deleteByVan(String van);
    Optional<Vehicle> findById(String id);
    Optional<Vehicle> findByVan(String van);
}
