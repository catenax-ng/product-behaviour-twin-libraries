package net.catena_x.btp.hi.oem.common.model.dto.healthindicators;

import com.fasterxml.jackson.core.type.TypeReference;
import net.catena_x.btp.libraries.util.database.converter.DAOJsonConverter;
import org.springframework.stereotype.Component;

@Component
public class HIHealthIndicatorValuesConverter extends DAOJsonConverter<double[]> {
    public HIHealthIndicatorValuesConverter() {
        super(new TypeReference<double[]>(){});
    }
}
