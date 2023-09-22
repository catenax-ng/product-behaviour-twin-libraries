package net.catena_x.btp.rul.oem.backend.rul_service.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;
import net.catena_x.btp.libraries.edc.EdcApi;
import net.catena_x.btp.libraries.edc.model.EdcAssetAddress;
import net.catena_x.btp.libraries.edc.util.exceptions.EdcException;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import net.catena_x.btp.rul.oem.backend.model.dto.calculation.RuLCalculation;
import net.catena_x.btp.rul.oem.backend.model.dto.calculation.RuLCalculationTable;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLCalculationStatus;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.requester.RuLDataToRequesterContent;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.requester.RuLNotificationToRequesterConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.RuLNotificationFromSupplierContent;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items.RuLOutput;
import net.catena_x.btp.rul.oem.backend.rul_service.receiver.util.RuLRequesterNotificationCreator;
import net.catena_x.btp.rul.oem.backend.util.enums.RuLServiceOptionHelper;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.function.Supplier;

@Component
public class RuLResultForwarder {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RuLCalculationTable rulCalculationTable;
    @Autowired private RuLNotificationToRequesterConverter rulNotificationToRequesterConverter;
    @Autowired private RuLRequesterNotificationCreator rulRequesterNotificationCreator;
    @Autowired private EdcApi edcApi;
    @Autowired private RuLServiceOptionHelper rulServiceOptionHelper;
    @Autowired private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RuLResultForwarder.class);

    public ResponseEntity<DefaultApiResult> forwardResult(
            @NotNull final String supplierNotificationID,
            @Nullable final String alternativeRequesterNotificationId, @Nullable final EdcAssetAddress alternativeAsset,
            @NotNull final RuLNotificationFromSupplierContent supplierNotificationContent,
            @NotNull final boolean ignoreAdditionalResults) {

        try {
            final RuLOutput result = ignoreAdditionalResults
                    ? DataHelper.getFirstItem(supplierNotificationContent.getEndurancePredictorOutputs())
                    : DataHelper.getFirstAndOnlyItem(supplierNotificationContent.getEndurancePredictorOutputs());
            assertIdsEqual(supplierNotificationID, supplierNotificationContent);

            final RemainingUsefulLife remainingUsefulLife = result.getRemainingUsefulLife();

            try {
                updateCalculationStatusToCalculated(supplierNotificationID, remainingUsefulLife);
            } catch (final Exception exception) {
                if(alternativeAsset==null) {
                    throw exception;
                } else if(!alternativeAsset.isFullyDefined()) {
                    throw exception;
                }
            }

            final RuLCalculation calculation = rulCalculationTable.getByIdNewTransaction(supplierNotificationID);

            if(!alternativeAsset.fillMissingData(calculation.getRequesterResultAddress())) {
                return apiHelper.failed("Failed to process RuL calculation result, unknown calculation id "
                        + supplierNotificationID + "!");
            }

            forwardResultWithHttp(supplierNotificationID,
                    alternativeRequesterNotificationId != null ?
                            alternativeRequesterNotificationId : calculation.getRequesterNotificationId(),
                    alternativeAsset, remainingUsefulLife);

            return apiHelper.ok("Forwarded RuL calculation result with id "
                    + supplierNotificationID + " successfully.");
        } catch (final Exception exception) {
            try {
                updateCalculationStatusToFailedProcessing(supplierNotificationID);
                final String error = "Failed to process RuL calculation with id "
                        + supplierNotificationID + ": " + exception.getMessage();
                logger.error(error);
                return apiHelper.failed(error);
            } catch (final Exception innerException) {
                final String error = "Failed to process RuL calculation result: " + innerException.getMessage();
                logger.error(error);
                return apiHelper.failed(error);
            }
        }
    }

    private void updateCalculationStatusToCalculated(
            @NotNull final String supplierNotificationID, @NotNull final RemainingUsefulLife remainingUsefulLife)
            throws Exception {

        final Supplier<Exception> updateStatus = ()-> {
            try {
                final RuLCalculation calculation =
                        rulCalculationTable.getByIdExternalTransaction(supplierNotificationID);
                assertCalculation(supplierNotificationID, calculation);
                assertCalculationInStaredState(supplierNotificationID, calculation.getStatus());

                rulCalculationTable.updateStatusAndRuLExternalTransaction(
                        supplierNotificationID, RuLCalculationStatus.CALCULATED, remainingUsefulLife);

            } catch(final Exception exception) {
                return exception;
            }

            return null;
        };

        final Exception updateException = rulCalculationTable.runSerializableNewTransaction(updateStatus);
        if(updateException != null) {
            throw updateException;
        }
    }

    private void forwardResultWithHttp(@NotNull final String supplierNotificationID,
                                       @NotNull final String requesterNotificationId,
                                       @NotNull @NotNull final EdcAssetAddress requesterAssetAddress,
                                       @NotNull final RemainingUsefulLife result) throws OemRuLException {

        final Notification<RuLDataToRequesterContent> notification =
                prepareNotification(supplierNotificationID, requesterNotificationId, requesterAssetAddress,
                        new RuLDataToRequesterContent(result));

        logger.info("Forwarding for id " + supplierNotificationID + " prepared.");

        checkForwardResult(supplierNotificationID, notification,
                forwardToRequester(supplierNotificationID, requesterAssetAddress, notification));
    }

    private Notification<RuLDataToRequesterContent> prepareNotification(
            @NotNull final String supplierNotificationID,
            @NotNull final String requesterNotificationId,
            @NotNull @NotNull final EdcAssetAddress requesterAssetAddress,
            @NotNull final RuLDataToRequesterContent rulDataToRequesterContent) throws OemRuLException {

        try {
            return rulRequesterNotificationCreator.createForHttp(requesterNotificationId, rulDataToRequesterContent,
                                                                 requesterAssetAddress);
        } catch (final Exception exception) {
            try {
                updateCalculationStatusToFaildBuildRequest(supplierNotificationID);
            } catch (final Exception updateException) {
                throw new OemRuLException(updateException);
            }

            throw new OemRuLException(exception);
        }
    }

    private void checkForwardResult(@NotNull final String supplierNotificationID,
                                    @NotNull final Notification<RuLDataToRequesterContent> notification,
                                    @NotNull final ResponseEntity<JsonNode> result) throws OemRuLException {
        if (result.getStatusCode() == HttpStatus.OK
                || result.getStatusCode() == HttpStatus.CREATED
                || result.getStatusCode() == HttpStatus.ACCEPTED) {
            logger.info("RuL calculation result for id " + supplierNotificationID + " forwarded.");
            try {
                updateCalculationStatusToSent(supplierNotificationID);
            } catch (final Exception exception) {
                throw new OemRuLException(exception);
            }
        } else {
            String notificationAsString = null;
            try {
                notificationAsString = objectMapper.writeValueAsString(notification);
            } catch (final JsonProcessingException exception) {
                notificationAsString = "JSON parse error!";
            }

            forwardingCallFailed(supplierNotificationID,
                    "Forwarding RuL calculation result to requester for id " + supplierNotificationID
                            + " failed: http code " + result.getStatusCode().toString() + ", response body: "
                            + result.getBody().toString() + " \nNotification: " + notificationAsString);
        }
    }

    private void forwardingCallFailed(@NotNull final String requestId,
                                      @NotNull final String errorText) throws OemRuLException {
        logger.error(errorText);

        try {
            rulCalculationTable.updateStatusNewTransaction(requestId, RuLCalculationStatus.SEND_TO_REQUESTER_FAILED);
        } catch (final Exception exception) {
            new OemRuLException(exception.getMessage() + "\n" + errorText, exception);
        }

        throw new OemRuLException(errorText);
    }

    private ResponseEntity<JsonNode> forwardToRequester(
            @NotNull final String supplierNotificationID, @NotNull final EdcAssetAddress requesterAssetAddress,
            @NotNull final Notification<RuLDataToRequesterContent> notification) throws OemRuLException {
        try {
            if(rulServiceOptionHelper.isShowOutputToRequester()) {
                System.out.println("=======================");
                System.out.println("RuL output to requester:");
                System.out.println(objectMapper.writeValueAsString(notification));
                System.out.println("=======================");
            }
        } catch (final Exception exception) {
            logger.error("Output to requester can not be mocked: " + exception.getMessage());
        }

        return startAsyncRequest(supplierNotificationID, requesterAssetAddress.getConnectorUrl(),
                requesterAssetAddress.getAssetId(), rulNotificationToRequesterConverter.toDAO(notification),
                JsonNode.class);
    }

    public <BodyType, ResponseType> ResponseEntity<ResponseType> startAsyncRequest(
            @NotNull final String supplierNotificationID,
            @NotNull final String endpoint, @NotNull final String asset,
            @NotNull final BodyType messageBody, @NotNull Class<ResponseType> responseTypeClass)
            throws OemRuLException {

        try {
            return edcApi.post(HttpUrl.parse(endpoint), asset, responseTypeClass,
                    messageBody, generateDefaultHeaders());
        } catch (final EdcException exception) {
            try {
                updateCalculationStatusToFailedSending(supplierNotificationID);
            } catch(final Exception updateException) {
                throw new OemRuLException(updateException);
            }

            throw new OemRuLException(exception);
        }
    }

    protected HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private void updateCalculationStatusToFailedProcessing(@NotNull final String supplierNotificationID)
            throws Exception {
        rulCalculationTable.updateStatusNewTransaction(
                supplierNotificationID, RuLCalculationStatus.FAILED_INTERNAL_PROCESS_RESULTS);
    }

    private void updateCalculationStatusToFailedSending(@NotNull final String supplierNotificationID) throws Exception {
        rulCalculationTable.updateStatusNewTransaction(
                supplierNotificationID, RuLCalculationStatus.SEND_TO_REQUESTER_FAILED);
    }

    private void updateCalculationStatusToSent(@NotNull final String supplierNotificationID) throws Exception {
        rulCalculationTable.updateStatusNewTransaction(supplierNotificationID, RuLCalculationStatus.SENT_TO_REQUESTER);
    }

    private void updateCalculationStatusToFaildBuildRequest(@NotNull final String supplierNotificationID)
            throws Exception {
        rulCalculationTable.updateStatusNewTransaction(
                supplierNotificationID, RuLCalculationStatus.FAILED_INTERNAL_BUILD_RESULT_REQUEST);
    }

    private static void assertCalculationInStaredState(
            @NotNull final String supplierNotificationID, @NotNull final RuLCalculationStatus status)
            throws OemRuLException {
        if(status != RuLCalculationStatus.CREATED
                && status != RuLCalculationStatus.RUNNING) {
            throw new OemRuLException("Calculation id " + supplierNotificationID + " had status "
                    + status.toString() + "!");
        }
    }

    private static void assertCalculation(
            @NotNull final String supplierNotificationID, @Nullable final RuLCalculation calculation)
            throws OemRuLException {
        if(calculation == null) {
            throw new OemRuLException("Unknown calculation id " + supplierNotificationID + "!");
        }
    }

    private static void assertIdsEqual(@NotNull final String supplierNotificationID,
                                       @NotNull final RuLNotificationFromSupplierContent supplierNotificatonContent)
            throws OemRuLException {
        if(supplierNotificatonContent.getRequestRefId() != null) {
            if (!supplierNotificationID.equals(supplierNotificatonContent.getRequestRefId())) {
                throw new OemRuLException("Id in notification content is not equal to referenced notification id!");
            }
        }
    }
}

