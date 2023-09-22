package net.catena_x.btp.hi.oem.frontend.model.dao.vehicle;

import net.catena_x.btp.hi.oem.common.model.dto.healthindicators.HIHealthIndicators;
import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicle;
import net.catena_x.btp.hi.oem.frontend.model.enums.HIFHealthState;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.UncheckedIOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class HIFVehicleConverter extends DAOConverter<HIFVehicle, HIVehicle> {
    @Value("${hifrontend.adaptionsvalues.thresholds.greenYellow:0.8}") private double thresholdGreenYellow;
    @Value("${hifrontend.adaptionsvalues.thresholds.yellowRed:1.0}") private double thresholdYellowRed;

    private DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.from(ZoneOffset.UTC));

    protected HIVehicle toDTOSourceExists(@NotNull final HIFVehicle source) {
        throw new UncheckedIOException("Conversion is not allowed!", null);
    }

    protected HIFVehicle toDAOSourceExists(@NotNull final HIVehicle source) {
        return new HIFVehicle(source.getVehicleId(), source.getVan(),
                source.getGearboxId(), dateFormatter.format(source.getProductionDate()),
                dateFormatter.format(source.getUpdateTimestamp()),
                getHealthState(source.getNewestHealthindicators(), 0),
                getHealthState(source.getNewestHealthindicators(), 1));
    }

    private HIFHealthState getHealthState(@Nullable final HIHealthIndicators healthIndicators,
                                          @Nullable final int index) {

        if(healthIndicators == null) {
            return HIFHealthState.CALCULATION_PENDING;
        }

        if(healthIndicators.getValues()[index] < thresholdGreenYellow) {
            return HIFHealthState.GREEN;
        }
        else if(healthIndicators.getValues()[index] < thresholdYellowRed) {
            return HIFHealthState.YELLOW;
        }
        else {
            return HIFHealthState.RED;
        }
    }
}
