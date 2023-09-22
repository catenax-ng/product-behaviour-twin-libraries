package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.items.HealthIndicatorInputDAO;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class HealthIndicatorInputConverter
        extends DAOConverter<HealthIndicatorInputDAO, HealthIndicatorInput> {
    @Autowired AdaptionValuesListConverter adaptionValuesListConverter;

    protected HealthIndicatorInput toDTOSourceExists(
            @NotNull final HealthIndicatorInputDAO source) {

        return new HealthIndicatorInput(
                source.getComponentId(),
                source.getClassifiedLoadSpectrum(),
                adaptionValuesListConverter.toDTO(source.getAdaptionValuesList()));
    }

    protected HealthIndicatorInputDAO toDAOSourceExists(
            @NotNull final HealthIndicatorInput source) {

        return new HealthIndicatorInputDAO(
                source.getComponentId(),
                source.getClassifiedLoadSpectrum(),
                adaptionValuesListConverter.toDAO(source.getAdaptionValues()));
    }
}
