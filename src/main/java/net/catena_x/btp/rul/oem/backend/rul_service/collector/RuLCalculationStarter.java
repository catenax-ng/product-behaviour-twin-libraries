package net.catena_x.btp.rul.oem.backend.rul_service.collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.edc.EdcApi;
import net.catena_x.btp.libraries.edc.model.EdcAssetAddress;
import net.catena_x.btp.libraries.edc.util.exceptions.EdcException;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.rul.oem.backend.model.dto.calculation.RuLCalculationTable;
import net.catena_x.btp.rul.oem.backend.model.dto.vinrelation.RuLVinRelation;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLCalculationStatus;
import net.catena_x.btp.rul.oem.backend.rul_service.collector.util.RuLInputDataBuilder;
import net.catena_x.btp.rul.oem.backend.rul_service.collector.util.RuLSupplierNotificationCreator;
import net.catena_x.btp.rul.oem.backend.rul_service.collector.util.exceptions.*;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.requester.RuLNotificationFromRequesterContent;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.RuLDataToSupplierContent;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.RuLNotificationToSupplierConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.util.RuLStarterApiResult;
import net.catena_x.btp.rul.oem.backend.util.enums.RuLServiceOptionHelper;
import net.catena_x.btp.rul.oem.backend.util.enums.RuLStarterCalculationType;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Component
public class RuLCalculationStarter {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RuLNotificationToSupplierConverter rulNotificationToSupplierConverter;
    @Autowired private RuLSupplierNotificationCreator rulSupplierNotificationCreator;
    @Autowired private RuLVinToIdConverter rulVinToIdConverter;
    @Autowired private VehicleTable vehicleTable;
    @Autowired private RuLInputDataBuilder rulInputDataBuilder;
    @Autowired private RuLCalculationTable rulCalculationTable;
    @Autowired private EdcApi edcApi;
    @Autowired private RuLServiceOptionHelper rulServiceOptionHelper;
    @Autowired private ObjectMapper objectMapper;

    @Value("${supplier.rulservice.inputAssetId}") private String inputAssetId;
    @Value("${supplier.rulservice.endpoint}") private URL supplierRuLServiceEndpoint;

    private final Logger logger = LoggerFactory.getLogger(RuLCalculationStarter.class);

    public ResponseEntity<RuLStarterApiResult> startCalculation(
            @NotNull final String requesterNotificationId, @NotNull final EdcAssetAddress requesterAssetAddress,
            @NotNull final RuLNotificationFromRequesterContent requesterNotificationContent) {

        try {
            final String vin = requesterNotificationContent.getVin();
            final Vehicle vehicle = getVehicleByVIN(vin);
            if(vehicle == null) {
                return failed(RuLStarterCalculationType.NO_DATA_FOR_VEHICLE,
                        "Vehicle with vin " + vin + " has no data for RuL calculation.");
            }

            final String supplierNotificationId = generateRequestId();
            final RuLDataToSupplierContent dataToSupplierContent =
                    rulInputDataBuilder.build(supplierNotificationId, vehicle);

            if(dataToSupplierContent == null) {
                throw noDataForVehicle(requesterNotificationContent.getVin());
            }

            createNewCalculationInDatabase(supplierNotificationId, requesterNotificationId, requesterAssetAddress);
            dispatchRequestWithHttp(supplierNotificationId, dataToSupplierContent);
        } catch(final OemRuLLoadSpectrumNotFoundException exception) {
            return failed(RuLStarterCalculationType.REQUIRED_LOAD_SPECTRUM_TYPE_NOT_FOUND, exception.getMessage());
        } catch(final OemRuLNoDataForVehicleException exception) {
            return failed(RuLStarterCalculationType.NO_DATA_FOR_VEHICLE, exception.getMessage());
        } catch(final OemRuLNoVinGivenException exception) {
            return failed(RuLStarterCalculationType.NO_VIN_GIVEN, exception.getMessage());
        } catch(final OemRuLSupplierCalculationServiceFailedException exception) {
            return failed(RuLStarterCalculationType.SUPPLIER_CALCULATION_SERVICE_FAILED, exception.getMessage());
        } catch(final OemRuLUnknownVehicleException exception) {
            return failed(RuLStarterCalculationType.UNKNOWN_VEHICLE, exception.getMessage());
        } catch(final Exception exception) {
            return failed(RuLStarterCalculationType.INTERNAL_ERROR, exception.getMessage());
        }

        return ok(RuLStarterCalculationType.OK_SUPPLIER_CALCULATION_SERVICE_STARTED,
                "Started RuL calculation successfully.");
    }

    @NotNull
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    private OemRuLNoDataForVehicleException noDataForVehicle(@NotNull final String vin) {
        return new OemRuLNoDataForVehicleException("Vehicle with vin " + vin
                                                            + " has no data for RuL calculation.");
    }

    private ResponseEntity<RuLStarterApiResult> ok(final RuLStarterCalculationType type, final String info) {
        return apiHelper.accepted(new RuLStarterApiResult(Instant.now(), type, info));
    }

    private ResponseEntity<RuLStarterApiResult> failed(final RuLStarterCalculationType type, final String info) {
        return apiHelper.ok(new RuLStarterApiResult(Instant.now(), type,
                    "Starting RuL calculation failed: " + info));
    }

    private Vehicle getVehicleByVIN(final String vin) throws OemRuLException {
        try {
            final RuLVinRelation relation = getVinRelationIdByVIN(vin);
            if(relation.isNoData()) {
                return null;
            }

            final Vehicle vehicle = vehicleTable.getByIdWithTelematicsDataNewTransaction(relation.getRefId());
            checkVehicleFound(vehicle, vin);
            return vehicle;
        } catch(final OemDatabaseException exception) {
            throw new OemRuLException(exception.getMessage(), exception);
        }
    }

    private RuLVinRelation getVinRelationIdByVIN(final String vin) throws OemRuLException {
        assertVIN(vin);
        return rulVinToIdConverter.getByVin(vin);
    }

    private void checkVehicleFound(final Vehicle vehicle, final String vin) throws OemRuLException {
        if(vehicle == null) {
            throw new OemRuLUnknownVehicleException("No vehicle found to corresponding vin " + vin);
        }
    }

    private void assertVIN(final String vin) throws OemRuLException {
        if(vin == null) {
            throw new OemRuLNoVinGivenException("No vin given!");
        }
    }

    private void createNewCalculationInDatabase(
            @NotNull final String supplierNotificationId, @NotNull final String requesterNotificationId,
            @NotNull final EdcAssetAddress requesterAssetAddress) throws OemRuLException {
        rulCalculationTable.createNowNewTransaction(supplierNotificationId, requesterNotificationId,
                requesterAssetAddress);
    }

    private void dispatchRequestWithHttp(@NotNull final String supplierNotificationId,
                                         @NotNull final RuLDataToSupplierContent rulDataToSupplierContent)
            throws OemRuLException {
        final Notification<RuLDataToSupplierContent> notification = prepareNotification(
                supplierNotificationId, rulDataToSupplierContent);

        logger.info("Request for id " + supplierNotificationId + " prepared.");

        processResult(supplierNotificationId, callService(supplierNotificationId, notification));
    }

    private Notification<RuLDataToSupplierContent> prepareNotification(
            @NotNull final String supplierNotificationId,
            @NotNull final RuLDataToSupplierContent rulDataToSupplierContent) throws OemRuLException {

        try {
            return rulSupplierNotificationCreator.createForHttp(supplierNotificationId, rulDataToSupplierContent);
        } catch (final Exception exception) {
            setCalculationStatus(supplierNotificationId,
                    RuLCalculationStatus.FAILED_INTERNAL_BUILD_CALCULATION_REQUEST);
            throw new OemRuLException(exception);
        }
    }

    private void setCalculationStatus(@NotNull final String supplierNotificationId,
                                      @NotNull final RuLCalculationStatus newStatus) throws OemRuLException {
        rulCalculationTable.updateStatusNewTransaction(supplierNotificationId, newStatus);
    }

    private void processResult(@NotNull final String supplierNotificationId,
                               @NotNull final ResponseEntity<JsonNode> result)
            throws OemRuLException {

        if (result.getStatusCode() == HttpStatus.OK
                || result.getStatusCode() == HttpStatus.CREATED
                || result.getStatusCode() == HttpStatus.ACCEPTED) {
            logger.info("External calculation service for id " + supplierNotificationId + " started.");
            setCalculationStatus(supplierNotificationId, RuLCalculationStatus.RUNNING);
        } else {
            throw new OemRuLSupplierCalculationServiceFailedException(
                        "Starting external calculation service for id " + supplierNotificationId + " failed: "
                                    + "http code " + result.getStatusCode().toString() + ", response body: "
                                    + result.getBody().toString());
        }
    }

    private ResponseEntity<JsonNode> callService(@NotNull final String supplierNotificationId,
            @NotNull final Notification<RuLDataToSupplierContent> notification) throws OemRuLException {

        try {
            if(rulServiceOptionHelper.isShowInputToSupplier()) {
                System.out.println("=======================");
                System.out.println("RuL input to supplier:");
                System.out.println(objectMapper.writeValueAsString(notification));
                System.out.println("=======================");
            }
        } catch (final Exception exception) {
            logger.error("Input to supplier can not be mocked: " + exception.getMessage());
        }

        return startAsyncRequest(supplierNotificationId, supplierRuLServiceEndpoint.toString(), inputAssetId,
                rulNotificationToSupplierConverter.toDAO(notification), JsonNode.class);
    }

    public <BodyType, ResponseType> ResponseEntity<ResponseType> startAsyncRequest(
            @NotNull final String supplierNotificationId, @NotNull final String endpoint, @NotNull final String asset,
            @NotNull final BodyType messageBody, @NotNull Class<ResponseType> responseTypeClass)
            throws OemRuLException {

        try {
            return edcApi.post(HttpUrl.parse(endpoint), asset, responseTypeClass,
                    messageBody, generateDefaultHeaders());
        } catch (final EdcException exception) {
            setCalculationStatus(supplierNotificationId, RuLCalculationStatus.FAILED_EXTERNAL);
            throw new OemRuLSupplierCalculationServiceFailedException(exception);
        }
    }

    protected HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
