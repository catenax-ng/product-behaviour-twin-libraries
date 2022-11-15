package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import com.fasterxml.jackson.core.type.TypeReference;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOJsonConverter;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.LoadSpectraConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InputLoadSpectraConverter extends DAOJsonConverter<List<ClassifiedLoadSpectrum>> {
    public InputLoadSpectraConverter() {
        super(new TypeReference<List<ClassifiedLoadSpectrum>>(){});
    }
}
