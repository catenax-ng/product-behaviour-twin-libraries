package net.catena_x.btp.libraries.oem.backend.datasource.model.registration;

public record VehicleInfo (
    String vehicleId,
    String van,
    String gearboxId,
    java.time.Instant productionDate
){}
