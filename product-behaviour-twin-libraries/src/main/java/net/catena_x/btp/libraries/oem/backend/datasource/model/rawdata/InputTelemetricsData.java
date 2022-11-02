package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsRawInputSource;

import java.util.List;

public record InputTelemetricsData(
        VehicleState state,
        List<String> loadCollectives,
        List<double[]> adaptionValues)
        implements TelemetricsRawInputSource {
}
