package net.catena_x.btp.rul.oem.util.exceptions;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class OemRuLException extends BtpException {
    public OemRuLException() {
        super();
    }

    public OemRuLException(@Nullable final String message) {
        super(message);
    }

    public OemRuLException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public OemRuLException(@Nullable final Throwable cause) {
        super(cause);
    }

    protected OemRuLException(@Nullable final String message, @Nullable final Throwable cause,
                              @NotNull final boolean enableSuppression,
                              @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
