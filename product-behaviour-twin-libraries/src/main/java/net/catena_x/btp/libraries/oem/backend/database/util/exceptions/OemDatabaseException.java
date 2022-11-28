package net.catena_x.btp.libraries.oem.backend.database.util.exceptions;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class OemDatabaseException extends BtpException {
    public OemDatabaseException() {
        super();
    }

    public OemDatabaseException(@Nullable final String message) {
        super(message);
    }

    public OemDatabaseException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public OemDatabaseException(@Nullable final Throwable cause) {
        super(cause);
    }

    protected OemDatabaseException(@Nullable final String message, @Nullable final Throwable cause,
                                   @NotNull final boolean enableSuppression,
                                   @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
