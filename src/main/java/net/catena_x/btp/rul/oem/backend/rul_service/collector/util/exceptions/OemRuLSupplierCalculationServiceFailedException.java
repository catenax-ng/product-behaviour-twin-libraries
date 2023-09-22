package net.catena_x.btp.rul.oem.backend.rul_service.collector.util.exceptions;

import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class OemRuLSupplierCalculationServiceFailedException extends OemRuLException {
    public OemRuLSupplierCalculationServiceFailedException() {
        super();
    }

    public OemRuLSupplierCalculationServiceFailedException(@Nullable final String message) {
        super(message);
    }

    public OemRuLSupplierCalculationServiceFailedException(@Nullable final String message,
                                                           @Nullable final Throwable cause) {
        super(message, cause);
    }

    public OemRuLSupplierCalculationServiceFailedException(@Nullable final Throwable cause) {
        super(cause);
    }

    protected OemRuLSupplierCalculationServiceFailedException(@Nullable final String message,
                                                              @Nullable final Throwable cause,
                                                              @NotNull final boolean enableSuppression,
                                                              @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}