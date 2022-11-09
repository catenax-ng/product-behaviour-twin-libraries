package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.converter;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.model.VehicleDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.TelematicsData;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.Vehicle;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class VehicleConverter extends DAOConverter<VehicleDAO, Vehicle> {

    protected Vehicle toDTOSourceExists(@NotNull final VehicleDAO source) {
        return new Vehicle(source.getVehicleId(), source.getVan(),
                source.getGearboxId(), source.getProductionDate(),
                source.getUpdateTimestamp(), source.getSyncCounter(), null);
    }

    protected VehicleDAO toDAOSourceExists(@NotNull final Vehicle source) {
        return new VehicleDAO(source.getVehicleId(), source.getVan(),
                source.getGearboxId(), source.getProductionDate(),
                source.getUpdateTimestamp(), source.getSyncCounter(), null);
    }

    public VehicleDAO toDAOWithNewestTelemeticsId(@Nullable final Vehicle source,
                                                  @Nullable final String newestTelemeticsId) {
        if(source == null) {
            return null;
        }

        final VehicleDAO converted = toDAO(source);
        converted.setNewestTelematicsId(newestTelemeticsId);
        return converted;
    }

    public Vehicle toDTOWithTelematicsData(@Nullable final VehicleDAO source,
                                           @Nullable final TelematicsData newestTelematicsData) {
        if(source == null) {
            return null;
        }

        final Vehicle converted = toDTO(source);
        if(newestTelematicsData != null) {
            converted.setNewestTelematicsData(newestTelematicsData);
        }

        return converted;
    }
}
