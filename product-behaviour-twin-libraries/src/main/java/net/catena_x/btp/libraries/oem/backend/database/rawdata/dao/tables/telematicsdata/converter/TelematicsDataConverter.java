package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.converter;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.model.TelematicsDataDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.TelematicsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class TelematicsDataConverter extends DAOConverter<TelematicsDataDAO, TelematicsData> {
    @Autowired LoadCollectivesConverter loadCollectivesConverter;
    @Autowired AdaptionValuesConverter adaptionValuesConverter;

    protected TelematicsData toDTOSourceExists(@NotNull final TelematicsDataDAO source) {
        return new TelematicsData(source.getId(), source.getStorageTimestamp(),
                source.getSyncCounter(), source.getVehicleId(),
                source.getCreationTimestamp(), source.getMileage(),source.getOperatingSeconds(),
                loadCollectivesConverter.toDTO(source.getLoadCollectives()),
                adaptionValuesConverter.toDTO(source.getAdaptionValues()));
    }

    protected TelematicsDataDAO toDAOSourceExists(@NotNull final TelematicsData source) {
        return new TelematicsDataDAO(source.getId(), source.getStorageTimestamp(),
                source.getSyncCounter(), source.getVehicleId(),
                source.getCreationTimestamp(), source.getMileage(),source.getOperatingSeconds(),
                loadCollectivesConverter.toDAO(source.getLoadCollectives()),
                adaptionValuesConverter.toDAO(source.getAdaptionValues()));
    }
}
