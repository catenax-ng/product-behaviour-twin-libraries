package net.catena_x.btp.hi.oem.common.database.hi.util;

import net.catena_x.btp.hi.oem.common.database.hi.tables.vehicle.HIVehicleDAO;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

@Component
public class VehicleComperator {
    public void assertEqual(@NotNull final HIVehicleDAO vehicle1, @NotNull final Vehicle vehicle2)
            throws OemHIException {
        try {
            Assert.notNull(vehicle1, "Vehicle 1 is null!");
            Assert.notNull(vehicle2, "Vehicle 2 is null!");

            Assert.notNull(vehicle1.getVehicleId(), "Id of vehicle 1 is null!");
            Assert.isTrue(vehicle1.getVehicleId().equals(vehicle2.getVehicleId()), "Vehicle ids differ!");

            Assert.notNull(vehicle1.getVan(), "Van of vehicle 1 is null!");
            Assert.isTrue(vehicle1.getVan().equals(vehicle2.getVan()), "Vehicle vans differ!");

            Assert.notNull(vehicle1.getGearboxId(), "Gearbox id of vehicle 1 is null!");
            Assert.isTrue(vehicle1.getGearboxId().equals(vehicle2.getGearboxId()),
                    "Vehicle Gearbox ids differ!");
        } catch (final Exception exception) {
            throw new OemHIException(exception.getMessage());
        }
    }
}
