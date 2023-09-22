package net.catena_x.btp.hi.oem.common.model.dto.infoitem;

import net.catena_x.btp.hi.oem.common.database.hi.tables.infoitem.HIInfoItemDAO;
import net.catena_x.btp.hi.oem.common.model.enums.HIInfoKey;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class HIInfoItemConverter extends DAOConverter<HIInfoItemDAO, HIInfoItem> {
    protected HIInfoItem toDTOSourceExists(@NotNull final HIInfoItemDAO source) {
        return new HIInfoItem(HIInfoKey.valueOf(source.getKey()), source.getValue());
    }

    protected HIInfoItemDAO toDAOSourceExists(@NotNull final HIInfoItem source) {
        return new HIInfoItemDAO(source.getKey().toString(), source.getValue());
    }
}
