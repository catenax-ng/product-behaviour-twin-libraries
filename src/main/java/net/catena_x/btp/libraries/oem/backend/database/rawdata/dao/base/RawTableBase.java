package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RawTableBase {
    protected String generateNewId() {
        return UUID.randomUUID().toString();
    }

    protected OemDatabaseException failed() {
        return failed("Database statement failed!");
    }

    protected OemDatabaseException failed(@Nullable String message) {
        return new OemDatabaseException(message);
    }

    protected OemDatabaseException failed(@Nullable String message, @Nullable Throwable cause) {
        return new OemDatabaseException(message, cause);
    }

    protected OemDatabaseException failed(@Nullable Throwable cause) {
        return new OemDatabaseException(cause);
    }
}
