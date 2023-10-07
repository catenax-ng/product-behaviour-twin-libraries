package net.catena_x.btp.sedc.apps.supplier.calculation.service;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.PeakLoadResult;

import javax.validation.constraints.NotNull;

public interface PeakLoadCalculationInterface {
    PeakLoadResult calculate(@NotNull final PeakLoadRawValues rawValues) throws BtpException;
}
