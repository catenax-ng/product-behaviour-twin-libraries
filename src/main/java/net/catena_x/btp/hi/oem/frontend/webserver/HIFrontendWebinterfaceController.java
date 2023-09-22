package net.catena_x.btp.hi.oem.frontend.webserver;

import net.catena_x.btp.hi.oem.backend.hi_service.receiver.HIResultProcessor;
import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicleTable;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicle;
import net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.HIFVehicleConverter;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;

@Controller
public class HIFrontendWebinterfaceController {
    @Autowired private HIFVehicleConverter hifVehicleConverter;
    @Autowired private HIVehicleTable hiVehicleTable;

    private final Logger logger = LoggerFactory.getLogger(HIResultProcessor.class);

    @GetMapping({"/", "/index.html"})
    public String start(@NotNull final Model model) {
        try {
            logger.info("Requested start page.");

            final List<HIFVehicle> resultListConverted = hifVehicleConverter.toDAO(
                hiVehicleTable.getAllWithHealthIndicatorsNewTransaction());
            resultListConverted.sort(Comparator.comparing(HIFVehicle::getVehicleId));

            // set model attribute
            model.addAttribute("hiVehicles", resultListConverted);
            return "index";
        } catch (final OemHIException exception) {
            logger.error(exception.getMessage());
            return "error";
        }
    }

    @GetMapping("/vehicle.html")
    public String vehicle(@RequestParam("id") String vehicleId,
                          @NotNull final Model model) {
        try {
            logger.info("Requested vehicle with id=" + vehicleId + ".");

            final HIFVehicle resultConverted = hifVehicleConverter.toDAO(
                    hiVehicleTable.getByIdWithHealthIndicatorsNewTransaction(vehicleId));

            if(resultConverted == null) {
                throw new OemHIException("Vehicle with id " + vehicleId + " not found!");
            }

            // set model attribute
            model.addAttribute("hiVehicle", resultConverted);
            return "vehicle";
        } catch (final OemHIException exception) {
            logger.error(exception.getMessage());
            return "error";
        }
    }
}
