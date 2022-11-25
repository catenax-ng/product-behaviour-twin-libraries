package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResult;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataExporter;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.DatabaseReset;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.TelematicsDataUpdater;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.VehicleRegistration;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataReader;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.VehicleDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/rawdata")
public class RawdataProviderController {
    @Autowired private VehicleDataLoader vehicleDataLoader;
    @Autowired private TestDataReader testDataReader;
    @Autowired private VehicleRegistration vehicleRegistration;
    @Autowired private TelematicsDataUpdater telematicsDataUpdater;
    @Autowired private DatabaseReset databaseReset;
    @Autowired private ApiHelper apiHelper;
    @Autowired private TestDataCategorized testDataCategorized;
    @Autowired private TestDataExporter testDataExporter;

    private ReentrantLock testDataMutex = new ReentrantLock();

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
            testDataMutex.lock();
            databaseReset.reset();
            return apiHelper.ok("Database reinitialized.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataMutex.unlock();
        }
    }

    @GetMapping("/init/vehicles")
    public ResponseEntity<ApiResult> initVehicles() {
        try {
            testDataMutex.lock();
            vehicleRegistration.registerFromTestDataCetegorized(getTestDataCategorized(
                    null, true));
            return apiHelper.ok("Vehicles registered.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataMutex.unlock();
        }
    }

    @GetMapping("/init/telematicsdata")
    public ResponseEntity<ApiResult> initTelematicsData() {
        try {
            testDataMutex.lock();
            telematicsDataUpdater.updateFromTestDataCetegorized(getTestDataCategorized(
                    null, true));
            return apiHelper.ok("Telematics data initialized.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataMutex.unlock();
        }
    }

    @GetMapping("/export/testdata")
    public ResponseEntity<ApiResult> exportTestdata(@RequestParam(required = false) @Nullable Integer limitperfile) {
        try {
            testDataMutex.lock();

            if(testdataExportPath==null) {
                throw new DataProviderException("Export path not set!");
            }

            if(limitperfile != null) {
                testDataExporter.exportLimited(getTestDataCategorized(null, true), Path.of(
                        testdataExportPath, "testdata_export_"
                                + exportDataFormatter.format(Instant.now())),
                        "json", limitperfile);
            } else {
                testDataExporter.export(getTestData(), Path.of(
                        testdataExportPath, "testdata_export_"
                                + exportDataFormatter.format(Instant.now()) + ".json"));
            }

            return apiHelper.ok("Test data exported.");
        }
        catch(DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataMutex.unlock();
        }
    }

    @PostMapping("/init/reinitbyfile")
    public ResponseEntity<ApiResult> reinitByFile(@RequestParam(value = "testdata", required = false)
                                                  @Nullable final MultipartFile testDataFileParam,
                                                  @RequestBody(required = false)
                                                  @Nullable final byte[] testDataFileBody) {

        try {
            testDataMutex.lock();

            if (testDataFileParam != null) {
                testDataMutex.lock();
                return reinitByJsonFile(new String(testDataFileParam.getBytes(), StandardCharsets.UTF_8));
            } else if (testDataFileBody != null) {
                return reinitByJsonFile(new String(testDataFileBody, StandardCharsets.UTF_8));
            } else {
                throw new DataProviderException("No JSON file given!");
            }
        } catch (Exception exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataMutex.unlock();
        }
    }

    @PostMapping("/init/appendbyfile")
    public ResponseEntity<ApiResult> appendByFile(@RequestParam(value = "testdata", required = false)
                                                  @Nullable final MultipartFile testDataFileParam,
                                                  @RequestBody(required = false)
                                                  @Nullable final byte[] testDataFileBody) {

        try {
            testDataMutex.lock();
            if (testDataFileParam != null) {
                return appendByJsonFile(new String(testDataFileParam.getBytes(), StandardCharsets.UTF_8));
            } else if (testDataFileBody != null) {
                return appendByJsonFile(new String(testDataFileBody, StandardCharsets.UTF_8));
            } else {
                throw new DataProviderException("No JSON file given!");
            }
        } catch (Exception exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataMutex.unlock();
        }
    }

    private synchronized ResponseEntity<ApiResult> reinitByJsonFile(@NotNull final String testDataJson)
            throws DataProviderException {

        getTestDataCategorized(testDataJson, true);

        return apiHelper.ok("Testdata reinitialized.");
    }

    private synchronized ResponseEntity<ApiResult> appendByJsonFile(@NotNull final String testDataJson)
            throws DataProviderException {

        getTestDataCategorized(testDataJson, false);

        return apiHelper.ok("Testdata appended.");
    }

    private TestData getTestData() throws DataProviderException {
        getTestDataCategorized(null, true);
        return testData;
    }

    private synchronized TestDataCategorized getTestDataCategorized(@Nullable final String testDataJson,
                                                                    @NotNull boolean resetTestdata)
            throws DataProviderException {

        if(testDataJson != null) {
            if(resetTestdata || testData == null) {
                testData = testDataReader.loadFromJson(testDataJson);
            } else {
                testDataReader.appendFromJson(testData, testDataJson);
            }

            testDataCategorized.reset();
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
