package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdaptionValuesList {
    private String version;
    private Instant timeStamp;
    private double mileageInKm;
    private long operatingTimeInSeconds;
    private double[] values;
}
