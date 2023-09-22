package net.catena_x.btp.rul.oem.backend.model.dto.calculation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;
import net.catena_x.btp.libraries.edc.model.EdcAssetAddress;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLCalculationStatus;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLCalculation {
    private String id;
    private String requesterNotificationId;
    private Instant calculationTimestamp;
    private RuLCalculationStatus status;
    private RemainingUsefulLife rul;
    private EdcAssetAddress requesterResultAddress;
}
