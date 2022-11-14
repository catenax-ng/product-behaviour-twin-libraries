package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.oem.backend.datasource.model.api.DataUpdaterStatus;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import okhttp3.HttpUrl;

@Component
public class VehicleRegistration extends DataUpdaterApiBase {
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<DataUpdaterStatus> post(@NotNull final VehicleInfo vehicleInfo) throws DataProviderException {
        final HttpUrl requestUrl = HttpUrl.parse(getRawdataApiBaseUrl() + "/vehicle/register");
        final HttpHeaders headers = generateDefaultHeaders();

        addAuthorizationHeaders(headers);

        final HttpEntity<VehicleInfo> request = new HttpEntity<>(vehicleInfo, headers);

        final ResponseEntity<DataUpdaterStatus> response = restTemplate.postForEntity(
                requestUrl.toString(), request, DataUpdaterStatus.class);

        checkResponse(response);

        return response;
    }
}
