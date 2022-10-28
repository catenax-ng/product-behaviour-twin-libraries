package net.catena_x.btp.libraries.oem.backend.datasource.model.registration;

public record VehicleInfo (
    String id,
    String van,
    java.time.Instant productionDate
){}
