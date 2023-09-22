package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.RuLNotificationFromSupplierContentDAO;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items.RuLOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLNotificationFromSupplierContentConverter
        extends DAOConverter<RuLNotificationFromSupplierContentDAO, RuLNotificationFromSupplierContent> {
    @Autowired private RuLOutputConverter rulOutputConverter;

    protected RuLNotificationFromSupplierContent toDTOSourceExists(
            @NotNull final RuLNotificationFromSupplierContentDAO source) {

        return new RuLNotificationFromSupplierContent(
                source.getRequestRefId(), source.getComponentType(),
                rulOutputConverter.toDTO(source.getEndurancePredictorOutputs()));
    }

    protected RuLNotificationFromSupplierContentDAO toDAOSourceExists(
            @NotNull final RuLNotificationFromSupplierContent source) {

        return new RuLNotificationFromSupplierContentDAO(
                source.getRequestRefId(), source.getComponentType(),
                rulOutputConverter.toDAO(source.getEndurancePredictorOutputs())
        );
    }
}
