package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.VehicleRegistration;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE)
public class DataProviderControllerInitVehicles {
    @Autowired private ApiHelper apiHelper;
    @Autowired private VehicleRegistration vehicleRegistration;
    @Autowired private TestDataManager testDataManager;

    @GetMapping(value = "/init/vehicles", produces = "application/json")
    public ResponseEntity<DefaultApiResult> initVehicles() {
        try {
            testDataManager.lock();
            vehicleRegistration.registerFromTestDataCetegorized(testDataManager.getTestDataCategorized(
                    null, true));
            return apiHelper.ok("Vehicles registered.");
        }
        catch(final DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataManager.unlock();
        }
    }
}
