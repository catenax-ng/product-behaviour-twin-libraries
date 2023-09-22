package net.catena_x.btp.hi.supplier.mockup.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.hi.oem.backend.hi_service.collector.HIDataCollector;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationFromSupplierContentDAO;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationToSupplierContentDAO;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.items.AdaptionValuesListDAO;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.items.HealthIndicatorInputDAO;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.items.HealthIndicatorOutputDAO;
import net.catena_x.btp.hi.supplier.mockup.controller.swagger.SupplierMockUpDoc;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.notification.dto.items.NotificationHeader;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.abs;

@RestController
public class HIServiceControllerSupplierMock {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RestTemplate restTemplate;

    final double[] ADAPTION_VALUES_LIMITS = {100, 255, 100, 255};
    final double GREEN_YELLOW_TRESHOLD = 0.8;
    final double YELLOW_RED_TRESHOLD = 0.95;

    final double GREEN = 0.0;
    final double YELLOW = 0.95;
    final double RED = 1.1;

    final double[] GREEN_GREEN = {GREEN, GREEN};
    final double[] GREEN_YELLOW = {GREEN, YELLOW};
    final double[] GREEN_RED = {GREEN, RED};
    final double[] YELLOW_GREEN = {YELLOW, GREEN};
    final double[] YELLOW_YELLOW = {YELLOW, YELLOW};
    final double[] YELLOW_RED = {YELLOW, RED};
    final double[] RED_GREEN = {RED, GREEN};
    final double[] RED_YELLOW = {RED, YELLOW};
    final double[] RED_RED = {RED, RED};

    final double[][] healthIndicatorValues1 = {
            GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN,
            RED_YELLOW, GREEN_GREEN, GREEN_YELLOW, GREEN_GREEN, GREEN_GREEN,
            GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_YELLOW, GREEN_YELLOW,
            GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN,
            GREEN_GREEN, YELLOW_GREEN, GREEN_GREEN, YELLOW_GREEN, GREEN_GREEN,
            GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN};

    final double[][] healthIndicatorValues2 = {
            GREEN_GREEN, YELLOW_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN,
            RED_RED, GREEN_GREEN, GREEN_YELLOW, GREEN_GREEN, GREEN_GREEN,
            GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_YELLOW, GREEN_YELLOW,
            GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_YELLOW, GREEN_YELLOW,
            GREEN_GREEN, YELLOW_GREEN, GREEN_GREEN, YELLOW_GREEN, GREEN_RED,
            GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN, GREEN_GREEN};

    private boolean useHiValues1 = true;
    private synchronized boolean isHiValues1() {
        useHiValues1 = !useHiValues1;
        return !useHiValues1;
    }

    @PostMapping(value = "api/service/{assetId}/submodel", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = SupplierMockUpDoc.SUMMARY, description = SupplierMockUpDoc.DESCRIPTION,
            tags = {"MockUp"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            in = ParameterIn.PATH, name = "assetId",
                            description = SupplierMockUpDoc.ASSETID_DESCRIPTION, required = true,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = SupplierMockUpDoc.ASSETID_EXAMPLE_1_NAME,
                                            description = SupplierMockUpDoc.ASSETID_EXAMPLE_1_DESCRIPTION,
                                            value = SupplierMockUpDoc.ASSETID_EXAMPLE_1_VALUE
                                    )
                            }
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            in = ParameterIn.QUERY, name = "provider-connector-url",
                            description = SupplierMockUpDoc.PROVIDERCONNECTORURL_DESCRIPTION, required = true,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = SupplierMockUpDoc.PROVIDERCONNECTORURL_EXAMPLE_1_NAME,
                                            description = SupplierMockUpDoc.PROVIDERCONNECTORURL_EXAMPLE_1_DESCRIPTION,
                                            value = SupplierMockUpDoc.PROVIDERCONNECTORURL_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = SupplierMockUpDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = SupplierMockUpDoc.BODY_EXAMPLE_1_NAME,
                                            description = SupplierMockUpDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = SupplierMockUpDoc.BODY_EXAMPLE_1_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "202",
                            description = SupplierMockUpDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = SupplierMockUpDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = SupplierMockUpDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = SupplierMockUpDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> runHICalculationMock(
            @RequestBody @NotNull NotificationDAO<HINotificationToSupplierContentDAO> data,
            @PathVariable @NotNull final String assetId,
            @RequestParam(required = true, name="provider-connector-url") @Nullable String providerConnectorUrl) {

        final List<HealthIndicatorInputDAO> healthIndicatorInputs = getInputsSorted(data.getContent());

        if(assetId.equals(HIDataCollector.INPUT_ASSET_NAME_TEST_PREDEFINED)) {
            return processPredefinedAlternating(data.getContent().getRequestRefId(), healthIndicatorInputs);
        } else {
            return processByInputs(data.getContent().getRequestRefId(), healthIndicatorInputs);
        }
    }

    public ResponseEntity<HINotificationFromSupplierContentDAO> runHICalculationMockSynchron(
            @NotNull final HINotificationToSupplierContentDAO data) {
        return processByInputsSynchron(data.getRequestRefId(), getInputsSorted(data));
    }

    private List<HealthIndicatorInputDAO> getInputsSorted(@NotNull final HINotificationToSupplierContentDAO data) {
        final List<HealthIndicatorInputDAO> healthIndicatorInputs = data.getHealthIndicatorInputs();
        healthIndicatorInputs.sort(Comparator.comparing(HealthIndicatorInputDAO::getComponentId));
        return healthIndicatorInputs;
    }

    private ResponseEntity<DefaultApiResult> processPredefinedAlternating(
            @NotNull final String refId, @NotNull final List<HealthIndicatorInputDAO> healthIndicatorInputs) {
        final int count = healthIndicatorInputs.size();
        final List<HealthIndicatorOutputDAO> outputs = new ArrayList<>(count);
        final double[][] healthIndicatorValues = isHiValues1() ? healthIndicatorValues1 : healthIndicatorValues2;

        for (int i = 0; i < count; i++) {
            final HealthIndicatorInputDAO inputData = healthIndicatorInputs.get(i);
            outputs.add(new HealthIndicatorOutputDAO("DV_0.0.99", inputData.getComponentId(),
                    (i < healthIndicatorValues.length) ? healthIndicatorValues[i] : GREEN_GREEN));
        }

        return buildAndSendNotification(refId, outputs);
    }

    private ResponseEntity<DefaultApiResult> processByInputs(
            @NotNull final String refId, @NotNull final List<HealthIndicatorInputDAO> healthIndicatorInputs) {
        return buildAndSendNotification(refId, generateOutputsByInputs(healthIndicatorInputs));
    }

    private ResponseEntity<HINotificationFromSupplierContentDAO> processByInputsSynchron(
            @NotNull final String refId, @NotNull final List<HealthIndicatorInputDAO> healthIndicatorInputs) {
        final HINotificationFromSupplierContentDAO result = new HINotificationFromSupplierContentDAO();
        result.setRequestRefId(refId);
        result.setHealthIndicatorOutputs(generateOutputsByInputs(healthIndicatorInputs));
        return apiHelper.ok(result);
    }

    private List<HealthIndicatorOutputDAO> generateOutputsByInputs(
            @NotNull final List<HealthIndicatorInputDAO> healthIndicatorInputs) {

        final int count = healthIndicatorInputs.size();
        final List<HealthIndicatorOutputDAO> outputs = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            final HealthIndicatorInputDAO inputData = healthIndicatorInputs.get(i);
            outputs.add(new HealthIndicatorOutputDAO("DV_0.0.99", inputData.getComponentId(),
                    new double[]{calculateLoadSpectrumIndicator(inputData.getClassifiedLoadSpectrum()),
                            calculateAdaptionValuesIndicator(inputData.getAdaptionValuesList())}));
        }

        return outputs;
    }

    private double calculateLoadSpectrumIndicator(@NotNull final ClassifiedLoadSpectrum loadSpectrum) {
        final double refValue = loadSpectrum.getBody().getCounts().getCountsList()[0];

        if(refValue < 1500) {
            return GREEN;
        } else if(refValue < 6000) {
            return YELLOW;
        } else {
            return RED;
        }
    }

    private double calculateAdaptionValuesIndicator(@NotNull final AdaptionValuesListDAO adaptionValuesList) {
        assert(adaptionValuesList.getValues().length == ADAPTION_VALUES_LIMITS.length);

        boolean yellow = false;
        for (int i = 0; i < ADAPTION_VALUES_LIMITS.length; i++) {
            double rel = abs(adaptionValuesList.getValues()[i]) / ADAPTION_VALUES_LIMITS[i];
            if(rel > YELLOW_RED_TRESHOLD) {
                return RED;
            } else if(rel > GREEN_YELLOW_TRESHOLD) {
                yellow = true;
            }
        }

        return yellow? YELLOW : GREEN;
    }

    private ResponseEntity<DefaultApiResult> buildAndSendNotification(@NotNull final String refId,
                                                                      @NotNull final List<HealthIndicatorOutputDAO> outputs)
    {
        final Notification<HINotificationFromSupplierContentDAO> notification = new Notification<>();
        notification.setHeader(new NotificationHeader());
        notification.getHeader().setReferencedNotificationID(refId);

        notification.setContent(new HINotificationFromSupplierContentDAO());
        notification.getContent().setRequestRefId(refId);
        notification.getContent().setHealthIndicatorOutputs(outputs);

        new Thread(() ->
        {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException exception) {
            }

            final HttpUrl requestUrl = HttpUrl.parse("http://localhost:25553/")
                    .newBuilder().addPathSegment("hidatareceiver").addPathSegment("notifyresult").build();

            final HttpHeaders headers = generateDefaultHeaders();

            final HttpEntity<Notification<HINotificationFromSupplierContentDAO>> request =
                    new HttpEntity<>(notification, headers);

            final ResponseEntity<DefaultApiResult> response = restTemplate.postForEntity(
                    requestUrl.toString(), request, DefaultApiResult.class);
        }).start();

        return apiHelper.accepted("Accepted.");
    }

    private HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
