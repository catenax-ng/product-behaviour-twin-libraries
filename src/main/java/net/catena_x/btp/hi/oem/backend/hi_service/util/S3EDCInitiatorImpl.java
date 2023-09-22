package net.catena_x.btp.hi.oem.backend.hi_service.util;

import net.catena_x.btp.libraries.edc.EdcApi;
import net.catena_x.btp.libraries.edc.util.exceptions.EdcException;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class S3EDCInitiatorImpl {
    @Autowired private EdcApi edcApi;

    public <BodyType, ResponseType> ResponseEntity<ResponseType> startAsyncRequest(
            @NotNull final String requestId,
            @NotNull final String endpoint,
            @NotNull final String asset,
            @NotNull final BodyType messageBody,
            @NotNull Class<ResponseType> responseTypeClass) throws EdcException {

        return edcApi.post(HttpUrl.parse(endpoint), asset, responseTypeClass, messageBody, new HttpHeaders());
    }
}
