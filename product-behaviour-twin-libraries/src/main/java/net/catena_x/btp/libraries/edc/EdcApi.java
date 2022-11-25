package net.catena_x.btp.libraries.edc;

import net.catena_x.btp.libraries.edc.util.exceptions.EdcException;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
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

    @Bean
    public static RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public <ResponseType> ResponseEntity<ResponseType> get(
                                               @NotNull final HttpUrl partnerUrl, @NotNull final String asset,
                                               @NotNull Class<ResponseType> responseClass,
                                               @Nullable final HttpHeaders headers)
            throws EdcException {

        HttpUrl requestUrl = buildApiWrapperUrl(partnerUrl, asset);

        addAuthorizationHeaders(headers);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<ResponseType> response = restTemplate.exchange(
                requestUrl.uri(), HttpMethod.GET, request, responseClass);

        checkResponse(response);

        return response;
    }

    public <BodyType, ResponseType> ResponseEntity<ResponseType> post(
            @NotNull final HttpUrl partnerUrl,
            @NotNull final String asset,
            @NotNull Class<ResponseType> responseClass,
            BodyType body,
            @Nullable HttpHeaders headers) throws EdcException {

        HttpUrl requestUrl = buildApiWrapperUrl(partnerUrl, asset);
        addAuthorizationHeaders(headers);
        HttpEntity<BodyType> request = new HttpEntity<>(body, headers);
        String url = requestUrl.toString();

        // this is a bad fix for API wrapper behaviour
        if( url.toUpperCase().endsWith("%2F")) {
            url = url.substring(0, url.length() - 3);
        }

        url = URLDecoder.decode(url, Charset.defaultCharset());

        ResponseEntity<ResponseType> response = restTemplate.postForEntity(url, request, responseClass);

        checkResponse(response);

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

    private <ResponseType> void checkResponse(@Nullable ResponseEntity<ResponseType> response)
            throws EdcException {
        if(response == null) {
            throw new EdcException("Internal error using edc api!");
        }
        else if( response.getStatusCode() != HttpStatus.OK) {
            throw new EdcException("Http status not ok while using edc api!");
        }
    }
}
