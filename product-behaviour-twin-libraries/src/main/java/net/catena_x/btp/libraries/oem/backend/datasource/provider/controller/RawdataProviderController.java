package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResult;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataExporter;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.DatabaseReset;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.TelematicsDataUpdater;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.VehicleRegistration;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataReader;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.VehilceDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/rawdata")
public class RawdataProviderController {
    @Autowired private VehilceDataLoader vehilceDataLoader;
    @Autowired private TestDataReader testDataReader;
    @Autowired private VehicleRegistration vehicleRegistration;
    @Autowired private TelematicsDataUpdater telematicsDataUpdater;
    @Autowired private DatabaseReset databaseReset;
    @Autowired private ApiHelper apiHelper;
    @Autowired private TestDataCategorized testDataCategorized;
    @Autowired private TestDataExporter testDataExporter;

    private TestData testData = null;
    private DateTimeFormatter exportDataFormatter =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneId.from(ZoneOffset.UTC));

    @Value("${services.dataprovider.testdata.file}")
    private String testDataFile;

    @Value("${services.dataprovider.testdata.clutchSpectrumGreen:#{null}}")
    private String clutchSpectrumGreen;

    @Value("${services.dataprovider.testdata.clutchSpectrumYellow:#{null}}")
    private String clutchSpectrumYellow;

    @Value("${services.dataprovider.testdata.clutchSpectrumRed:#{null}}")
    private String clutchSpectrumRed;

    @Value("${services.dataprovider.testdata.exportPath:#{null}}")
    private String testdataExportPath;

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
            vehicleRegistration.registerFromTestDataCetegorized(getTestDataCategorized(null));
            return apiHelper.ok("Vehicles registered.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @GetMapping("/init/telematicsdata")
    public ResponseEntity<ApiResult> initTelematicsData() {
        try {
            telematicsDataUpdater.updateFromTestDataCetegorized(getTestDataCategorized(null));
            return apiHelper.ok("Telematics data initialized.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @GetMapping("/export/testdata")
    public ResponseEntity<ApiResult> exportTestdata() {
        try {
            if(testdataExportPath==null) {
                throw new DataProviderException("Export path not set!");
            }

            testDataExporter.export(getTestData(), Path.of(
                    testdataExportPath, "testdata_export_"
                            + exportDataFormatter.format(Instant.now()) + ".json" ));
            return apiHelper.ok("Test data exported.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @PostMapping("/init/reinitbyfile")
    public ResponseEntity<ApiResult> reinitByFile(@RequestParam(value = "testdata", required = false)
                                                  @Nullable final MultipartFile testDataFileParam,
                                                  @RequestBody(required = false)
                                                  @Nullable final byte[] testDataFileBody) {

        try {
            if (testDataFileParam != null) {
                return reinitByJsonFile(new String(testDataFileParam.getBytes(), StandardCharsets.UTF_8));
            } else if (testDataFileBody != null) {
                return reinitByJsonFile(new String(testDataFileBody, StandardCharsets.UTF_8));
            } else {
                throw new DataProviderException("No JSON file given!");
            }
        } catch (Exception exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    private synchronized ResponseEntity<ApiResult> reinitByJsonFile(@NotNull final String testDataJson)
            throws DataProviderException {

        getTestDataCategorized(testDataJson);

        return apiHelper.ok("Testdata reinitialized.");
    }

    private TestData getTestData() throws DataProviderException {
        getTestDataCategorized(null);
        return testData;
    }

    private synchronized TestDataCategorized getTestDataCategorized(@Nullable final String testDataJson)
            throws DataProviderException {

        if(testDataJson != null) {
            testDataCategorized.reset();
            testData = testDataReader.loadFromJson(testDataJson);
            testDataCategorized.initFromTestData(testData);
            return testDataCategorized;
        }

        if(!testDataCategorized.isInitialized()) {
            testData = testDataReader.loadFromFiles(Path.of(testDataFile),
                    Path.of(clutchSpectrumGreen), Path.of(clutchSpectrumYellow), Path.of(clutchSpectrumRed));

            testDataCategorized.initFromTestData(testData);
        }

        return testDataCategorized;
    }
}
