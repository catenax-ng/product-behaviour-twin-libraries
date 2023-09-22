package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthIndicatorOutput {
    private String version;
    private String componentId;
    private double[] healthIndicatorValues;
}