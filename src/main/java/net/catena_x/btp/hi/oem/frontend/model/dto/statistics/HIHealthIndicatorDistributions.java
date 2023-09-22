package net.catena_x.btp.hi.oem.frontend.model.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIHealthIndicatorDistributions {
    private HIHistogram histogramLoadSpectra;
    private HIHistogram histogramAdaptionValues;
}
