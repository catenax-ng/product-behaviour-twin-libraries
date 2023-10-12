package net.catena_x.btp.sedc.apps.oem.database.base;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PeakLoadTableBase {
    protected String generateNewId() {
        return UUID.randomUUID().toString();
    }

    protected BtpException failed() {
        return failed("Database statement failed!");
    }

    protected BtpException failed(@Nullable String message) {
        return new BtpException(message);
    }

    protected BtpException failed(@Nullable String message, @Nullable Throwable cause) {
        return new BtpException(message, cause);
    }

    protected BtpException failed(@Nullable Throwable cause) {
        return new BtpException(cause);
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