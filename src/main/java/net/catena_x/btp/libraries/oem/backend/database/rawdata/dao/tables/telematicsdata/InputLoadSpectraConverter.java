package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import com.fasterxml.jackson.core.type.TypeReference;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.util.database.converter.DAOJsonConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InputLoadSpectraConverter extends DAOJsonConverter<List<ClassifiedLoadSpectrum>> {
    public InputLoadSpectraConverter() {
        super(new TypeReference<List<ClassifiedLoadSpectrum>>(){});
    }
}
