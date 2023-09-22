package net.catena_x.btp.hi.oem.common.model.dto.vehicle;

import net.catena_x.btp.hi.oem.common.database.hi.tables.vehicle.HIVehicleDAO;
import net.catena_x.btp.hi.oem.common.database.hi.tables.vehicle.HIVehicleWithHealthIndicatorsDAO;
import net.catena_x.btp.hi.oem.common.model.dto.healthindicators.HIHealthIndicators;
import net.catena_x.btp.hi.oem.common.model.dto.healthindicators.HIHealthIndicatorsConverter;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HIVehicleConverter extends DAOConverter<HIVehicleDAO, HIVehicle> {
    @Autowired private HIHealthIndicatorsConverter hiHealthIndicatorsConverter;

    protected HIVehicle toDTOSourceExists(@NotNull final HIVehicleDAO source) {
        return new HIVehicle(source.getVehicleId(), source.getVan(),
                source.getGearboxId(), source.getProductionDate(),
                source.getUpdateTimestamp(), null);
    }

    protected HIVehicleDAO toDAOSourceExists(@NotNull final HIVehicle source) {
        return new HIVehicleDAO(source.getVehicleId(), source.getVan(),
                source.getGearboxId(), source.getProductionDate(),
                source.getUpdateTimestamp(), null);
    }

    public HIVehicleDAO toDAOWithNewestHealthIndicatorsId(@Nullable final HIVehicle source,
                                                          @Nullable final String newestHealthIndicatorsId) {
        if(source == null) {
            return null;
        }

        final HIVehicleDAO converted = toDAO(source);
        converted.setNewestHealthindicatorsId(newestHealthIndicatorsId);
        return converted;
    }

    public HIVehicleWithHealthIndicatorsDAO toDAOWithNewestHealthIndicators(@Nullable final HIVehicle source) {
        if(source == null) {
            return null;
        }

        final HIVehicleDAO converted = toDAO(source);
        final HIHealthIndicators healthIndicators = source.getNewestHealthindicators();

        if(healthIndicators == null) {
            return new HIVehicleWithHealthIndicatorsDAO(converted, null);
        }

        converted.setNewestHealthindicatorsId(healthIndicators.getId());
        return new HIVehicleWithHealthIndicatorsDAO(converted,
                hiHealthIndicatorsConverter.toDAO(healthIndicators));
    }

    public List<HIVehicleWithHealthIndicatorsDAO> toDAOWithNewestHealthIndicators(
            @Nullable final List<HIVehicle> source) {
        if(source == null) {
            return null;
        }

        return source.stream().map((vehicle -> toDAOWithNewestHealthIndicators(vehicle))).collect(Collectors.toList());
    }

    public HIVehicle toDTOWithHealthIndicators(@Nullable final HIVehicleWithHealthIndicatorsDAO source) {
        if(source == null) {
            return null;
        }

        final HIVehicle converted = toDTO(source.vehicle());
        if(source.healthIndicators() != null) {
            converted.setNewestHealthindicators(hiHealthIndicatorsConverter.toDTO(source.healthIndicators()));
        }

        return converted;
    }

    public List<HIVehicle> toDTOWithHealthIndicators(@Nullable final List<HIVehicleWithHealthIndicatorsDAO> source) {
        if(source == null) {
            return null;
        }

        return source.stream().map((vehicle -> toDTOWithHealthIndicators(vehicle))).collect(Collectors.toList());
    }
}