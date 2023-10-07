package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestdataConfig;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger.InitAppendByConfigDoc;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.tesdtata.TestDataManager;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE)
public class DataProviderControllerInitAppendByConfig {
    @Autowired private ApiHelper apiHelper;
    @Autowired private TestDataManager testDataManager;

    @PostMapping(value = "/init/appendbyconfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.Operation(
            summary = InitAppendByConfigDoc.SUMMARY, description = InitAppendByConfigDoc.DESCRIPTION,
            tags = {"Development"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = InitAppendByConfigDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitAppendByConfigDoc.BODY_EXAMPLE_1_NAME,
                                            description = InitAppendByConfigDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = InitAppendByConfigDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = InitAppendByConfigDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InitAppendByConfigDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = InitAppendByConfigDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InitAppendByConfigDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> appendByConfig(@NotNull @RequestBody final TestdataConfig config) {

        try {
            testDataManager.lock();
            return appendByConfigSync(config);
        } catch (final Exception exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataManager.unlock();
        }
    }

    private synchronized ResponseEntity<DefaultApiResult> appendByConfigSync(@NotNull final TestdataConfig config)
            throws DataProviderException {
        testDataManager.getTestDataCategorized(config, false);
        return apiHelper.ok("Testdata appended.");
    }
}
