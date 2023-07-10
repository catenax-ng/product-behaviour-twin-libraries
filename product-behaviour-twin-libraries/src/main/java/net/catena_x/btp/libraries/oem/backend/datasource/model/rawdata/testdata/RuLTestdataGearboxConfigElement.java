package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import java.time.Instant;

public record RuLTestdataGearboxConfigElement(
        String catenaxId,
        String manufacturerId,
        Instant manufacturingDate,
        Instant assembledOn
) {}
