package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.TelematicsDataUpdater;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.VehicleRegistration;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.TestDataReader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.VehilceDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
public class RawdataProviderController {
    @Autowired VehilceDataLoader vehilceDataLoader;
    @Autowired TestDataReader testDataReader;
    @Autowired VehicleRegistration vehicleRegistration;
    @Autowired TelematicsDataUpdater telematicsDataUpdater;

    @Value("${services.dataprovider.testdata.file}")
    private String testDataFile;

    @Nullable private TestDataCategorized testData = null;

    @GetMapping("/api/rawdata/init/vehicles")
    public ResponseEntity<byte[]> initVehicles(@PathVariable @NotNull final InfoKey key) {
        try {
            vehicleRegistration.registerFromTestDataCetegorized(getTestData());
            return ok("Vehicles registered.");
        }
        catch(DataProviderException exception) {
            return failed(exception.toString());
        }
    }

    @GetMapping("/api/rawdata/init/telematicsdata")
    public ResponseEntity<byte[]> initTelematicsData(@PathVariable @NotNull final InfoKey key) {
        try {
            telematicsDataUpdater.updateFromTestDataCetegorized(getTestData());
            return ok("Telematics data initialized.");
        }
        catch(DataProviderException exception) {
            return failed(exception.toString());
        }
    }

    private ResponseEntity<byte[]> ok(@NotNull final String info) {
        return new ResponseEntity<>( String.format( "{\n  \"result\" : \"ok\",\n  \"info\" : \"%s\"\n}",
                info).getBytes(),
                new HttpHeaders(), HttpStatus.OK);
    }

    private ResponseEntity<byte[]> failed(@NotNull final String error) {
        return new ResponseEntity<>( String.format( "{\n  \"result\" : \"error\",\n  \"errorText\" : \"%s\"\n}",
                error).getBytes(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private TestDataCategorized getTestData() throws DataProviderException {
        if(testData == null) {
            testData.initFromTestData(testDataReader.loadFromFile(Path.of(testDataFile)));
        }

        return testData;
    }
}
