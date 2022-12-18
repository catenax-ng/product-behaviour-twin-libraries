package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger.InitAppendByFileDoc;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE)
public class DataProviderControllerInitAppendByFile {
    @Autowired private ApiHelper apiHelper;
    @Autowired private TestDataManager testDataManager;

    @PostMapping(value = "/init/appendbyfile", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = InitAppendByFileDoc.SUMMARY, description = InitAppendByFileDoc.DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = InitAppendByFileDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitAppendByFileDoc.BODY_EXAMPLE_1_NAME,
                                            description = InitAppendByFileDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = InitAppendByFileDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = InitAppendByFileDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InitAppendByFileDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = InitAppendByFileDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InitAppendByFileDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> appendByFile(@RequestParam(value = "testdata", required = false)
                                                         @Nullable final MultipartFile testDataFileParam,
                                                         @RequestBody(required = false)
                                                         @Nullable final byte[] testDataFileBody) {

        try {
            testDataManager.lock();
            if (testDataFileParam != null) {
                return appendByJsonFile(new String(testDataFileParam.getBytes(), StandardCharsets.UTF_8));
            } else if (testDataFileBody != null) {
                return appendByJsonFile(new String(testDataFileBody, StandardCharsets.UTF_8));
            } else {
                throw new DataProviderException("No JSON file given!");
            }
        } catch (final Exception exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataManager.unlock();
        }
    }

    private synchronized ResponseEntity<DefaultApiResult> appendByJsonFile(@NotNull final String testDataJson)
            throws DataProviderException {
        testDataManager.getTestDataCategorized(testDataJson, false);
        return apiHelper.ok("Testdata appended.");
    }
}
