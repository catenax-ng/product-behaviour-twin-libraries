package net.catena_x.btp.sedc.apps.supplier.calculation.service;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.PeakLoadResult;

import javax.validation.constraints.NotNull;

public class PeakLoadCalculation implements PeakLoadCalculationInterface {
    public PeakLoadResult calculate(@NotNull final PeakLoadRawValues rawValues) throws BtpException  {
        final PeakLoadResult result = new PeakLoadResult();

        try {
            Thread.sleep(2345L);
        } catch (final InterruptedException exception) {
        }

        result.setPeakLoadCapability(1L);
        return result;
    }
}
