package net.catena_x.btp.libraries.util.apihelper;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public final class ResponseChecker {
    public static void checkResponseDefault(@Nullable final ResponseEntity<DefaultApiResult> response)
            throws BtpException {
        assertResponseNotNull(response);
        assertResponseHttpStatusCodeSuccessful(response);
    }

    private static void assertResponseHttpStatusCodeSuccessfulDefault(
            @NotNull final ResponseEntity<DefaultApiResult> response) throws BtpException {
        if(!isHttpStatusCodeSuccessful(response)) {
            throw new BtpException(makeHttpStatusErrorMessage(getMessageFromResponse(response)));
        }
    }

    public static <T> void checkResponse(@Nullable final ResponseEntity<T> response)
            throws BtpException {
        assertResponseNotNull(response);
        assertResponseHttpStatusCodeSuccessful(response);
    }

    private static <T> void assertResponseHttpStatusCodeSuccessful(
            @NotNull final ResponseEntity<T> response) throws BtpException {
        if(!isHttpStatusCodeSuccessful(response)) {
            throw new BtpException("Http status not ok!");
        }
    }

    private static String makeHttpStatusErrorMessage(@Nullable final String responseMessage) {
        return "Http status not ok: " + ((responseMessage!=null) ? responseMessage : "Unknown error!");
    }

    private static String getMessageFromResponse(@NotNull final ResponseEntity<DefaultApiResult> response) {
        if(response.getBody() == null) {
            return null;
        }

        return response.getBody().getMessage();
    }

    private static <T> boolean isHttpStatusCodeSuccessful(@NotNull final ResponseEntity<T> response) {
        final HttpStatusCode result = response.getStatusCode();
        return result.equals(HttpStatus.OK) || result.equals(HttpStatus.CREATED) || result.equals(HttpStatus.ACCEPTED);
    }

    private static <T> boolean isStatusCodeEqual(ResponseEntity<T> response) {
        return response.getStatusCode() != HttpStatus.OK;
    }

    private static <T> void assertResponseNotNull(@Nullable final ResponseEntity<T> response)
            throws DataProviderException {
        if(response == null) {
            throw new DataProviderException("Internal error, response is nullr!");
        }
    }
}
