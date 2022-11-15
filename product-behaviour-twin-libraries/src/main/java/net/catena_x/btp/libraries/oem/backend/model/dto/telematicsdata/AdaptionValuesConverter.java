package net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata;

import com.fasterxml.jackson.core.type.TypeReference;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOJsonConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdaptionValuesConverter extends DAOJsonConverter<List<AdaptionValues>> {
    public AdaptionValuesConverter() {
        super(new TypeReference<List<AdaptionValues>>(){});
    }
}
