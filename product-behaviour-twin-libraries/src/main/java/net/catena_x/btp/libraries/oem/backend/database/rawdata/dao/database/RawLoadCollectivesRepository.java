package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.LoadCollectives;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawLoadCollectivesRepository extends JpaRepository<LoadCollectives, String> {}
