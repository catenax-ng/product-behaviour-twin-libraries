package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.util;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RawdataInfoTableInitializer {
    @Autowired private InfoTable infoTable;

    public void init() throws OemDatabaseException {
        infoTable.setInfoItemNewTransaction(InfoKey.DATAVERSION, "DV_0.0.99");
        infoTable.setInfoItemNewTransaction(InfoKey.ADAPTIONVALUEINFO, "{}");
        infoTable.setInfoItemNewTransaction(InfoKey.LOADSPECTRUMINFO,
                "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
    }
}
