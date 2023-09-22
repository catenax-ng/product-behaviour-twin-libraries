package net.catena_x.btp.hi.oem.common.model.dto.healthindicators;

import net.catena_x.btp.hi.oem.common.database.hi.tables.healthindicators.HIHealthIndicatorsDAO;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class HIHealthIndicatorsConverter extends DAOConverter<HIHealthIndicatorsDAO, HIHealthIndicators> {
    @Autowired private HIHealthIndicatorValuesConverter hiHealthIndicatorValuesConverter;

    protected HIHealthIndicators toDTOSourceExists(@NotNull final HIHealthIndicatorsDAO source) {
        return new HIHealthIndicators(source.getId(), source.getCalculationTimestamp(),
                source.getCalculationSyncCounter(), source.getVehicleId(), source.getGearboxId(),
                hiHealthIndicatorValuesConverter.toDTO(source.getValues()));
    }

    protected HIHealthIndicatorsDAO toDAOSourceExists(@NotNull final HIHealthIndicators source) {
        return new HIHealthIndicatorsDAO(source.getId(), source.getCalculationTimestamp(),
                source.getCalculationSyncCounter(), source.getVehicleId(), source.getGearboxId(),
                hiHealthIndicatorValuesConverter.toDAO(source.getValues()));
    }
}
