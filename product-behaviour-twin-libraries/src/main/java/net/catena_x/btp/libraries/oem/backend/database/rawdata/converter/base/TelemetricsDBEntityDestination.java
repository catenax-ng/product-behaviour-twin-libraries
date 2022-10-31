package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base;

import java.time.Instant;

public interface TelemetricsDBEntityDestination {
    void setId(String id);
    void setVehicleid(String vehicleid);
    void setTimestamp(Instant timestamp);
    void setMileage(float mileage);
    void setOperatingseconds(long operatingseconds);
}
