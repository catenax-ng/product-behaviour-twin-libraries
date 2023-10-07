package net.catena_x.btp.hi.supplier.mockup.controller;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationToSupplierContentDAO;
import net.catena_x.btp.hi.supplier.mockup.controller.swagger.SupplierMockUpDirectEndpointDoc;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class HIServiceControllerSupplierMockDirectEndpoint {
    @Autowired private HIServiceControllerSupplierMock service;

    @PostMapping(value = "api/v1/routine/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.Operation(
            summary = SupplierMockUpDirectEndpointDoc.SUMMARY, description = SupplierMockUpDirectEndpointDoc.DESCRIPTION,
            tags = {"MockUp"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = SupplierMockUpDirectEndpointDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = SupplierMockUpDirectEndpointDoc.BODY_EXAMPLE_1_NAME,
                                            description = SupplierMockUpDirectEndpointDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = SupplierMockUpDirectEndpointDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "202",
                            description = SupplierMockUpDirectEndpointDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = SupplierMockUpDirectEndpointDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = SupplierMockUpDirectEndpointDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = SupplierMockUpDirectEndpointDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )
                    )
            }
    )
    public ResponseEntity<DefaultApiResult> runHICalculationMock(
            @RequestBody @NotNull NotificationDAO<HINotificationToSupplierContentDAO> data) {
        return service.runHICalculationMock(data, "example-not-predefined", null);
    }
}
