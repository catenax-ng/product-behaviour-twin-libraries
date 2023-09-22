package net.catena_x.btp.hi.oem.frontend.rest.controller;

import net.catena_x.btp.hi.oem.backend.hi_service.receiver.HIResultProcessor;
import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicleTable;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicle;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicleConverter;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.statistics.HIFHealthIndicatorDistributions;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.statistics.HIFHealthIndicatorDistributionsConverter;
import net.catena_x.btp.hi.oem.frontend.model.dto.statistics.HIHealthIndicatorDistributions;
import net.catena_x.btp.hi.oem.frontend.rest.controller.swagger.StatisticHealthStatesDistributionsDoc;
import net.catena_x.btp.hi.oem.frontend.rest.controller.util.HIHistogramGenerator;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(HIFrontendApiConfig.API_PATH_BASE)
public class HIFrontendControllerStatisticHealthStatesDistributions {
    @Autowired ApiHelper apiHelper;
    @Autowired HIFVehicleConverter hifVehicleConverter;
    @Autowired HIVehicleTable hiVehicleTable;
    @Autowired HIFHealthIndicatorDistributionsConverter hifHealthIndicatorDistributionsConverter;
    @Autowired HIHistogramGenerator hiHistogramGenerator;

    private final Logger logger = LoggerFactory.getLogger(HIResultProcessor.class);

    @GetMapping(value = "/statistic/healthstates/distributions", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = StatisticHealthStatesDistributionsDoc.SUMMARY,
            tags = {"Productive"},
            description = StatisticHealthStatesDistributionsDoc.DESCRIPTION,
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = StatisticHealthStatesDistributionsDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = StatisticHealthStatesDistributionsDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = HIFHealthIndicatorDistributions.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = StatisticHealthStatesDistributionsDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = StatisticHealthStatesDistributionsDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<String> getDistributions() {
        try {
            final List<HIFVehicle> vehicles = hifVehicleConverter.toDAO(
                    hiVehicleTable.getAllWithHealthIndicatorsNewTransaction());
            final HIHealthIndicatorDistributions distributions = new HIHealthIndicatorDistributions();
            distributions.setHistogramLoadSpectra(
                    hiHistogramGenerator.generateHistogramHealthStatesLoadSpectra(vehicles));
            distributions.setHistogramAdaptionValues(
                    hiHistogramGenerator.generateHistogramHealthStatesAdaptionValues(vehicles));
            return apiHelper.okAsString(hifHealthIndicatorDistributionsConverter.toDAO(distributions),
                    HIFHealthIndicatorDistributions.class);
        } catch (final OemHIException exception) {
            logger.error(exception.getMessage());
            return apiHelper.failedAsString(exception.getMessage());
        }
    }
}
