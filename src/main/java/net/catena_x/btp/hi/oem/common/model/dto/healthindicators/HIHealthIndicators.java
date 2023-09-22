package net.catena_x.btp.hi.oem.common.model.dto.healthindicators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIHealthIndicators {
    private String id;
    private Instant calculationTimestamp;
    private long calculationSyncCounter;
    private String vehicleId;
    private String gearboxId;
    private double[] values;
}
