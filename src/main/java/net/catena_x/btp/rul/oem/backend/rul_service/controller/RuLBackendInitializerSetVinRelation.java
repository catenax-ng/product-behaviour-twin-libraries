package net.catena_x.btp.rul.oem.backend.rul_service.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.rul.oem.backend.rul_service.controller.swagger.InitializerSetVinRelationDoc;
import net.catena_x.btp.rul.oem.backend.rul_service.initializer.VinRelationSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(RuLBackendApiConfig.INITIALIZER_API_PATH_BASE)
public class RuLBackendInitializerSetVinRelation {
    @Autowired VinRelationSetter vinRelationSetter;

    private final Logger logger = LoggerFactory.getLogger(RuLBackendCollectorControllerNotifyCalculation.class);

    @GetMapping(value = "/vinrelation/set/{vin}", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = InitializerSetVinRelationDoc.SUMMARY, description = InitializerSetVinRelationDoc.DESCRIPTION,
            tags = {"Initialization"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            in = ParameterIn.PATH, name = "vin",
                            description = InitializerSetVinRelationDoc.VIN_DESCRIPTION, required = true,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitializerSetVinRelationDoc.VIN_EXAMPLE_1_NAME,
                                            description = InitializerSetVinRelationDoc.VIN_EXAMPLE_1_DESCRIPTION,
                                            value = InitializerSetVinRelationDoc.VIN_EXAMPLE_1_VALUE
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitializerSetVinRelationDoc.VIN_EXAMPLE_2_NAME,
                                            description = InitializerSetVinRelationDoc.VIN_EXAMPLE_2_DESCRIPTION,
                                            value = InitializerSetVinRelationDoc.VIN_EXAMPLE_2_VALUE
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitializerSetVinRelationDoc.VIN_EXAMPLE_3_NAME,
                                            description = InitializerSetVinRelationDoc.VIN_EXAMPLE_3_DESCRIPTION,
                                            value = InitializerSetVinRelationDoc.VIN_EXAMPLE_3_VALUE
                                    )
                            }
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            in = ParameterIn.QUERY, name = "refId",
                            description = InitializerSetVinRelationDoc.REFID_DESCRIPTION, required = true,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitializerSetVinRelationDoc.REFID_EXAMPLE_1_NAME,
                                            description = InitializerSetVinRelationDoc.REFID_EXAMPLE_1_DESCRIPTION,
                                            value = InitializerSetVinRelationDoc.REFID_EXAMPLE_1_VALUE
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitializerSetVinRelationDoc.REFID_EXAMPLE_2_NAME,
                                            description = InitializerSetVinRelationDoc.REFID_EXAMPLE_2_DESCRIPTION,
                                            value = InitializerSetVinRelationDoc.REFID_EXAMPLE_2_VALUE
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitializerSetVinRelationDoc.REFID_EXAMPLE_3_NAME,
                                            description = InitializerSetVinRelationDoc.REFID_EXAMPLE_3_DESCRIPTION,
                                            value = InitializerSetVinRelationDoc.REFID_EXAMPLE_3_VALUE
                                    )
                            }
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = InitializerSetVinRelationDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InitializerSetVinRelationDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = InitializerSetVinRelationDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InitializerSetVinRelationDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> setVinRelation(
            @PathVariable @NotNull String vin, @RequestParam(required = true) @NotNull String refId) {
        return vinRelationSetter.setRelation(vin, refId);
    }
}
