package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationToSupplierContentDAO;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.HealthIndicatorInputConverter;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class HINotificationToSupplierContentConverter
        extends DAOConverter<HINotificationToSupplierContentDAO, HIDataToSupplierContent> {

    @Autowired private HealthIndicatorInputConverter healthIndicatorInputConverter;

    protected HIDataToSupplierContent toDTOSourceExists(
            @NotNull final HINotificationToSupplierContentDAO source) {

        return new HIDataToSupplierContent(
                source.getRequestRefId(),
                healthIndicatorInputConverter.toDTO(source.getHealthIndicatorInputs()));
    }

    protected HINotificationToSupplierContentDAO toDAOSourceExists(
            @NotNull final HIDataToSupplierContent source) {

        return new HINotificationToSupplierContentDAO(
                source.getRequestRefId(),
                healthIndicatorInputConverter.toDAO(source.getHealthIndicatorInputs()));
    }
}
