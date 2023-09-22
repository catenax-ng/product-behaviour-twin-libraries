package net.catena_x.btp.hi.oem.backend.hi_service.collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.hi.oem.backend.hi_service.collector.enums.UpdateInfo;
import net.catena_x.btp.hi.oem.backend.hi_service.collector.util.*;
import net.catena_x.btp.hi.oem.backend.hi_service.controller.util.HIKnowledgeAgentTest;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.HIDataToSupplierContent;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.HINotificationToSupplierConverter;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.HealthIndicatorInput;
import net.catena_x.btp.hi.oem.common.model.dto.calculation.HICalculation;
import net.catena_x.btp.hi.oem.common.model.dto.calculation.HICalculationTable;
import net.catena_x.btp.hi.oem.common.model.dto.knowledgeagent.HIKAInputs;
import net.catena_x.btp.hi.oem.common.model.enums.HICalculationStatus;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.edc.EdcApi;
import net.catena_x.btp.libraries.edc.util.exceptions.EdcException;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class HIDataCollector {
    public final static String DATA_VERSION = "DV_0.0.99";
    public final static String INPUT_ASSET_NAME_TEST_PREDEFINED = "predefined";

    @Autowired private HINotificationToSupplierConverter hiNotificationToSupplierConverter;
    @Autowired private HINotificationCreator hiNotificationCreator;
    @Autowired private HIVehicleCollector hiVehicleCollector;
    @Autowired private HIInputDataBuilder hiInputDataBuilder;
    @Autowired private HICalculationTable hiCalculationTable;
    @Autowired private InfoTable infoTable;
    @Autowired private HIVehicleRegistrator vehicleRegistrator;
    @Autowired private HIKnowledgeAgentTest knowledgeAgentTest;
    @Autowired private EdcApi edcApi;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;

    @Value("${supplier.hiservice.inputAssetId}") private String inputAssetId;
    @Value("${supplier.hiservice.endpoint}") private URL supplierHiServiceEndpoint;

    private final Logger logger = LoggerFactory.getLogger(HIDataCollector.class);

    public void doUpdate() throws OemHIException {
        doUpdate(new HIUpdateOptions());
    }

    public synchronized UpdateInfo doUpdate(@NotNull final HIUpdateOptions options) throws OemHIException {
        final long syncCounterMin = getLastSyncCounterReady() + 1L;

        try {
            final List<Vehicle> updatedVehicles = hiVehicleCollector.collect(options.isRecalculateAllVehicles()?
                                                                                            0L :  syncCounterMin);

            removeVehiclesWithMissingTelematicsData(updatedVehicles);

            if (DataHelper.isNullOrEmpty(updatedVehicles)) {
                logger.info("No updated vehicles this time!");
                return UpdateInfo.NOTHING_TO_UPDATE;
            }

            logger.info("Found " + updatedVehicles.size() + " updated vehicles!");

            registerNewVehicles(updatedVehicles);

            final Instant currentDatabaseTimestamp = infoTable.getCurrentDatabaseTimestampNewTransaction();

            if(options.isUseKnowledgeAgent()) {
                if(options.isLimitVehicleTwinCount()){
                    buildHIServiceInputsForAgentAndDispatch(
                            updatedVehicles.subList(0, Math.min(options.getMaxVehicleTwins(), updatedVehicles.size())),
                            syncCounterMin, currentDatabaseTimestamp);
                } else {
                    buildHIServiceInputsForAgentAndDispatch(updatedVehicles, syncCounterMin, currentDatabaseTimestamp);
                }
            } else {
                buildHIServiceInputsAndDispatch(updatedVehicles, syncCounterMin, options);
            }

            return UpdateInfo.UPDATE_STARTED;
        } catch (final Exception exception) {
            throw new OemHIException(exception);
        }
    }

    private void removeVehiclesWithMissingTelematicsData(@Nullable final List<Vehicle> updatedVehicles) {
        if(updatedVehicles != null) {
            updatedVehicles.removeIf(vehicle -> (vehicle.getNewestTelematicsData() == null));
        }
    }

    private void registerNewVehicles(@NotNull final List<Vehicle> updatedVehicles) throws BtpException {
        vehicleRegistrator.registerNewVehicles(updatedVehicles);
    }

    private void buildHIServiceInputsAndDispatch(@NotNull final List<Vehicle> updatedVehicles,
                                                 @NotNull final long syncCounterMin,
                                                 @NotNull final HIUpdateOptions options) throws BtpException {
        final String requestId = generateRequestId();
        final HIDataToSupplierContent dataToSupplierContent = hiInputDataBuilder.build(requestId, updatedVehicles);
        createNewCalculationInDatabase(requestId, syncCounterMin, getSyncCounterMax(updatedVehicles));
        dispatchRequestWithHttp(requestId, dataToSupplierContent, options);
    }

    private void buildHIServiceInputsForAgentAndDispatch(
            @NotNull final List<Vehicle> updatedVehicles, @NotNull final long syncCounterMin,
            @NotNull final Instant calculationTimestamp) throws BtpException {

        final String requestId = generateRequestId();
        final long sycCounterMax = getSyncCounterMax(updatedVehicles);
        final HIKAInputs agentInputs = hiInputDataBuilder.buildForAgent(requestId, updatedVehicles,
                sycCounterMax, calculationTimestamp);
        createNewCalculationInDatabase(requestId, syncCounterMin, sycCounterMax);
        dispatchRequestWithAgent(agentInputs);
    }

    private long getSyncCounterMax(@NotNull final List<Vehicle> updatedVehicles) {
        if(DataHelper.isNullOrEmpty(updatedVehicles)){
            return -1;
        }

        final Vehicle vehicleMaxSyncCounter = updatedVehicles.stream().max(
                Comparator.comparing(Vehicle::getSyncCounter)).get();

        return vehicleMaxSyncCounter.getSyncCounter();
    }

    @NotNull
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    private long getLastSyncCounterReady() throws OemHIException {
        List<HICalculation> calculations = hiCalculationTable.getByStatusOrderByCalculationSyncCounterNewTransaction(
                HICalculationStatus.READY);

        if(DataHelper.isNullOrEmpty(calculations)) {
            return 0;
        }

        return calculations.get(calculations.size() - 1).getCalculationSyncCounterMax();
    }

    private void createNewCalculationInDatabase(@NotNull final String requestId,
                                                @NotNull final long syncCounterMin,
                                                @NotNull final long syncCounterMax) throws OemHIException {
        hiCalculationTable.createNowNewTransaction(requestId, syncCounterMin, syncCounterMax);
    }

    private Notification<HIDataToSupplierContent> prepareNotification(
            @NotNull final String requestId, @NotNull final HIDataToSupplierContent hiDataToSupplierContent,
            @NotNull final HIUpdateOptions options) throws OemHIException {

        try {
            Notification<HIDataToSupplierContent> notification =
                    hiNotificationCreator.createForHttp(requestId, hiDataToSupplierContent);

            if (options.isLimitVehicleTwinCount()) {
                limitVehicles(notification, options.getMaxVehicleTwins());
            }

            return notification;
        } catch (final Exception exception) {
            setCalculationStatus(requestId, HICalculationStatus.FAILED_INTERNAL_BUILD_REQUEST);
            throw new OemHIException(exception);
        }
    }

    private void dispatchRequestWithHttp(@NotNull final String requestId,
                                         @NotNull final HIDataToSupplierContent hiDataToSupplierContent,
                                         @NotNull final HIUpdateOptions options) throws OemHIException {
        final Notification<HIDataToSupplierContent> notification = prepareNotification(
                requestId, hiDataToSupplierContent, options);

        logger.info("Request for id " + requestId + " (" + notification.getContent().getHealthIndicatorInputs().size()
                + " vehicles) prepared.");

        processResult(requestId,
                options.isRenameLoadSpectrumToLoadCollective()
                ? renameAndCallService(requestId, notification, options)
                : callService(requestId, notification, options));
    }

    private void dispatchRequestWithAgent(@NotNull final HIKAInputs agentInputs) {
        logger.info("Agent request for id " + agentInputs.getRequestId() + " (" + agentInputs.getRequests().size()
                + " vehicles) prepared.");
        callAgent(agentInputs);
    }

    private void processResult(@NotNull final String requestId, @NotNull final ResponseEntity<JsonNode> result)
            throws OemHIException {

        if (result.getStatusCode() == HttpStatus.OK
                    || result.getStatusCode() == HttpStatus.CREATED
                    || result.getStatusCode() == HttpStatus.ACCEPTED) {
            logger.info("External calculation service for id " + requestId + " started.");
            setCalculationStatus(requestId, HICalculationStatus.RUNNING);
        } else {
            serviceCallFailed("Starting external calculation service for id \" + requestId + \" failed: "
                    + "http code " + result.getStatusCode().toString() + ", response body: "
                    + result.getBody().toString());
        }
    }

    private ResponseEntity<JsonNode> renameAndCallService(
            @NotNull final String requestId, @NotNull final Notification<HIDataToSupplierContent> notification,
            @NotNull final HIUpdateOptions options) throws OemHIException {

        try {
            final String notificationAsString = objectMapper.writeValueAsString(
                    hiNotificationToSupplierConverter.toDAO(notification))
                    .replace("Spectrum", "Collective");

            return startAsyncRequest(requestId, supplierHiServiceEndpoint.toString(),
                    options.isUsePredefinedResults()? INPUT_ASSET_NAME_TEST_PREDEFINED : inputAssetId,
                    notificationAsString, JsonNode.class);
        } catch(final IOException exception) {
            setCalculationStatus(requestId, HICalculationStatus.FAILED_EXTERNAL);
            throw new OemHIException("Error while converting inputs to json!", exception);
        }
    }

    private ResponseEntity<JsonNode> callService(
            @NotNull final String requestId, @NotNull final Notification<HIDataToSupplierContent> notification,
            @NotNull final HIUpdateOptions options) throws OemHIException {
        return startAsyncRequest(requestId, supplierHiServiceEndpoint.toString(),
                options.isUsePredefinedResults()? INPUT_ASSET_NAME_TEST_PREDEFINED : inputAssetId,
                hiNotificationToSupplierConverter.toDAO(notification), JsonNode.class);
    }

    private ResponseEntity<JsonNode> callAgent(@NotNull final HIKAInputs agentInputs) {

        final ResponseEntity<DefaultApiResult> result = knowledgeAgentTest.run(agentInputs);
        final ResponseEntity<JsonNode> response = new ResponseEntity<>(objectMapper.valueToTree(result.getBody()),
                result.getHeaders(), result.getStatusCode());
        return response;
    }

    private void serviceCallFailed(@NotNull final String errorText) throws OemHIException {
        logger.error(errorText);
        throw new OemHIException(errorText);
    }

    private void limitVehicles(@NotNull final Notification<HIDataToSupplierContent> notificationInOut,
                               @NotNull final int maxVehicleTwins) {
        if(maxVehicleTwins < notificationInOut.getContent().getHealthIndicatorInputs().size()) {
            List<HealthIndicatorInput> limitedInputs =
                    notificationInOut.getContent().getHealthIndicatorInputs().subList(0, maxVehicleTwins);
            notificationInOut.getContent().setHealthIndicatorInputs(limitedInputs);
        }
    }

    private void setCalculationStatus(@NotNull final String requestId, @NotNull final HICalculationStatus newStatus)
            throws OemHIException {
        hiCalculationTable.updateStatusNewTransaction(requestId, newStatus);
    }

    public <BodyType, ResponseType> ResponseEntity<ResponseType> startAsyncRequest(
            @NotNull final String requestId, @NotNull final String endpoint, @NotNull final String asset,
            @NotNull final BodyType messageBody, @NotNull Class<ResponseType> responseTypeClass) throws OemHIException {

        try {
            return edcApi.post(HttpUrl.parse(endpoint), asset, responseTypeClass,
                    messageBody, generateDefaultHeaders());
        } catch (final EdcException exception) {
            setCalculationStatus(requestId, HICalculationStatus.FAILED_EXTERNAL);
            throw new OemHIException(exception);
        }
    }

    protected HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}