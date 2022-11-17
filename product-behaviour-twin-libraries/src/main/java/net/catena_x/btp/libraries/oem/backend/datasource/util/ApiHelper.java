package net.catena_x.btp.libraries.oem.backend.datasource.util;

import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResult;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResultType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ApiHelper {
    public ResponseEntity<ApiResult> ok(@NotNull final String info) {
        return ok(info, null);
    }

    public ResponseEntity<ApiResult> okWithValue(@NotNull final String value) {
        return ok(null, value);
    }

    public ResponseEntity<ApiResult> ok(@Nullable final String info, @Nullable final String value) {
        return new ResponseEntity<ApiResult>(
                new ApiResult(Instant.now(), ApiResultType.OK, info, value),
                generateDefaultHeaders(), HttpStatus.OK);
    }

    public ResponseEntity<ApiResult> failed(@NotNull final String error) {
        return new ResponseEntity<ApiResult>(
                new ApiResult(Instant.now(), ApiResultType.ERROR, error, null),
                generateDefaultHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Content-Disposition", "inline");
        return headers;
    }
}
