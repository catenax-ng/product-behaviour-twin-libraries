package net.catena_x.btp.sedc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.sedc.model.rawvalues.PowerClassesNormalized;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeakLoadRawValues {
    private long dataCollectionPeriodInMs = 0L;
    private PowerClassesNormalized powerClassesNormalized = null;
    private double averageEnvironmentTemperatureInC = 0.0;
    private double stateOfChargeAtStartNormalized = 0.0;
    private double stateOfChargeAtEndNormalized = 0.0;
}
