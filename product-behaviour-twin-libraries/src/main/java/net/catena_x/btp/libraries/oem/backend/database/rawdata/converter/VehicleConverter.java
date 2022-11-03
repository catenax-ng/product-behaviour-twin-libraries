package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.springframework.stereotype.Component;

@Component
public class VehicleConverter {
    public Vehicle convert(VehicleInfo source) {
        Vehicle destination = new Vehicle();
        convertMembers(destination, source);
        return destination;
    }

    private static void convertMembers(Vehicle destination, VehicleInfo source) {
        destination.setId(source.id());
        destination.setVan(source.van());
        destination.setGearboxId(source.gearboxId());
        destination.setProductionDate(source.productionDate());
        destination.setTelemetricsData(null);
        destination.setUpdateTimestamp(null);
    }
}
