package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.items.RuLOutputDAO;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLOutputConverter extends DAOConverter<RuLOutputDAO, RuLOutput> {

    protected RuLOutput toDTOSourceExists(@NotNull final RuLOutputDAO source) {
        return new RuLOutput(source.getComponentId(), source.getComponentType(), source.getRemainingUsefulLife());
    }

    protected RuLOutputDAO toDAOSourceExists(@NotNull final RuLOutput source) {
        return new RuLOutputDAO(source.getComponentId(), source.getComponentType(), source.getRemainingUsefulLife());
    }
}
