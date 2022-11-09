package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.converter;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.model.InfoItemDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.InfoItem;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class InfoItemConverter extends DAOConverter<InfoItemDAO, InfoItem> {
    protected InfoItem toDTOSourceExists(@NotNull final InfoItemDAO source) {
        return new InfoItem(InfoItem.InfoKey.valueOf(source.getKey()),
                            source.getValue(), source.getQueryTimestamp());
    }

    protected InfoItemDAO toDAOSourceExists(@NotNull final InfoItem source) {
        return new InfoItemDAO(source.getKey().toString(),
                               source.getValue(), source.getQueryTimestamp());
    }
}
