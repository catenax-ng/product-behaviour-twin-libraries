package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthIndicatorInput {
    private String componentId;
    private ClassifiedLoadSpectrum classifiedLoadSpectrum;
    private AdaptionValuesList adaptionValues;
}
