package net.catena_x.btp.rul.oem.backend.rul_service.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.rul.oem.backend.rul_service.controller.swagger.InitializerDeleteVinRelationDoc;
import net.catena_x.btp.rul.oem.backend.rul_service.initializer.VinRelationDeleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(RuLBackendApiConfig.INITIALIZER_API_PATH_BASE)
public class RuLBackendInitializerDeleteVinRelation {
    @Autowired private VinRelationDeleter vinRelationDeleter;

    private final Logger logger = LoggerFactory.getLogger(RuLBackendCollectorControllerNotifyCalculation.class);

    @GetMapping(value = "/vinrelation/delete/{vin}", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = InitializerDeleteVinRelationDoc.SUMMARY,
            description = InitializerDeleteVinRelationDoc.DESCRIPTION,
            tags = {"Initialization"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            in = ParameterIn.PATH, name = "vin",
                            description = InitializerDeleteVinRelationDoc.VIN_DESCRIPTION, required = true,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitializerDeleteVinRelationDoc.VIN_EXAMPLE_1_NAME,
                                            description = InitializerDeleteVinRelationDoc.VIN_EXAMPLE_1_DESCRIPTION,
                                            value = InitializerDeleteVinRelationDoc.VIN_EXAMPLE_1_VALUE
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InitializerDeleteVinRelationDoc.VIN_EXAMPLE_2_NAME,
                                            description = InitializerDeleteVinRelationDoc.VIN_EXAMPLE_2_DESCRIPTION,
                                            value = InitializerDeleteVinRelationDoc.VIN_EXAMPLE_2_VALUE
                                    )
                            }
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = InitializerDeleteVinRelationDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InitializerDeleteVinRelationDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = InitializerDeleteVinRelationDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InitializerDeleteVinRelationDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> deleteVinRelation(@PathVariable @NotNull String vin) {
        return vinRelationDeleter.deleteRelation(vin);
    }
}
