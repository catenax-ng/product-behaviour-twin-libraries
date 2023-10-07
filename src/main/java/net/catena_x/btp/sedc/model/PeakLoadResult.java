package net.catena_x.btp.sedc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.sedc.model.result.PeakLoadCapability;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeakLoadResult {
    private long peakLoadCapability = PeakLoadCapability.UNKNOWN;
}
