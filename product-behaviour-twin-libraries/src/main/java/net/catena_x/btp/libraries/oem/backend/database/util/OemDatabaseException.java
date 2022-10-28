package net.catena_x.btp.libraries.oem.backend.database.util;

public class OemDatabaseException extends Exception {
    public OemDatabaseException() {
        super();
    }

    public OemDatabaseException(String message) {
        super(message);
    }

    public OemDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public OemDatabaseException(Throwable cause) {
        super(cause);
    }

    protected OemDatabaseException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
