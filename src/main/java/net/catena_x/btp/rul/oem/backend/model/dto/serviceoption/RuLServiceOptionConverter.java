package net.catena_x.btp.rul.oem.backend.model.dto.serviceoption;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.database.rul.tables.serviceoption.RuLServiceOptionDAO;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLServiceOptionType;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLServiceOptionConverter extends DAOConverter<RuLServiceOptionDAO, RuLServiceOption> {
    protected RuLServiceOption toDTOSourceExists(@NotNull final RuLServiceOptionDAO source) {
        return new RuLServiceOption(RuLServiceOptionType.valueOf(source.getKey()), source.getValue());
    }

    protected RuLServiceOptionDAO toDAOSourceExists(@NotNull final RuLServiceOption source) {
        return new RuLServiceOptionDAO(source.getKey().toString(), source.getValue());
    }
}
