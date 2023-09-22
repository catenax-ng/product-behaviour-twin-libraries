package net.catena_x.btp.rul.oem.backend.rul_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.edc.model.EdcAssetAddress;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.rul.oem.backend.rul_service.collector.RuLCalculationStarter;
import net.catena_x.btp.rul.oem.backend.rul_service.controller.swagger.CollectorNotifyCalculationDoc;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.requester.RuLNotificationFromRequesterContentDAO;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.requester.RuLNotificationFromRequesterContentConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.util.RuLStarterApiResult;
import net.catena_x.btp.rul.oem.backend.util.enums.RuLServiceOptionHelper;
import net.catena_x.btp.rul.oem.backend.util.enums.RuLStarterCalculationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@RestController
@RequestMapping(RuLBackendApiConfig.COLLECTOR_API_PATH_BASE)
public class RuLBackendCollectorControllerNotifyCalculation {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RuLCalculationStarter rulCalculationStarter;
    @Autowired private RuLNotificationFromRequesterContentConverter rulNotificationFromRequesterContentConverter;
    @Autowired private RuLServiceOptionHelper rulServiceOptionHelper;
    @Autowired private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RuLBackendCollectorControllerNotifyCalculation.class);

    @PostMapping(value = "/notifycalculation", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = CollectorNotifyCalculationDoc.SUMMARY, description = CollectorNotifyCalculationDoc.DESCRIPTION,
            tags = {"Productive"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = CollectorNotifyCalculationDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = CollectorNotifyCalculationDoc.BODY_EXAMPLE_1_NAME,
                                            description = CollectorNotifyCalculationDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = CollectorNotifyCalculationDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = CollectorNotifyCalculationDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = CollectorNotifyCalculationDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = RuLStarterApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = CollectorNotifyCalculationDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = CollectorNotifyCalculationDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<RuLStarterApiResult> notifyCalculation(
            @RequestBody @NotNull NotificationDAO<RuLNotificationFromRequesterContentDAO> request) {
        try {
            if(rulServiceOptionHelper.isShowInputFromRequester()) {
                System.out.println("=======================");
                System.out.println("RuL input from requester:");
                System.out.println(objectMapper.writeValueAsString(request));
                System.out.println("=======================");
            }
        } catch (final Exception exception) {
            logger.error("Input from requester can not be mocked: " + exception.getMessage());
        }

        try {
            return rulCalculationStarter.startCalculation(request.getHeader().getNotificationID(),
                    EdcAssetAddress.replyAddressFromNotification(request),
                    rulNotificationFromRequesterContentConverter.toDTO(request.getContent()));
        } catch (final BtpException exception) {
            return apiHelper.ok(new RuLStarterApiResult(Instant.now(), RuLStarterCalculationType.REQUEST_ERROR,
                    "Error in request notification: " + exception.getMessage()));
        }
    }
}
