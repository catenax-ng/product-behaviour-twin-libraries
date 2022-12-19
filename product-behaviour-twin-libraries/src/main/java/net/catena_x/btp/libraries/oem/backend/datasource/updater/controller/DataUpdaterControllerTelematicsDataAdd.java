package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger.TelematicsDataAddDoc;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
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
@RequestMapping(DataUpdaterApiConfig.API_PATH_BASE)
public class DataUpdaterControllerTelematicsDataAdd {
    @Autowired private VehicleTable vehicleTable;
    @Autowired private ApiHelper apiHelper;

    @PostMapping(value = "/telematicsdata/add", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = TelematicsDataAddDoc.SUMMARY, description = TelematicsDataAddDoc.DESCRIPTION,
            tags = {"Productive"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = TelematicsDataAddDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = TelematicsDataAddDoc.BODY_EXAMPLE_1_NAME,
                                            description = TelematicsDataAddDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = TelematicsDataAddDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = TelematicsDataAddDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = TelematicsDataAddDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = TelematicsDataAddDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = TelematicsDataAddDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> addTelemeticsData(@RequestBody @NotNull final InputTelematicsData telematicsData) {
        try {
            vehicleTable.appendTelematicsDataNewTransaction(telematicsData);
            return apiHelper.ok("Telematics data added.");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }
}
