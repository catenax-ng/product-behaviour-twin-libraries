package net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.TelematicsDataDAO;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class TelematicsDataConverter extends DAOConverter<TelematicsDataDAO, TelematicsData> {
    @Autowired private LoadSpectraConverter loadSpectraConverter;
    @Autowired private AdaptionValuesConverter adaptionValuesConverter;

    protected TelematicsData toDTOSourceExists(@NotNull final TelematicsDataDAO source) {
        return new TelematicsData(source.getId(), source.getStorageTimestamp(),
                source.getSyncCounter(), source.getVehicleId(),
                loadSpectraConverter.toDTO(source.getLoadSpectra()),
                adaptionValuesConverter.toDTO(source.getAdaptionValues()));
    }

    protected TelematicsDataDAO toDAOSourceExists(@NotNull final TelematicsData source) {
        return new TelematicsDataDAO(source.getId(), source.getStorageTimestamp(),
                source.getSyncCounter(), source.getVehicleId(),
                loadSpectraConverter.toDAO(source.getLoadSpectra()),
                adaptionValuesConverter.toDAO(source.getAdaptionValues()));
    }
}
