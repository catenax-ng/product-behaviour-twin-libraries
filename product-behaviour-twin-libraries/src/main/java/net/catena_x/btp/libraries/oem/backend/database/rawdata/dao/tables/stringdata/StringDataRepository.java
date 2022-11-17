package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.stringdata;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface StringDataRepository extends Repository<StringDataDAO, Long> {
    @Modifying void insert(@Param("base_id") @NotNull final String baseId,
                  @Param("index") @NotNull final long index,
                  @Param("value") @NotNull final String value);
    @Modifying void deleteAll();
    @Modifying void deleteByBaseId(@Param("base_id") @NotNull final String baseId);
    List<StringDataDAO> queryAll();
    List<StringDataDAO> queryByBaseId(@Param("base_id") @NotNull final String baseId);
}
