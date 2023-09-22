package net.catena_x.btp.hi.oem.backend.hi_service.collector.util;

import net.catena_x.btp.hi.oem.backend.hi_service.collector.HIDataCollector;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.AdaptionValuesList;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class HIAdaptionValueInputConverter {
    public AdaptionValuesList convert(@NotNull final AdaptionValues adaptionValues) {
        float operationHours = (adaptionValues.getStatus().getOperatingTime() == null) ?
                                        adaptionValues.getStatus().getOperatingHours() :
                                        Float.parseFloat(adaptionValues.getStatus().getOperatingTime());

        return new AdaptionValuesList(HIDataCollector.DATA_VERSION, adaptionValues.getStatus().getDate(),
                adaptionValues.getStatus().getMileage(), (long)(operationHours * 3600.0f), adaptionValues.getValues());
    }
}
