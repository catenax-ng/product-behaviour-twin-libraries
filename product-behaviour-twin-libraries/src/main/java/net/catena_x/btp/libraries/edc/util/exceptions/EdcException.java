package net.catena_x.btp.libraries.edc.util.exceptions;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class EdcException extends Exception {
    public EdcException() {
        super();
    }

    public EdcException(@Nullable final String message) {
        super(message);
    }

    public EdcException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public EdcException(@Nullable final Throwable cause) {
        super(cause);
    }

    protected EdcException(@Nullable final String message, @Nullable final Throwable cause,
                           @NotNull final boolean enableSuppression,
                           @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
