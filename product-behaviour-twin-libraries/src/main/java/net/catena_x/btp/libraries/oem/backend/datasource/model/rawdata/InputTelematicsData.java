package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata;

import java.util.List;

public record InputTelematicsData(
        VehicleState state,
        List<String> loadCollectives,
        List<double[]> adaptionValues
){}
