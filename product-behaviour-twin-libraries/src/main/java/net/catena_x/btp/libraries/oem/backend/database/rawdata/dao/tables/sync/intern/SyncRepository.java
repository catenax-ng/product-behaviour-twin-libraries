package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.intern;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.model.SyncDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;

public interface SyncRepository extends Repository<SyncDAO, String> {
    @Modifying void init(@Param("id") @NotNull final String id);
    @Modifying void setCurrent(@Param("id") @NotNull final String id,
                               @Param("sync_counter") @NotNull final long syncCounter);
    @Modifying void deleteAll();
    SyncDAO getCurrent(@Param("id") @NotNull final String id);
}
