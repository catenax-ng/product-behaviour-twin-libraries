package net.catena_x.btp.hi.oem.backend.hi_service.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.hi.oem.backend.hi_service.collector.util.HICollectorOptionReader;
import net.catena_x.btp.hi.oem.backend.hi_service.collector.util.HIUpdateOptions;
import net.catena_x.btp.hi.oem.backend.hi_service.controller.swagger.CollectorRunTestDoc;
import net.catena_x.btp.hi.oem.backend.hi_service.controller.util.HIDbMaintainer;
import net.catena_x.btp.hi.oem.backend.hi_service.controller.util.HIJobRunner;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(HIBackendApiConfig.COLLECTOR_API_PATH_BASE)
public class HIBackendCollectorControllerRunTest {
    @Autowired private HIJobRunner jobRunner;
    @Autowired private HIDbMaintainer hiDbMaintainer;
    @Autowired private HICollectorOptionReader hiCollectorOptionReader;
    @Autowired private ApiHelper apiHelper;

    @GetMapping(value = "/runtest/{options}", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = CollectorRunTestDoc.SUMMARY, description = CollectorRunTestDoc.DESCRIPTION,
            tags = {"Development"},
            parameters = {@io.swagger.v3.oas.annotations.Parameter(
                    in = ParameterIn.PATH, name = "options",
                    description = CollectorRunTestDoc.OPTIONS_DESCRIPTION, required = true,
                        examples = {
                                @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        name = CollectorRunTestDoc.OPTIONS_EXAMPLE_1_NAME,
                                        description = CollectorRunTestDoc.OPTIONS_EXAMPLE_1_DESCRIPTION,
                                        value = CollectorRunTestDoc.OPTIONS_EXAMPLE_1_VALUE
                                ),
                                @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        name = CollectorRunTestDoc.OPTIONS_EXAMPLE_2_NAME,
                                        description = CollectorRunTestDoc.OPTIONS_EXAMPLE_2_DESCRIPTION,
                                        value = CollectorRunTestDoc.OPTIONS_EXAMPLE_2_VALUE
                                ),
                                @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        name = CollectorRunTestDoc.OPTIONS_EXAMPLE_3_NAME,
                                        description = CollectorRunTestDoc.OPTIONS_EXAMPLE_3_DESCRIPTION,
                                        value = CollectorRunTestDoc.OPTIONS_EXAMPLE_3_VALUE
                                ),
                                @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        name = CollectorRunTestDoc.OPTIONS_EXAMPLE_4_NAME,
                                        description = CollectorRunTestDoc.OPTIONS_EXAMPLE_4_DESCRIPTION,
                                        value = CollectorRunTestDoc.OPTIONS_EXAMPLE_4_VALUE
                                )
                        }
            )},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = CollectorRunTestDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = CollectorRunTestDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = CollectorRunTestDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = CollectorRunTestDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> run(@PathVariable @NotNull final String options) {

        HIUpdateOptions updateOptions = null;
        try {
            updateOptions = hiCollectorOptionReader.read(options);
        } catch(final OemHIException exception) {
            return apiHelper.failed(exception.getMessage());
        }

        if(updateOptions.isForceCalculationIgnoringQueue()) {
            jobRunner.resetQueue();
        }

        if(updateOptions.isResetHiDatabase()) {
            hiDbMaintainer.reset();
        }

        return jobRunner.startJob(updateOptions);
    }
}
