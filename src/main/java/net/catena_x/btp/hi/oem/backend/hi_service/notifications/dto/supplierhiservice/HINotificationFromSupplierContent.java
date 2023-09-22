package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.HealthIndicatorOutput;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HINotificationFromSupplierContent {
    private String requestRefId;
    private List<HealthIndicatorOutput> healthIndicatorOutputs;
}
