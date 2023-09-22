package net.catena_x.btp.hi.oem.frontend.rest.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.hi.oem.backend.hi_service.receiver.HIResultProcessor;
import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicleTable;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicle;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicleConverter;
import net.catena_x.btp.hi.oem.frontend.rest.controller.base.HIFrontendControllerVehicleBase;
import net.catena_x.btp.hi.oem.frontend.rest.controller.swagger.VehicleGearboxIdDoc;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
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
@RequestMapping(HIFrontendApiConfig.API_PATH_BASE)
public class HIFrontendControllerVehicleGearboxId extends HIFrontendControllerVehicleBase {
    @Autowired ApiHelper apiHelper;
    @Autowired HIFVehicleConverter hifVehicleConverter;
    @Autowired HIVehicleTable hiVehicleTable;

    private final Logger logger = LoggerFactory.getLogger(HIResultProcessor.class);

    @GetMapping(value = "/vehicle/gearboxid/{gearboxId}", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = VehicleGearboxIdDoc.SUMMARY, description = VehicleGearboxIdDoc.DESCRIPTION,
            tags = {"Productive"},
            parameters = {@io.swagger.v3.oas.annotations.Parameter(
                    in = ParameterIn.PATH, name = "gearboxId",
                    description = VehicleGearboxIdDoc.GEARBOXID_DESCRIPTION, required = true,
                    examples = {
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = VehicleGearboxIdDoc.GEARBOXID_EXAMPLE_1_NAME,
                                    description = VehicleGearboxIdDoc.GEARBOXID_EXAMPLE_1_DESCRIPTION,
                                    value = VehicleGearboxIdDoc.GEARBOXID_EXAMPLE_1_VALUE
                            )
                    }
            )},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = VehicleGearboxIdDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = VehicleGearboxIdDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = HIFVehicle.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = VehicleGearboxIdDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = VehicleGearboxIdDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<String> getVehicleBygearboxId(@PathVariable @NotNull final String gearboxId) {
        try {
            return checkVehicle(hifVehicleConverter.toDAO(
                    hiVehicleTable.getByGearboxIdWithHealthIndicatorsNewTransaction(gearboxId)), apiHelper);
        } catch (final OemHIException exception) {
            logger.error(exception.getMessage());
            return apiHelper.failedAsString(exception.getMessage());
        }
    }
}
