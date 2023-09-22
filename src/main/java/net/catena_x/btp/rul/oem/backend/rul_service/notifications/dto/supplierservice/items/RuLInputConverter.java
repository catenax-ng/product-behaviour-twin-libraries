package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.items.RuLInputDAO;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLInputConverter extends DAOConverter<RuLInputDAO, RuLInput> {

    protected RuLInput toDTOSourceExists(@NotNull final RuLInputDAO source) {
        return new RuLInput(source.getComponentId(), source.getClassifiedLoadSpectrumGearSet(),
                source.getClassifiedLoadSpectrumGearOil());
    }

    protected RuLInputDAO toDAOSourceExists(@NotNull final RuLInput source) {
        return new RuLInputDAO(source.getComponentId(), source.getClassifiedLoadSpectrumGearSet(),
                source.getClassifiedLoadSpectrumGearOil());
    }
}
