package net.catena_x.btp.rul.oem.backend.rul_service.collector.util;

import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.LoadSpectrumType;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import net.catena_x.btp.rul.oem.backend.rul_service.collector.util.exceptions.OemRuLLoadSpectrumNotFoundException;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.RuLDataToSupplierContent;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items.RuLInput;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
public class RuLInputDataBuilder {
    public RuLDataToSupplierContent build(@NotNull final String supplierNotificationId,
                                          @NotNull final Vehicle vehicle) throws OemRuLException {
        final RuLInput rulInput = getRulInput(vehicle);
        if(rulInput == null) {
            return null;
        }

        final List<RuLInput> rulInputs = new ArrayList<>();
        rulInputs.add(rulInput);

        return new RuLDataToSupplierContent(supplierNotificationId, rulInputs);
    }

    private RuLInput getRulInput(@NotNull final Vehicle vehicle) throws OemRuLException {
        if (vehicle.getNewestTelematicsData() == null) {
            return null;
        }

        final List<ClassifiedLoadSpectrum> loadSpectra = vehicle.getNewestTelematicsData().getLoadSpectra();
        final RuLInput rulInput = new RuLInput(
                vehicle.getGearboxId(),
                findLoadSpectrumByType(loadSpectra, LoadSpectrumType.GEAR_SET),
                findLoadSpectrumByType(loadSpectra, LoadSpectrumType.GEAR_OIL));

        return rulInputIfOk(rulInput);
    }

    private RuLInput rulInputIfOk(final RuLInput rulInput) throws OemRuLException {
        final boolean loadSpectrumGearSetMissing = (rulInput.getClassifiedLoadSpectrumGearSet() == null);
        final boolean loadSpectrumGearOilMissing = (rulInput.getClassifiedLoadSpectrumGearOil() == null);

        if(loadSpectrumGearSetMissing && loadSpectrumGearOilMissing) {
            return null;
        } else if(loadSpectrumGearSetMissing || loadSpectrumGearOilMissing) {
            throw new OemRuLLoadSpectrumNotFoundException("Load spectrum for gear "
                                                                + (loadSpectrumGearSetMissing? "set" :"oil" )
                                                                + " not included in telematics data!");
        }

        return rulInput;
    }

    private ClassifiedLoadSpectrum findLoadSpectrumByType(
            @NotNull final List<ClassifiedLoadSpectrum> loadSpectra,
            @NotNull final LoadSpectrumType componentDescription) throws OemRuLException {

        for (final ClassifiedLoadSpectrum loadSpectrum: loadSpectra) {
            if(loadSpectrum.getMetadata().getComponentDescription() == componentDescription) {
                return loadSpectrum;
            }
        }

        throw new OemRuLLoadSpectrumNotFoundException("Load spectrum with required type not found!");
    }
}
