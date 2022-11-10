package net.catena_x.btp.libraries.oem.backend.model.dto.vehicle;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.VehicleDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle.VehicleWithTelematicsDataDAO;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsData;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsDataConverter;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VehicleConverter extends DAOConverter<VehicleDAO, Vehicle> {
    @Autowired private TelematicsDataConverter telematicsDataConverter;

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

    public VehicleWithTelematicsDataDAO toDAOWithNewestTelemeticsData(@Nullable final Vehicle source) {
        if(source == null) {
            return null;
        }

        final VehicleDAO converted = toDAO(source);
        final TelematicsData telematicsData = source.getNewestTelematicsData();

        if(telematicsData == null) {
            return new VehicleWithTelematicsDataDAO(converted, null);
        }

        converted.setNewestTelematicsId(telematicsData.getId());
        return new VehicleWithTelematicsDataDAO(converted, telematicsDataConverter.toDAO(telematicsData));
    }

    public List<VehicleWithTelematicsDataDAO> toDAOWithNewestTelemeticsData(@Nullable final List<Vehicle> source) {
        if(source == null) {
            return null;
        }

        return source.stream().map((vehicle -> toDAOWithNewestTelemeticsData(vehicle))).collect(Collectors.toList());
    }

    public Vehicle toDTOWithTelematicsData(@Nullable final VehicleWithTelematicsDataDAO source) {
        if(source == null) {
            return null;
        }

        final Vehicle converted = toDTO(source.vehicle());
        if(source.telematicsData() != null) {
            converted.setNewestTelematicsData(telematicsDataConverter.toDTO(source.telematicsData()));
        }

        return converted;
    }

    public List<Vehicle> toDTOWithTelematicsData(@Nullable final List<VehicleWithTelematicsDataDAO> source) {
        if(source == null) {
            return null;
        }

        return source.stream().map((vehicle -> toDTOWithTelematicsData(vehicle))).collect(Collectors.toList());
    }
}
