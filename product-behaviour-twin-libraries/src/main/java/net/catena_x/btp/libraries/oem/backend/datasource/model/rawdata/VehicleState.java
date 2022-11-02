package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata;

import java.time.Instant;

public record VehicleState(
        String vehicleId,
        Instant creationTimestamp,
        float mileage,
        long operatingSeconds
) {}
