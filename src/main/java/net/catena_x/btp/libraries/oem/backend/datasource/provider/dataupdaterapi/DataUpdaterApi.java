package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.Collections;

@Component
public class DataUpdaterApi {
    @Value("${services.dataupdater.endpoint.baseurl}") private String dataupdaterBaseUrl;
    @Value("${services.dataupdater.endpoint.username}") private String dataupdaterUserName;
    @Value("${services.dataupdater.endpoint.password}") private String dataupdaterPassword;
    @Value("${services.dataupdater.endpoint.authenticationActivated}") private boolean authenticationActivated;

    protected String getRawdataApiBaseUrl() {
        return dataupdaterBaseUrl;
    }

    protected HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    protected void addAuthorizationHeaders(@NotNull final HttpHeaders headers) {
        if (isAuthenticationActivated()) {
            headers.add("Authorization", getDataUpdaterAuthString());
        }
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
        return dataupdaterUserName;
    }

    private String getDataUpdaterPassword() {
        return dataupdaterPassword;
    }

    private boolean isAuthenticationActivated() {
        return authenticationActivated;
    }
}
