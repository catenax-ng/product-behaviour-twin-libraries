package net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIFHealthIndicatorDistributions {
    private HIFHistogram histogramLoadSpectra;
    private HIFHistogram histogramAdaptionValues;
}
