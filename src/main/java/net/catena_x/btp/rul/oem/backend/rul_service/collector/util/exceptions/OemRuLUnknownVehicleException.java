package net.catena_x.btp.rul.oem.backend.rul_service.collector.util.exceptions;

import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class OemRuLUnknownVehicleException extends OemRuLException {
    public OemRuLUnknownVehicleException() {
        super();
    }

    public OemRuLUnknownVehicleException(@Nullable final String message) {
        super(message);
    }

    public OemRuLUnknownVehicleException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public OemRuLUnknownVehicleException(@Nullable final Throwable cause) {
        super(cause);
    }

    protected OemRuLUnknownVehicleException(@Nullable final String message, @Nullable final Throwable cause,
                                            @NotNull final boolean enableSuppression,
                                            @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}