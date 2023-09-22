package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthIndicatorInputDAO {
    private String componentId;
    private ClassifiedLoadSpectrum classifiedLoadSpectrum;

    @JsonProperty("adaptionValueList")
    private AdaptionValuesListDAO adaptionValuesList;
}
