package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.items.HealthIndicatorOutputDAO;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class HealthIndicatorOutputConverter
        extends DAOConverter<HealthIndicatorOutputDAO, HealthIndicatorOutput> {

    protected HealthIndicatorOutput toDTOSourceExists(
            @NotNull final HealthIndicatorOutputDAO source) {

        return new HealthIndicatorOutput(
                source.getVersion(),
                source.getComponentId(),
                source.getHealthIndicatorValues());
    }

    protected HealthIndicatorOutputDAO toDAOSourceExists(
            @NotNull final HealthIndicatorOutput source) {

        return new HealthIndicatorOutputDAO(
                source.getVersion(),
                source.getComponentId(),
                source.getHealthIndicatorValues());
    }
}
