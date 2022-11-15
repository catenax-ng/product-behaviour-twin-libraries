package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOConverter;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class InputTelematicsDataConverter extends DAOConverter<TelematicsDataDAO, InputTelematicsData> {
    @Autowired InputLoadSpectraConverter loadSpectraConverter;
    @Autowired InputAdaptionValuesConverter adaptionValuesConverter;

    protected InputTelematicsData toDTOSourceExists(@NotNull final TelematicsDataDAO source) {
        return new InputTelematicsData(
                source.getVehicleId(),
                loadSpectraConverter.toDTO(source.getLoadSpectra()),
                adaptionValuesConverter.toDTO(source.getAdaptionValues()));
    }

    protected TelematicsDataDAO toDAOSourceExists(@NotNull final InputTelematicsData source) {
        return new TelematicsDataDAO(null, null, 0, source.vehicleId(),
                loadSpectraConverter.toDAO(source.loadSpectra()),
                adaptionValuesConverter.toDAO(source.adaptionValues()));
    }

    public TelematicsDataDAO toDAOWithId(@NotNull final String id, @Nullable final InputTelematicsData source) {
        if(source == null) {
            return null;
        }

        final TelematicsDataDAO converted = this.toDAO(source);
        converted.setId(id);
        return converted;
    }
}
