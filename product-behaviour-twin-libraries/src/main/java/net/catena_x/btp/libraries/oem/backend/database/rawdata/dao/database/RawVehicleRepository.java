package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RawVehicleRepository extends JpaRepository<Vehicle, String> {}
