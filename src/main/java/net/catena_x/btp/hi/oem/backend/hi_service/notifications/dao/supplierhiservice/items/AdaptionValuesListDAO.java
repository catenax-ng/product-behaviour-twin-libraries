package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdaptionValuesListDAO {
    private String version;

    @JsonProperty("timestamp")
    private Instant timeStamp;

    @JsonProperty("mileage_km")
    private double mileageInKm;

    @JsonProperty("operatingtime_s")
    private long operatingTimeInSeconds;

    private double[] values;
}
