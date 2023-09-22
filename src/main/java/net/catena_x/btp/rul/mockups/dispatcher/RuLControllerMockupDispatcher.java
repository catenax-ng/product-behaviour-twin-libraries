package net.catena_x.btp.rul.mockups.dispatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.rul.mockups.dispatcher.swagger.MockupDispatcherDoc;
import net.catena_x.btp.rul.mockups.requester.RuLRequesterMock;
import net.catena_x.btp.rul.mockups.supplier.RuLSupplierMock;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.requester.RuLNotificationToRequesterContentDAO;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.RuLNotificationToSupplierContentDAO;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
public class RuLControllerMockupDispatcher {
    @Autowired private ApiHelper apiHelper;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RuLSupplierMock rulSupplierMock;
    @Autowired private RuLRequesterMock rulRequesterMock;

    @Value("${supplier.rulservice.inputAssetId}") private String inputAssetId;

    @io.swagger.v3.oas.annotations.Operation(
            summary = MockupDispatcherDoc.SUMMARY, description = MockupDispatcherDoc.DESCRIPTION,
            tags = {"MockUp"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            in = ParameterIn.PATH, name = "assetId",
                            description = MockupDispatcherDoc.ASSETID_DESCRIPTION, required = true,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = MockupDispatcherDoc.ASSETID_EXAMPLE_1_NAME,
                                            description = MockupDispatcherDoc.ASSETID_EXAMPLE_1_DESCRIPTION,
                                            value = MockupDispatcherDoc.ASSETID_EXAMPLE_1_VALUE
                                    )
                            }
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            in = ParameterIn.QUERY, name = "provider-connector-url",
                            description = MockupDispatcherDoc.PROVIDERCONNECTORURL_DESCRIPTION, required = true,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = MockupDispatcherDoc.PROVIDERCONNECTORURL_EXAMPLE_1_NAME,
                                            description = MockupDispatcherDoc.PROVIDERCONNECTORURL_EXAMPLE_1_DESCRIPTION,
                                            value = MockupDispatcherDoc.PROVIDERCONNECTORURL_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = MockupDispatcherDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = MockupDispatcherDoc.BODY_EXAMPLE_1_NAME,
                                            description = MockupDispatcherDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = MockupDispatcherDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = MockupDispatcherDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = MockupDispatcherDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = MockupDispatcherDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = MockupDispatcherDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    @PostMapping(value = "api/service/{assetId}/submodel", produces = "application/json")
    public ResponseEntity<DefaultApiResult> mockupReceive(
            @RequestBody @NotNull String data,
            @PathVariable @NotNull final String assetId,
            @RequestParam(required = true, name="provider-connector-url") @Nullable String providerConnectorUrl) {
        try {
            if(assetId.equals(inputAssetId)) {
                return rulSupplierMock.runRuLCalculationMock(getNotificationToSupplier(data));
            }
            else {
                return rulRequesterMock.receiveRuLResultFromOemMock(getNotificationToRequester(data));
            }
        } catch (final Exception exception) {
            return apiHelper.failed("Asset id " + assetId + " could not be dispatched: " + exception.getMessage());
        }
    }

    private NotificationDAO<RuLNotificationToRequesterContentDAO> getNotificationToRequester(@NotNull final String data)
            throws Exception {
        return objectMapper.readValue(
                data, new TypeReference<NotificationDAO<RuLNotificationToRequesterContentDAO>>() {});
    }

    private NotificationDAO<RuLNotificationToSupplierContentDAO> getNotificationToSupplier(@NotNull final String data)
            throws Exception {
        return objectMapper.readValue(
                data, new TypeReference<NotificationDAO<RuLNotificationToSupplierContentDAO>>() {});
    }
}