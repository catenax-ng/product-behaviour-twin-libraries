package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import java.nio.file.Path;

public record RuLTestdataInputElement(
        Path filename,
        RuLTestdataVehicleConfigElement vehicle,
        RuLTestdataGearboxConfigElement gearbox
) {}
