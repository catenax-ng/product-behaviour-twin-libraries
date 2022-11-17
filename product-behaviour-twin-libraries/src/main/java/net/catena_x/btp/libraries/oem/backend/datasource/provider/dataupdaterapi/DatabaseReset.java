package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResult;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseReset {
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired DataUpdaterApi dataUpdaterApi;

    public void reset() throws DataProviderException {
        final HttpUrl requestUrl = HttpUrl.parse(
                dataUpdaterApi.getRawdataApiBaseUrl() + "/reset");
        final HttpHeaders headers = dataUpdaterApi.generateDefaultHeaders();

        dataUpdaterApi.addAuthorizationHeaders(headers);

        final ResponseEntity<ApiResult> response = restTemplate.getForEntity(
                requestUrl.toString(), ApiResult.class);

        dataUpdaterApi.checkResponse(response);
    }
}
