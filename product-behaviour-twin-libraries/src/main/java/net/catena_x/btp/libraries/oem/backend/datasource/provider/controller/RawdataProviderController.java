package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResult;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.DatabaseReset;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.TelematicsDataUpdater;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.VehicleRegistration;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataReader;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.VehilceDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/rawdata")
public class RawdataProviderController {
    @Autowired private VehilceDataLoader vehilceDataLoader;
    @Autowired private TestDataReader testDataReader;
    @Autowired private VehicleRegistration vehicleRegistration;
    @Autowired private TelematicsDataUpdater telematicsDataUpdater;
    @Autowired private DatabaseReset databaseReset;
    @Autowired private ApiHelper apiHelper;
    @Autowired private TestDataCategorized testData;

    @Value("${services.dataprovider.testdata.file}")
    private String testDataFile;

    @GetMapping("/reset")
    public ResponseEntity<ApiResult> reset() {
        try {
            databaseReset.reset();
            return apiHelper.ok("Database reinitialized.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @GetMapping("/init/vehicles")
    public ResponseEntity<ApiResult> initVehicles() {
        try {
            vehicleRegistration.registerFromTestDataCetegorized(getTestData());
            return apiHelper.ok("Vehicles registered.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @GetMapping("/init/telematicsdata")
    public ResponseEntity<ApiResult> initTelematicsData() {
        try {
            telematicsDataUpdater.updateFromTestDataCetegorized(getTestData());
            return apiHelper.ok("Telematics data initialized.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    private synchronized TestDataCategorized getTestData() throws DataProviderException {
        if(!testData.isInitialized()) {
            testData.initFromTestData(testDataReader.loadFromFile(Path.of(testDataFile)));
        }

        return testData;
    }
}
