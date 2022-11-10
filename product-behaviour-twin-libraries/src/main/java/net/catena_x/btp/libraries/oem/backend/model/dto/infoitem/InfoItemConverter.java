package net.catena_x.btp.libraries.oem.backend.model.dto.infoitem;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.InfoItemDAO;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class InfoItemConverter extends DAOConverter<InfoItemDAO, InfoItem> {
    protected InfoItem toDTOSourceExists(@NotNull final InfoItemDAO source) {
        return new InfoItem(InfoKey.valueOf(source.getKey()),
                            source.getValue(), source.getQueryTimestamp());
    }

    protected InfoItemDAO toDAOSourceExists(@NotNull final InfoItem source) {
        return new InfoItemDAO(source.getKey().toString(),
                               source.getValue(), source.getQueryTimestamp());
    }
}
