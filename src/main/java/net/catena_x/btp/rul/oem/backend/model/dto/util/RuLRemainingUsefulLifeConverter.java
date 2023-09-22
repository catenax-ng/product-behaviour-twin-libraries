package net.catena_x.btp.rul.oem.backend.model.dto.util;

import com.fasterxml.jackson.core.type.TypeReference;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;
import net.catena_x.btp.libraries.util.database.converter.DAOJsonConverter;
import org.springframework.stereotype.Component;

@Component
public class RuLRemainingUsefulLifeConverter extends DAOJsonConverter<RemainingUsefulLife> {
    public RuLRemainingUsefulLifeConverter() {
        super(new TypeReference<RemainingUsefulLife>(){});
    }
}