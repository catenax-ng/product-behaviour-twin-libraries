package net.catena_x.btp.hi.oem.frontend.rest.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import net.catena_x.btp.hi.oem.backend.hi_service.receiver.HIResultProcessor;
import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicleTable;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicle;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicleConverter;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.statistics.HIFHistogram;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.statistics.HIFHistogramConverter;
import net.catena_x.btp.hi.oem.frontend.rest.controller.swagger.StatisticHealthStatesDistributionTypeDoc;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(HIFrontendApiConfig.API_PATH_BASE)
public class HIFrontendControllerStatisticHealthStatesDistributionType {
    @Autowired ApiHelper apiHelper;
    @Autowired HIFVehicleConverter hifVehicleConverter;
    @Autowired HIVehicleTable hiVehicleTable;
    @Autowired HIFHistogramConverter hifHistogramConverter;
    @Autowired HIHistogramGenerator hiHistogramGenerator;

    private final Logger logger = LoggerFactory.getLogger(HIResultProcessor.class);

    @GetMapping(value = "/statistic/healthstates/distribution/{type}", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = StatisticHealthStatesDistributionTypeDoc.SUMMARY,
            tags = {"Productive"},
            description = StatisticHealthStatesDistributionTypeDoc.DESCRIPTION,
            parameters = {@io.swagger.v3.oas.annotations.Parameter(
                    in = ParameterIn.PATH, name = "type",
                    description = StatisticHealthStatesDistributionTypeDoc.TYPE_DESCRIPTION, required = true,
                    examples = {
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = StatisticHealthStatesDistributionTypeDoc.TYPE_EXAMPLE_1_NAME,
                                    description = StatisticHealthStatesDistributionTypeDoc.TYPE_EXAMPLE_1_DESCRIPTION,
                                    value = StatisticHealthStatesDistributionTypeDoc.TYPE_EXAMPLE_1_VALUE
                            ),
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = StatisticHealthStatesDistributionTypeDoc.TYPE_EXAMPLE_2_NAME,
                                    description = StatisticHealthStatesDistributionTypeDoc.TYPE_EXAMPLE_2_DESCRIPTION,
                                    value = StatisticHealthStatesDistributionTypeDoc.TYPE_EXAMPLE_2_VALUE
                            )
                    }
            )},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = StatisticHealthStatesDistributionTypeDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = StatisticHealthStatesDistributionTypeDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = HIFHistogram.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = StatisticHealthStatesDistributionTypeDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = StatisticHealthStatesDistributionTypeDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<String> getDistribution(@PathVariable @NotNull final String type) {
        try {
            final List<HIFVehicle> vehicles = hifVehicleConverter.toDAO(
                    hiVehicleTable.getAllWithHealthIndicatorsNewTransaction());
            return apiHelper.okAsString(hifHistogramConverter.toDAO(
                    switch (type) {
                        case "loadspectra" ->
                                hiHistogramGenerator.generateHistogramHealthStatesLoadSpectra(vehicles);
                        case "adaptionvalues" ->
                                hiHistogramGenerator.generateHistogramHealthStatesAdaptionValues(vehicles);
                        default -> throw new OemHIException("Invalid type " + type);
                    }),
                    HIFHistogram.class);
        } catch (final OemHIException exception) {
            logger.error(exception.getMessage());
            return apiHelper.failedAsString(exception.getMessage());
        }
    }
}
