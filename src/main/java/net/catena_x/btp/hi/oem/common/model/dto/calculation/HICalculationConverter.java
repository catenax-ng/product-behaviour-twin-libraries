package net.catena_x.btp.hi.oem.common.model.dto.calculation;

import net.catena_x.btp.hi.oem.common.database.hi.tables.calculation.HICalculationDAO;
import net.catena_x.btp.hi.oem.common.model.enums.HICalculationStatus;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class HICalculationConverter extends DAOConverter<HICalculationDAO, HICalculation> {
    protected HICalculation toDTOSourceExists(@NotNull final HICalculationDAO source) {
        return new HICalculation(source.getId(), source.getCalculationTimestamp(),
                source.getCalculationSyncCounterMin(), source.getCalculationSyncCounterMax(),
                HICalculationStatus.valueOf(source.getStatus()), source.getMessage());
    }

    protected HICalculationDAO toDAOSourceExists(@NotNull final HICalculation source) {
        return new HICalculationDAO(source.getId(), source.getCalculationTimestamp(),
                source.getCalculationSyncCounterMin(), source.getCalculationSyncCounterMax(),
                source.getStatus().toString(), source.getMessage());
    }
}
