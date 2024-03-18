package net.catena_x.btp.libraries.util.exceptions;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class BtpException extends Exception {
    public BtpException() {
        super();
    }

    public BtpException(@Nullable final String message) {
        super(message);
    }

    public BtpException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public BtpException(@Nullable final Throwable cause) {
        super(cause);
    }

    protected BtpException(@Nullable final String message, @Nullable final Throwable cause,
                           @NotNull final boolean enableSuppression,
                           @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getMessageTopLevel() {
        return super.getMessage();
    }

    @Override
    public String getMessage() {
        final String message = super.getMessage();

        Throwable cause = getCause();
        for (int i = 0;  i < 20; i++) {
            if(cause == null){
                return message;
            }

            if(message.indexOf(cause.getMessage()) < 0) {
                return message + " {cause: " + cause.getMessage() + "}";
            }

            cause = cause.getCause();
        }

        return message;
    }
}
