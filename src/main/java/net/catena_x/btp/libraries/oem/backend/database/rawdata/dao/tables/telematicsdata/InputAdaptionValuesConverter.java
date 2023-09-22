package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import com.fasterxml.jackson.core.type.TypeReference;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.util.database.converter.DAOJsonConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InputAdaptionValuesConverter extends DAOJsonConverter<List<AdaptionValues>> {
    public InputAdaptionValuesConverter() {
        super(new TypeReference<List<AdaptionValues>>(){});
    }
}
