package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base;

import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;

public interface TelemetricsRawInputSource {
    VehicleState state();
}
