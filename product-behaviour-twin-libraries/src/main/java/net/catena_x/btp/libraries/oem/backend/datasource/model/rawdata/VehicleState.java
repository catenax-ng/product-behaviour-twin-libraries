package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata;

public record VehicleState(
        String vehicleid,
        java.time.Instant timestamp,
        float mileage,
        long operatingseconds
) {}
