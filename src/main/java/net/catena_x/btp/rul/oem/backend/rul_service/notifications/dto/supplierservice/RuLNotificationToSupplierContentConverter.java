package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.RuLNotificationToSupplierContentDAO;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items.RuLInputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLNotificationToSupplierContentConverter
        extends DAOConverter<RuLNotificationToSupplierContentDAO, RuLDataToSupplierContent> {
    @Autowired private RuLInputConverter rulInputConverter;

    protected RuLDataToSupplierContent toDTOSourceExists(
            @NotNull final RuLNotificationToSupplierContentDAO source) {

        return new RuLDataToSupplierContent(
                source.getRequestRefId(),
                rulInputConverter.toDTO(source.getEndurancePredictorInputs()));
    }

    protected RuLNotificationToSupplierContentDAO toDAOSourceExists(
            @NotNull final RuLDataToSupplierContent source) {

        return new RuLNotificationToSupplierContentDAO(
                source.getRequestRefId(),
                rulInputConverter.toDAO(source.getEndurancePredictorInputs())
        );
    }
}
