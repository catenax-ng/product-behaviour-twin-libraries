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



    @Query("SELECT v FROM Vehicle v WHERE v.van = :van")
    Vehicle findByVan(@Param("van") String van);

    //@Query("DELETE FROM Vehicle v WHERE v.id = :id")
    //void deleteById(@Param("id") String id);

    @Query("DELETE FROM Vehicle v WHERE v.van = :van")
    void deleteByVan(@Param("van") String van);
}
