package net.catena_x.btp.hi.supplier.mockup.controller;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationFromSupplierContentDAO;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationToSupplierContentDAO;
import net.catena_x.btp.hi.supplier.mockup.controller.swagger.SupplierMockUpSynchronousEndpointDoc;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class HIServiceControllerSupplierMockSynchronousEndpoint {
    @Autowired private HIServiceControllerSupplierMock service;

    @PostMapping(value = "api/v1/routine/hiservice", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = SupplierMockUpSynchronousEndpointDoc.SUMMARY, description = SupplierMockUpSynchronousEndpointDoc.DESCRIPTION,
            tags = {"MockUp"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = SupplierMockUpSynchronousEndpointDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = SupplierMockUpSynchronousEndpointDoc.BODY_EXAMPLE_1_NAME,
                                            description = SupplierMockUpSynchronousEndpointDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = SupplierMockUpSynchronousEndpointDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "202",
                            description = SupplierMockUpSynchronousEndpointDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = SupplierMockUpSynchronousEndpointDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = HINotificationFromSupplierContentDAO.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = SupplierMockUpSynchronousEndpointDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = SupplierMockUpSynchronousEndpointDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )
                    )
            }
    )
    public ResponseEntity<HINotificationFromSupplierContentDAO> runHICalculationMockSynchron(
            @RequestBody @NotNull HINotificationToSupplierContentDAO data) {
        return service.runHICalculationMockSynchron(data);
    }
}
