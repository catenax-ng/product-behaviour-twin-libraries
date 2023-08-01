package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestdataConfig;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger.SqlExportByConfigDoc;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.testdata.TestDataExporter;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE)
public class DataProviderControllerSqlExportByConfig {
    @Value("${hidb.schemaname:hi}") private String defaultSchemaName;
    @Value("${hidb.username:hi_user}") private String defaultUserName;

    @Autowired private TestDataExporter testDataExporter;
    @Autowired private ApiHelper apiHelper;
    @PostMapping(value = "/init/sqlexportbyconfig", produces = "application/sql")
    @io.swagger.v3.oas.annotations.Operation(
            summary = SqlExportByConfigDoc.SUMMARY, description = SqlExportByConfigDoc.DESCRIPTION,
            tags = {"Development"},
            parameters = {
                    @Parameter(
                            in = ParameterIn.QUERY, name = SqlExportByConfigDoc.SCHEMA_NAME_NAME,
                            description = SqlExportByConfigDoc.SCHEMA_NAME_DESCRIPTION, required = false,
                            examples = {
                                    @ExampleObject(
                                            name = SqlExportByConfigDoc.SCHEMA_NAME_EXAMPLE_1_NAME,
                                            description = SqlExportByConfigDoc.SCHEMA_NAME_EXAMPLE_1_DESCRIPTION,
                                            value = SqlExportByConfigDoc.SCHEMA_NAME_EXAMPLE_1_VALUE
                                    ),
                                    @ExampleObject(
                                            name = SqlExportByConfigDoc.SCHEMA_NAME_EXAMPLE_2_NAME,
                                            description = SqlExportByConfigDoc.SCHEMA_NAME_EXAMPLE_2_DESCRIPTION,
                                            value = SqlExportByConfigDoc.SCHEMA_NAME_EXAMPLE_2_VALUE
                                    )
                            }
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = SqlExportByConfigDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = SqlExportByConfigDoc.BODY_EXAMPLE_1_NAME,
                                            description = SqlExportByConfigDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = SqlExportByConfigDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = SqlExportByConfigDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/sql",
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = SqlExportByConfigDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = SqlExportByConfigDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = SqlExportByConfigDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<String> sqlExportByConfig(
            @NotNull @RequestBody final TestdataConfig config,
            @Nullable @RequestParam(value = "schemaname", required = false) String schemaName) {

        if(schemaName == null) {
            schemaName = defaultSchemaName;
        }

        try {
            return sqlExportByConfigSync(config, schemaName);
        } catch (final Exception exception) {
            return apiHelper.failedAsString(exception.toString());
        } finally {
        }
    }

    private synchronized ResponseEntity<String> sqlExportByConfigSync(
            @NotNull final TestdataConfig config, @NotNull final String schemaName) throws DataProviderException {
        return apiHelper.okSql(testDataExporter.exportToSqlFromConfig(config, schemaName));
    }
}
