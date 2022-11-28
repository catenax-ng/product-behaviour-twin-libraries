package net.catena_x.btp.libraries.oem.backend.datasource.model.registration;

import java.time.Instant;

public record VehicleInfo (
    String vehicleId,
    String van,
    String gearboxId,
    Instant productionDate
){}
