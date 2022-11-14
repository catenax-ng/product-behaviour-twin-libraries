package net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOJsonConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoadSpectraConverter extends DAOJsonConverter<List<String>> {
    public LoadSpectraConverter() {
        super(ArrayList.class);
    }
}
