package net.catena_x.btp.hi.oem.frontend.rest.controller.base;

import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicle;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public class HIFrontendControllerVehicleBase {
    protected ResponseEntity<String> checkVehicle(@Nullable final HIFVehicle vehicle,
                                                  @NotNull final ApiHelper apiHelper) {
        if(vehicle == null) {
            return apiHelper.failedAsString("Vehicle not found!");
        }

        return apiHelper.okAsString(vehicle, HIFVehicle.class);
    }
}
