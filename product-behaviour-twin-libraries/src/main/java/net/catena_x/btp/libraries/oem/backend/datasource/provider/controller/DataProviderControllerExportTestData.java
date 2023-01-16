package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataExporter;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger.ExportTestDataDoc;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
    @io.swagger.v3.oas.annotations.Operation(

            summary = ExportTestDataDoc.SUMMARY, description = ExportTestDataDoc.DESCRIPTION,
            tags = {"Development"},
            parameters = @io.swagger.v3.oas.annotations.Parameter(
                    in = ParameterIn.QUERY, name = ExportTestDataDoc.LIMIT_NAME,
                    description = ExportTestDataDoc.LIMIT_DESCRIPTION, required = false,
                    examples = {
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = ExportTestDataDoc.LIMIT_EXAMPLE_1_NAME,
                                    description = ExportTestDataDoc.LIMIT_EXAMPLE_1_DESCRIPTION,
                                    value = ExportTestDataDoc.LIMIT_EXAMPLE_1_VALUE
                            )
                    }
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = ExportTestDataDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = ExportTestDataDoc .RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = ExportTestDataDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = ExportTestDataDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
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
        catch(final Exception exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataManager.unlock();
        }
    }
}
