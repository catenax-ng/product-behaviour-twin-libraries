package net.catena_x.btp.hi.oem.common.model.dto.calculation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.hi.oem.common.model.enums.HICalculationStatus;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HICalculation {
    private String id;
    private Instant calculationTimestamp;
    private long calculationSyncCounterMin;
    private long calculationSyncCounterMax;
    private HICalculationStatus status;
    private String message;
}
