package net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class DataProviderException extends BtpException {
    public DataProviderException() {
        super();
    }

    public DataProviderException(@Nullable final String message) {
        super(message);
    }

    public DataProviderException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public DataProviderException(@Nullable final Throwable cause) {
        super(cause);
    }

    protected DataProviderException(@Nullable final String message, @Nullable final Throwable cause,
                                    @NotNull final boolean enableSuppression,
                                    @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
