package net.catena_x.btp.libraries.util.exceptions;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class S3Exception extends Exception {
    public S3Exception() {
        super();
    }

    public S3Exception(@Nullable final String message) {
        super(message);
    }

    public S3Exception(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public S3Exception(@Nullable final Throwable cause) {
        super(cause);
    }

    protected S3Exception(@Nullable final String message, @Nullable final Throwable cause,
                          @NotNull final boolean enableSuppression,
                          @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
