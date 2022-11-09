package net.catena_x.btp.libraries.oem.backend.database.rawdata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private String vehicleId;
    private String van;
    private String gearboxId;
    private Instant productionDate;
    private Instant updateTimestamp;
    private long syncCounter;
    private TelematicsData newestTelematicsData;
}
