package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ResponseChecker;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseReset {
    @Autowired private RestTemplate restTemplate;
    @Autowired private DataUpdaterApi dataUpdaterApi;

    public void reset() throws DataProviderException {
        final HttpUrl requestUrl = HttpUrl.parse(
                dataUpdaterApi.getRawdataApiBaseUrl()).newBuilder().addPathSegment("reset").build();

        final HttpHeaders headers = dataUpdaterApi.generateDefaultHeaders();

        dataUpdaterApi.addAuthorizationHeaders(headers);

        final ResponseEntity<DefaultApiResult> response = restTemplate.exchange(
                requestUrl.toString(), HttpMethod.GET,
                new HttpEntity<>(headers), DefaultApiResult.class);

        try {
            ResponseChecker.checkResponse(response);
        } catch (final BtpException exception) {
            throw new DataProviderException(exception);
        }
    }
}
