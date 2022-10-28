package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawInfoItemRepository extends JpaRepository<InfoItem, InfoItem.InfoKey> {}