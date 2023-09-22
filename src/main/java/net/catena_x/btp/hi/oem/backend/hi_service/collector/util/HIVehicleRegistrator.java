package net.catena_x.btp.hi.oem.backend.hi_service.collector.util;

import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicleTable;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class HIVehicleRegistrator {
    @Autowired private HIVehicleTable hiVehicleTable;

    public void registerNewVehicles(@NotNull final List<Vehicle> vehicles) throws OemHIException {
        for (final Vehicle vehicle : vehicles) {
            hiVehicleTable.insertIfNewNewTransaction(vehicle);
        }
    }
}
