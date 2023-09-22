package net.catena_x.btp.rul.oem.backend.database.rul.base;

import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RuLTableBase {
    protected String generateNewId() {
        return UUID.randomUUID().toString();
    }

    protected OemRuLException failed() {
        return failed("Database statement failed!");
    }

    protected OemRuLException failed(@Nullable String message) {
        return new OemRuLException(message);
    }

    protected OemRuLException failed(@Nullable String message, @Nullable Throwable cause) {
        return new OemRuLException(message, cause);
    }

    protected OemRuLException failed(@Nullable Throwable cause) {
        return new OemRuLException(cause);
    }

    protected String arrayToJsonString(@Nullable double[] arrayData) {
        final StringBuilder arrayToJsonBuilder = new StringBuilder();

        arrayToJsonBuilder.append("[");

        if(arrayData != null) {
            if(arrayData.length > 0) {
                arrayToJsonBuilder.append(arrayData[0]);
                for (int i = 1; i < arrayData.length; i++) {
                    arrayToJsonBuilder.append(",");
                    arrayToJsonBuilder.append(arrayData[i]);
                }
            }
        }

        arrayToJsonBuilder.append("]");

        return arrayToJsonBuilder.toString();
    }
}
