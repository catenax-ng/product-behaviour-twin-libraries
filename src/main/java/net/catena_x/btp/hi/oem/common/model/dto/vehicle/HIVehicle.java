package net.catena_x.btp.hi.oem.common.model.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.hi.oem.common.model.dto.healthindicators.HIHealthIndicators;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIVehicle {
    private String vehicleId;
    private String van;
    private String gearboxId;
    private Instant productionDate;
    private Instant updateTimestamp;
    private HIHealthIndicators newestHealthindicators;
}

