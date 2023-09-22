package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationFromSupplierContentDAO;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.HealthIndicatorOutputConverter;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class HINotificationFromSupplierContentConverter
        extends DAOConverter<HINotificationFromSupplierContentDAO, HINotificationFromSupplierContent> {
    @Autowired private HealthIndicatorOutputConverter healthIndicatorOutputConverter;

    protected HINotificationFromSupplierContent toDTOSourceExists(
            @NotNull final HINotificationFromSupplierContentDAO source) {

        return new HINotificationFromSupplierContent(
                source.getRequestRefId(),
                healthIndicatorOutputConverter.toDTO(source.getHealthIndicatorOutputs()));
    }

    protected HINotificationFromSupplierContentDAO toDAOSourceExists(
            @NotNull final HINotificationFromSupplierContent source) {

        return new HINotificationFromSupplierContentDAO(
                source.getRequestRefId(),
                healthIndicatorOutputConverter.toDAO(source.getHealthIndicatorOutputs())
        );
    }
}
