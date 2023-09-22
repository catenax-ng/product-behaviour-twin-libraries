package net.catena_x.btp.hi.oem.frontend.rest.controller.util;

import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicle;
import net.catena_x.btp.hi.oem.frontend.model.dto.statistics.HIHistogram;
import net.catena_x.btp.hi.oem.frontend.model.enums.HIFHealthState;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class HIHistogramGenerator {
    public HIHistogram generateHistogramHealthStatesLoadSpectra(@NotNull final List<HIFVehicle> vehicles ) {
        return generateHistogram(vehicles.stream().map(v->v.getHealthStateLoadSpectra()).toList());
    }

    public HIHistogram generateHistogramHealthStatesAdaptionValues(@NotNull final List<HIFVehicle> vehicles ) {
        return generateHistogram(vehicles.stream().map(v->v.getHealthStateAdaptionValues()).toList());
    }

    private HIHistogram generateHistogram(@NotNull final List<HIFHealthState> healthStates) {
        int[] counts = new int[4];

        for (final HIFHealthState healthState : healthStates) {
            ++counts[switch(healthState) {
                case GREEN -> 0;
                case YELLOW -> 1;
                case RED -> 2;
                case CALCULATION_PENDING -> 3;
            }];
        }

        return new HIHistogram(counts[0], counts[1], counts[2], counts[3]);
    }
}
