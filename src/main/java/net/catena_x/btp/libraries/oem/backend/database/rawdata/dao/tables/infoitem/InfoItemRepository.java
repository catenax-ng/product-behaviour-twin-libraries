package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface InfoItemRepository extends Repository<InfoItemDAO, String> {
    @Modifying void insert(@Param("key") @NotNull final String key, @Param("value") @NotNull final String value);
    @Modifying void update(@Param("key") @NotNull final String key, @Param("value") @NotNull final String value);
    @Modifying void delete(@Param("key") @NotNull final String key);
    @Modifying void deleteAll();
    List<InfoItemDAO> queryAll();
    InfoItemDAO queryByKey(@Param("key") @NotNull final String key);
}
