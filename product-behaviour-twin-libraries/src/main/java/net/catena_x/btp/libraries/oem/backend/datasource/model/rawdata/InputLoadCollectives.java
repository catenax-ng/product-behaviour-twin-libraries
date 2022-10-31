package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsRawInputSource;

import java.util.List;

public record InputLoadCollectives(
        VehicleState state,
        List<String> loadcollectives)
        implements TelemetricsRawInputSource {
}
