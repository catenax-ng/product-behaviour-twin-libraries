package net.catena_x.btp.libraries.edc;

import net.catena_x.btp.libraries.edc.util.exceptions.EdcException;
import net.catena_x.btp.libraries.util.apihelper.ResponseChecker;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Base64;

@Component
public class EdcApi {
    @Autowired private RestTemplate restTemplate;
    @Value("${edc.apiwrapper.url}") private String apiWrapperUrl;
    @Value("${edc.apiwrapper.submodelPath}") private String submodelPath; //"api/service"
    @Value("${edc.apiwrapper.submodel}") private String submodel; //"submodel"
    @Value("${edc.apiwrapper.providerEdcUrlKey}") private String providerEdcUrlKey; //"provider-connector-url"
    @Value("${edc.apiwrapper.username}") private String apiWrapperUsername;
    @Value("${edc.apiwrapper.password}") private String apiWrapperPassword;

    public <ResponseType> ResponseEntity<ResponseType> get(
            @NotNull final HttpUrl partnerUrl, @NotNull final String asset, @NotNull Class<ResponseType> responseClass,
            @Nullable final HttpHeaders headers) throws BtpException {

        HttpUrl requestUrl = buildApiWrapperUrl(partnerUrl, asset);

        addAuthorizationHeaders(headers);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<ResponseType> response = restTemplate.exchange(
                requestUrl.uri(), HttpMethod.GET, request, responseClass);

        ResponseChecker.checkResponse(response);

        return response;
    }

    public <BodyType, ResponseType> ResponseEntity<ResponseType> post(
            @NotNull final HttpUrl partnerUrl,
            @NotNull final String asset,
            @NotNull Class<ResponseType> responseClass,
            @NotNull BodyType body,
            @Nullable HttpHeaders headers) throws EdcException {

        final HttpUrl requestUrl = buildApiWrapperUrl(partnerUrl, asset);
        addAuthorizationHeaders(headers);
        final HttpEntity<BodyType> request = new HttpEntity<>(body, headers);

        String url = requestUrl.toString();

        // this is a bad fix for API wrapper behaviour
        if( url.toUpperCase().endsWith("%2F")) {
            url = url.substring(0, url.length() - 3);
        }

        url = URLDecoder.decode(url, Charset.defaultCharset());

        final ResponseEntity<ResponseType> response = restTemplate.postForEntity(url, request, responseClass);

        try {
            ResponseChecker.checkResponse(response);
        } catch (final BtpException exception) {
            throw new EdcException(exception);
        }

        return response;
    }

    private HttpUrl buildApiWrapperUrl(@NotNull final HttpUrl partnerUrl, @NotNull final String asset) {
        return HttpUrl.parse(this.apiWrapperUrl).newBuilder()
                .addPathSegments(submodelPath)
                .addPathSegment(asset)
                .addPathSegment(submodel)
                .addQueryParameter(providerEdcUrlKey, partnerUrl.toString())
                .build();
    }

    private void addAuthorizationHeaders(HttpHeaders headers) {
        headers.add("Authorization",getAuthString());
    }

    private String getAuthString() {
        StringBuilder sb = new StringBuilder();

        String authStr = sb.append(apiWrapperUsername).append(":")
                .append(apiWrapperPassword).toString();
        sb.setLength(0);
        sb.append("Basic ").append(Base64.getEncoder().encodeToString(authStr.getBytes()));
        return sb.toString();
    }
}
