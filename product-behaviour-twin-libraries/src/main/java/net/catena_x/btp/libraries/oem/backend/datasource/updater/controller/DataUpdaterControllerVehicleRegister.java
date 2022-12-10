package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(DataUpdaterApiConfig.API_PATH_BASE)
public class DataUpdaterControllerVehicleRegister {
    @Autowired private ApiHelper apiHelper;
    @Autowired private VehicleTable vehicleTable;

    @PostMapping(value = "/vehicle/register", produces = "application/json")
    public ResponseEntity<DefaultApiResult> registerVehicle(@RequestBody @NotNull final VehicleInfo vehicle) {
        try {
            vehicleTable.registerVehicleNewTransaction(vehicle);
            return apiHelper.ok("Vehicle registered.");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }
}
