package net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class UncheckedDataProviderException extends RuntimeException {

    public UncheckedDataProviderException() {
        super();
    }

    public UncheckedDataProviderException(@Nullable final DataProviderException checkedException) {
        super(checkedException.getMessage(), checkedException.getCause());
    }

    protected UncheckedDataProviderException(@Nullable final DataProviderException checkedException,
                                             @NotNull final boolean enableSuppression,
                                             @NotNull final boolean writableStackTrace) {
        super(checkedException.getMessage(), checkedException.getCause(), enableSuppression, writableStackTrace);
    }
}
