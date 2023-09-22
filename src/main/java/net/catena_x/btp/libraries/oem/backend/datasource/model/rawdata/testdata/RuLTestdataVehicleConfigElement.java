package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import java.time.Instant;

public record RuLTestdataVehicleConfigElement(
        String vin,
        String van,
        String catenaxId,
        String manufacturerId,
        Instant manufacturingDate
) {}
