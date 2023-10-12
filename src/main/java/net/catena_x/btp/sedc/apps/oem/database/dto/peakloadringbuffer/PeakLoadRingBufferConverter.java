package net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.sedc.apps.oem.database.tables.peakloadringbuffer.PeakLoadRingBufferElementDAO;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.model.rawvalues.PowerClassesNormalized;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class PeakLoadRingBufferConverter extends DAOConverter<PeakLoadRingBufferElementDAO, PeakLoadRingBufferElement> {
    protected PeakLoadRingBufferElement toDTOSourceExists(@NotNull final PeakLoadRingBufferElementDAO source) {
        return new PeakLoadRingBufferElement(source.getId(), source.getTimestamp(),
                                  new PeakLoadRawValues(source.getDataCollectionPeriodInMs(),
                                          new PowerClassesNormalized(
                                                  source.getRecuperation(),
                                                  source.getLowTorqueLowRevolutions(),
                                                  source.getLowTorqueHighRevolutions(),
                                                  source.getHighTorqueLowRevolutions(),
                                                  source.getHighTorqueHighRevolutions(),
                                                  source.getPeakLoad()),
                                          source.getAverageEnvironmentTemperatureInC(),
                                          source.getStateOfChargeAtStartNormalized(),
                                          source.getStateOfChargeAtEndNormalized()),
                                  (source.getPeakLoadCapability() == null) ? null :
                                          new PeakLoadResult(source.getPeakLoadCapability()));
    }

    protected PeakLoadRingBufferElementDAO toDAOSourceExists(@NotNull final PeakLoadRingBufferElement source) {
        return new PeakLoadRingBufferElementDAO(source.getId(), source.getTimestamp(),
                source.getRawValues().getDataCollectionPeriodInMs(),
                source.getRawValues().getPowerClassesNormalized().getRecuperation(),
                source.getRawValues().getPowerClassesNormalized().getLowTorqueLowRevolutions(),
                source.getRawValues().getPowerClassesNormalized().getLowTorqueHighRevolutions(),
                source.getRawValues().getPowerClassesNormalized().getHighTorqueLowRevolutions(),
                source.getRawValues().getPowerClassesNormalized().getHighTorqueHighRevolutions(),
                source.getRawValues().getPowerClassesNormalized().getPeakLoad(),
                source.getRawValues().getAverageEnvironmentTemperatureInC(),
                source.getRawValues().getStateOfChargeAtStartNormalized(),
                source.getRawValues().getStateOfChargeAtEndNormalized(),
                (source.getResult() == null) ? null : source.getResult().getPeakLoadCapability());
    }
}