package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataExporter;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger.ExportTestDataDoc;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.tesdtata.TestDataManager;
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
    @Operation(
            summary = ExportTestDataDoc.SUMMARY, description = ExportTestDataDoc.DESCRIPTION,
            tags = {"Development"},
            parameters = {
                    @Parameter(
                            in = ParameterIn.QUERY, name = ExportTestDataDoc.LIMIT_NAME,
                            description = ExportTestDataDoc.LIMIT_DESCRIPTION, required = false,
                            examples = {
                                    @ExampleObject(
                                            name = ExportTestDataDoc.LIMIT_EXAMPLE_1_NAME,
                                            description = ExportTestDataDoc.LIMIT_EXAMPLE_1_DESCRIPTION,
                                            value = ExportTestDataDoc.LIMIT_EXAMPLE_1_VALUE
                                    )
                            }
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY, name = ExportTestDataDoc.USEOLDBAMMVERSION_NAME,
                            description = ExportTestDataDoc.USEOLDBAMMVERSION_DESCRIPTION, required = false,
                            examples = {
                                    @ExampleObject(
                                            name = ExportTestDataDoc.USEOLDBAMMVERSION_EXAMPLE_1_NAME,
                                            description = ExportTestDataDoc.USEOLDBAMMVERSION_EXAMPLE_1_DESCRIPTION,
                                            value = ExportTestDataDoc.USEOLDBAMMVERSION_EXAMPLE_1_VALUE
                                    ),
                                    @ExampleObject(
                                            name = ExportTestDataDoc.USEOLDBAMMVERSION_EXAMPLE_2_NAME,
                                            description = ExportTestDataDoc.USEOLDBAMMVERSION_EXAMPLE_2_DESCRIPTION,
                                            value = ExportTestDataDoc.USEOLDBAMMVERSION_EXAMPLE_2_VALUE
                                    )
                            }
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY, name = ExportTestDataDoc.ONLYFIRSTLOADSPECTRUMPERTYPE_NAME,
                            description = ExportTestDataDoc.ONLYFIRSTLOADSPECTRUMPERTYPE_DESCRIPTION, required = false,
                            examples = {
                                    @ExampleObject(
                                            name = ExportTestDataDoc.ONLYFIRSTLOADSPECTRUMPERTYPE_EXAMPLE_1_NAME,
                                            description = ExportTestDataDoc.ONLYFIRSTLOADSPECTRUMPERTYPE_EXAMPLE_1_DESCRIPTION,
                                            value = ExportTestDataDoc.ONLYFIRSTLOADSPECTRUMPERTYPE_EXAMPLE_1_VALUE
                                    ),
                                    @ExampleObject(
                                            name = ExportTestDataDoc.ONLYFIRSTLOADSPECTRUMPERTYPE_EXAMPLE_2_NAME,
                                            description = ExportTestDataDoc.ONLYFIRSTLOADSPECTRUMPERTYPE_EXAMPLE_2_DESCRIPTION,
                                            value = ExportTestDataDoc.ONLYFIRSTLOADSPECTRUMPERTYPE_EXAMPLE_2_VALUE
                                    )
                            }
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY, name = ExportTestDataDoc.EXPORTDAMAGEANDRUL_NAME,
                            description = ExportTestDataDoc.EXPORTDAMAGEANDRUL_DESCRIPTION, required = false,
                            examples = {
                                    @ExampleObject(
                                            name = ExportTestDataDoc.EXPORTDAMAGEANDRUL_EXAMPLE_1_NAME,
                                            description = ExportTestDataDoc.EXPORTDAMAGEANDRUL_EXAMPLE_1_DESCRIPTION,
                                            value = ExportTestDataDoc.EXPORTDAMAGEANDRUL_EXAMPLE_1_VALUE
                                    ),
                                    @ExampleObject(
                                            name = ExportTestDataDoc.EXPORTDAMAGEANDRUL_EXAMPLE_2_NAME,
                                            description = ExportTestDataDoc.EXPORTDAMAGEANDRUL_EXAMPLE_2_DESCRIPTION,
                                            value = ExportTestDataDoc.EXPORTDAMAGEANDRUL_EXAMPLE_2_VALUE
                                    )
                            }
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = ExportTestDataDoc.RESPONSE_OK_DESCRIPTION,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = ExportTestDataDoc .RESPONSE_OK_VALUE
                                            )},
                                    schema = @Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @ApiResponse(
                            responseCode = "500",
                            description = ExportTestDataDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = ExportTestDataDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> exportTestdata(
            @RequestParam(required = false) @Nullable final Integer limitPerFile,
            @RequestParam(required = false) @Nullable final Boolean useOldBammVersion,
            @RequestParam(required = false) @Nullable final Boolean onlyFirstLoadSpectrumPerType,
            @RequestParam(required = false) @Nullable final Boolean exportDamageAndRul) {
        try {
            testDataManager.lock();

            if(testdataExportPath == null) {
                throw new DataProviderException("Export path not set!");
            }

            final boolean oldBammVersion = (useOldBammVersion != null)? useOldBammVersion : false;
            final boolean exportOnlyOneLoadSpectrumPerType =
                    (onlyFirstLoadSpectrumPerType != null)? onlyFirstLoadSpectrumPerType : false;
            final boolean exportDamageAndRulAspects = (exportDamageAndRul != null)? exportDamageAndRul : true;

            if(limitPerFile != null) {
                testDataExporter.exportLimited(testDataManager.getTestDataCategorized(null, true),
                        Path.of(testdataExportPath, "testdata_export_"
                                        + exportDataFormatter.format(Instant.now())), "json",
                        limitPerFile, oldBammVersion, exportOnlyOneLoadSpectrumPerType, exportDamageAndRulAspects);
            } else {
                testDataExporter.export(testDataManager.getTestDataCategorized(null, true),
                        Path.of(testdataExportPath, "testdata_export_"
                                        + exportDataFormatter.format(Instant.now()) + ".json"),
                        oldBammVersion, exportOnlyOneLoadSpectrumPerType, exportDamageAndRulAspects);
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
