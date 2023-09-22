package net.catena_x.btp.hi.oem.frontend.rest.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicleTable;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicle;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicleConverter;
import net.catena_x.btp.hi.oem.frontend.rest.controller.base.HIFrontendControllerVehicleBase;
import net.catena_x.btp.hi.oem.frontend.rest.controller.swagger.VehicleIdDoc;
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
public class HIFrontendControllerVehicleId extends HIFrontendControllerVehicleBase {
    @Autowired ApiHelper apiHelper;
    @Autowired HIFVehicleConverter hifVehicleConverter;
    @Autowired HIVehicleTable hiVehicleTable;

    private final Logger logger = LoggerFactory.getLogger(HIFrontendControllerVehicleId.class);

    @GetMapping(value = "/vehicle/id/{vehicleId}", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = VehicleIdDoc.SUMMARY, description = VehicleIdDoc.DESCRIPTION,
            tags = {"Productive"},
            parameters = {@io.swagger.v3.oas.annotations.Parameter(
                    in = ParameterIn.PATH, name = "vehicleId",
                    description = VehicleIdDoc.VEHICLEID_DESCRIPTION, required = true,
                    examples = {
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = VehicleIdDoc.VEHICLEID_EXAMPLE_1_NAME,
                                    description = VehicleIdDoc.VEHICLEID_EXAMPLE_1_DESCRIPTION,
                                    value = VehicleIdDoc.VEHICLEID_EXAMPLE_1_VALUE
                            )
                    }
            )},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = VehicleIdDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = VehicleIdDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = HIFVehicle.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = VehicleIdDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = VehicleIdDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<String> getVehicleById(@PathVariable @NotNull final String vehicleId) {
        try {
            return checkVehicle(hifVehicleConverter.toDAO(
                    hiVehicleTable.getByIdWithHealthIndicatorsNewTransaction(vehicleId)), apiHelper);
        } catch (final OemHIException exception) {
            logger.error(exception.getMessage());
            return apiHelper.failedAsString(exception.getMessage());
        }
    }
}
