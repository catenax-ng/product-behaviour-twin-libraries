package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.AdaptionValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawAdaptionValuesRepository extends JpaRepository<AdaptionValues, String> {}