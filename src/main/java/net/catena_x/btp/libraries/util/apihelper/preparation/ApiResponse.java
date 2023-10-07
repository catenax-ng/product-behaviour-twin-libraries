package net.catena_x.btp.libraries.util.apihelper.preparation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class ApiResponse {
    @Autowired private ObjectMapper objectMapper;

    public <T> ResponseEntity<String> toString(@NotNull final ApiResult<T> apiResult,
                                               @NotNull final Class<?> typeOfT) {
        try {
            final String resultAsString = objectMapper.writerFor(typeOfT)
                    .writeValueAsString(apiResult.getApiResult());
            return new ResponseEntity<>(resultAsString, apiResult.getHeaders(),
                    apiResult.getStatusCode() );
        } catch ( final JsonProcessingException exception) {
            return new ResponseEntity<>("{\"message\": \"Error in error handling!\"}",
                    apiResult.getHeaders(), apiResult.getStatusCode() );
        }
    }

    public <T> ResponseEntity<T> toObject(@NotNull final ApiResult<T> apiResult) {
        return new ResponseEntity<>(apiResult.getApiResult(), apiResult.getHeaders(), apiResult.getStatusCode());
    }

    public ResponseEntity<String> sql(@NotNull final String sqlString) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/sql");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, " attachment; filename=\"testdata.sql\"");
        return new ResponseEntity<>(sqlString, headers, HttpStatus.OK);
    }
}
