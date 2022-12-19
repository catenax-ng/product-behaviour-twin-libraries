package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger.ResetDoc;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.DatabaseReset;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE)
public class DataProviderControllerReset {
    @Autowired private DatabaseReset databaseReset;
    @Autowired private ApiHelper apiHelper;
    @Autowired private TestDataManager testDataManager;

    @GetMapping(value = "/reset", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = ResetDoc.SUMMARY, description = ResetDoc.DESCRIPTION,
            tags = {"Development"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = ResetDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = ResetDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = ResetDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = ResetDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> reset() {
        try {
            testDataManager.lock();
            databaseReset.reset();
            return apiHelper.ok("Database reinitialized.");
        }
        catch(final DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataManager.unlock();
        }
    }
}
