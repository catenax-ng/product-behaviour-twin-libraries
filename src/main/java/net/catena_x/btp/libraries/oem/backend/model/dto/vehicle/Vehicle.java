package net.catena_x.btp.libraries.oem.backend.model.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsData;

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
