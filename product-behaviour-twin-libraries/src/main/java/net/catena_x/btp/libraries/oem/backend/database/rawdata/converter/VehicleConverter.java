package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.springframework.stereotype.Component;

@Component
public class VehicleConverter {
    public void convert(VehicleInfo source, Vehicle destination) {
        destination.setId(source.id());
        destination.setVan(source.van());
        destination.setProductionDate(source.productionDate());
        destination.setTelemetricsData(null);
        destination.setUpdateTimestamp(null);
    }
}
