package net.catena_x.btp.rul.oem.backend.rul_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.edc.model.EdcAssetAddress;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.rul.oem.backend.rul_service.controller.swagger.ReceiverNotifyResultDoc;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.RuLNotificationFromSupplierContentDAO;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.RuLNotificationFromSupplierContentConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.receiver.RuLResultForwarder;
import net.catena_x.btp.rul.oem.backend.rul_service.util.RuLStarterApiResult;
import net.catena_x.btp.rul.oem.backend.util.enums.RuLServiceOptionHelper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(RuLBackendApiConfig.RECEIVER_API_PATH_BASE)
public class RuLBackendReceiverControllerNotifyResult {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RuLResultForwarder rulResultForwarder;
    @Autowired private RuLNotificationFromSupplierContentConverter rulNotificationFromSupplierContentConverter;
    @Autowired private RuLServiceOptionHelper rulServiceOptionHelper;
    @Autowired private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RuLBackendReceiverControllerNotifyResult.class);

    @PostMapping(value = "/notifyresult", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = ReceiverNotifyResultDoc.SUMMARY, description = ReceiverNotifyResultDoc.DESCRIPTION,
            tags = {"Productive"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = ReceiverNotifyResultDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = ReceiverNotifyResultDoc.BODY_EXAMPLE_1_NAME,
                                            description = ReceiverNotifyResultDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = ReceiverNotifyResultDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = ReceiverNotifyResultDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = ReceiverNotifyResultDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = RuLStarterApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = ReceiverNotifyResultDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = ReceiverNotifyResultDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> notifyResult(
            @RequestBody @NotNull NotificationDAO<RuLNotificationFromSupplierContentDAO> result,
            @RequestParam(required = false) @Nullable String forwardEdcAddress,
            @RequestParam(required = false) @Nullable String forwardBPN,
            @RequestParam(required = false) @Nullable String forwardAssetId,
            @RequestParam(required = false) @Nullable String forwardRefId,
            @RequestParam(required = false) @Nullable Boolean allowAdditionalResults) {

        try {
            if(rulServiceOptionHelper.isShowOutputFromSupplier()) {
                System.out.println("=======================");
                System.out.println("RuL output from supplier:");
                System.out.println(objectMapper.writeValueAsString(result));
                System.out.println("=======================");
            }
        } catch (final Exception exception) {
            logger.error("Output from supplier can not be mocked: " + exception.getMessage());
        }

        final EdcAssetAddress alternativeAsset = new EdcAssetAddress();
        alternativeAsset.setConnectorUrl(forwardEdcAddress);
        alternativeAsset.setBpn(forwardBPN);
        alternativeAsset.setAssetId(forwardAssetId);

        boolean ignoreAdditionalResults = (allowAdditionalResults==null) ? true : !allowAdditionalResults;

        try {
            return rulResultForwarder.forwardResult(result.getHeader().getReferencedNotificationID(), forwardRefId,
                    alternativeAsset, rulNotificationFromSupplierContentConverter.toDTO(result.getContent()),
                    ignoreAdditionalResults);
        } catch(final Exception exception){
            return apiHelper.failed("Forwarding result failed: " + exception.getMessage());
        }
    }
}
