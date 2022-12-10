package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataExporter;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE)
public class DataProviderControllerExportTestData {
    @Autowired private ApiHelper apiHelper;
    @Autowired private TestDataExporter testDataExporter;
    @Autowired private TestDataManager testDataManager;

    @Value("${services.dataprovider.testdata.exportPath:#{null}}")
    private String testdataExportPath;

    private DateTimeFormatter exportDataFormatter =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneId.from(ZoneOffset.UTC));

    @GetMapping(value = "/export/testdata", produces = "application/json")
    public ResponseEntity<DefaultApiResult> exportTestdata(@RequestParam(required = false) @Nullable Integer limitperfile) {
        try {
            testDataManager.lock();

            if(testdataExportPath==null) {
                throw new DataProviderException("Export path not set!");
            }

            if(limitperfile != null) {
                testDataExporter.exportLimited(testDataManager.getTestDataCategorized(null, true), Path.of(
                                testdataExportPath, "testdata_export_"
                                        + exportDataFormatter.format(Instant.now())),
                        "json", limitperfile);
            } else {
                testDataExporter.export(testDataManager.getTestData(), Path.of(
                        testdataExportPath, "testdata_export_"
                                + exportDataFormatter.format(Instant.now()) + ".json"));
            }

            return apiHelper.ok("Test data exported.");
        }
        catch(final DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataManager.unlock();
        }
    }
}
