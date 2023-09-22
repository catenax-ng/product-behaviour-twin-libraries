package net.catena_x.btp.rul.mockups.supplier;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.rul.mockups.supplier.swagger.MockupDirectEndpointDoc;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.RuLNotificationToSupplierContentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
public class RulSupplierMockDirectEndpoint {
    @Autowired private ApiHelper apiHelper;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RuLSupplierMock rulSupplierMock;

    @io.swagger.v3.oas.annotations.Operation(
            summary = MockupDirectEndpointDoc.SUMMARY, description = MockupDirectEndpointDoc.DESCRIPTION,
            tags = {"MockUp"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = MockupDirectEndpointDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = MockupDirectEndpointDoc.BODY_EXAMPLE_1_NAME,
                                            description = MockupDirectEndpointDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = MockupDirectEndpointDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = MockupDirectEndpointDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = MockupDirectEndpointDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = MockupDirectEndpointDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = MockupDirectEndpointDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    @PostMapping(value = "api/v1/routine/notification", produces = "application/json")
    public ResponseEntity<DefaultApiResult> mockupRulDirect(
            @RequestBody @NotNull String data) {
        try {
            return rulSupplierMock.runRuLCalculationMock(getNotificationToSupplier(data));
        } catch (final Exception exception) {
            return apiHelper.failed("Direct endpoint failed: " + exception.getMessage());
        }
    }

    private NotificationDAO<RuLNotificationToSupplierContentDAO> getNotificationToSupplier(@NotNull final String data)
            throws Exception {
        return objectMapper.readValue(data, new TypeReference<>() {});
    }
}
