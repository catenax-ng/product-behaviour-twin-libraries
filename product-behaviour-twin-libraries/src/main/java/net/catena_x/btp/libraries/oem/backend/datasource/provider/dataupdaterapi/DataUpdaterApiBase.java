package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Base64;
import java.util.Collections;

public class DataUpdaterApiBase {

    protected String getRawdataApiBaseUrl() {
        return "http://localhost:20002//api/rawdata";
    }

    protected HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    protected <T> void checkResponse(@Nullable final ResponseEntity<T> response)
            throws DataProviderException {

        if(response == null) {
            throw new DataProviderException("Internal error using data updater api!");
        }
        else if( response.getStatusCode() != HttpStatus.OK) {
            throw new DataProviderException("Http status not ok while using data updater api!");
        }
    }

    protected void addAuthorizationHeaders(@NotNull final HttpHeaders headers) {
        headers.add("Authorization", getDataUpdaterAuthString());
    }

    private String getDataUpdaterAuthString() {
        final StringBuilder sb = new StringBuilder();

        final String authStr = sb.append(getDataUpdaterUsername()).append(":")
                .append(getDataUpdaterPassword()).toString();

        sb.setLength(0);
        sb.append("Basic ").append(Base64.getEncoder().encodeToString(authStr.getBytes()));

        return sb.toString();
    }

    private String getDataUpdaterUsername() {
        return "user";
    }

    private String getDataUpdaterPassword() {
        return "pass";
    }
}
