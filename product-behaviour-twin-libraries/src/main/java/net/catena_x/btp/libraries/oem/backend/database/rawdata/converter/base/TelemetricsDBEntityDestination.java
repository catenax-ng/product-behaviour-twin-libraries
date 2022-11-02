package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base;

import java.time.Instant;

public interface TelemetricsDBEntityDestination {
    void setId(String id);
    void setVehicleId(String vehicleId);
    void setCreationTimestamp(Instant creationTimestamp);
    void setMileage(float mileage);
    void setOperatingSeconds(long operatingSeconds);
}
