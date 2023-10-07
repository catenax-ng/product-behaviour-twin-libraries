package net.catena_x.btp.libraries.util.apihelper;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.InfoTableInternal;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResultWithValue;
import net.catena_x.btp.libraries.util.apihelper.preparation.ApiResponse;
import net.catena_x.btp.libraries.util.apihelper.preparation.ApiResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class ApiHelper {
    @Autowired private ApiResponse apiResponse;

    private final Logger logger = LoggerFactory.getLogger(InfoTableInternal.class);

    public ResponseEntity<DefaultApiResult> ok(@NotNull final String message) {
        return apiResponse.toObject(ApiResult.ok(message));
    }

    public ResponseEntity<String> okAsString(@NotNull final String message) {
        return apiResponse.toString(ApiResult.ok(message), DefaultApiResult.class);
    }

    public <T> ResponseEntity<T> ok(@NotNull final T value) {
        return apiResponse.toObject(ApiResult.ok(value));
    }

    public ResponseEntity<String> okSql(@NotNull final String sqlString) {
        return apiResponse.sql(sqlString);
    }

    public <T> ResponseEntity<String> okAsString(@NotNull final T value, @NotNull final Class<T> typeOfT) {
        return apiResponse.toString(ApiResult.ok(value), typeOfT);
    }

    public <T> ResponseEntity<DefaultApiResultWithValue<T>> okWithValue(@Nullable final String message,
                                                                        @Nullable final T value) {
        return apiResponse.toObject(ApiResult.okWithValue(message, value));
    }

    public <T> ResponseEntity<String> okWithValueAsString(@Nullable final String message, @Nullable final T value) {
        final ApiResult<DefaultApiResultWithValue<T>> result = ApiResult.okWithValue(message, value);
        return apiResponse.toString(result, DefaultApiResultWithValue.class);
    }

    public ResponseEntity<DefaultApiResult> accepted(@NotNull final String message) {
        return apiResponse.toObject(ApiResult.accepted(message));
    }

    public ResponseEntity<String> acceptedAsString(@NotNull final String message) {
        return apiResponse.toString(ApiResult.accepted(message), DefaultApiResult.class);
    }

    public <T> ResponseEntity<T> accepted(@NotNull final T value) {
        return apiResponse.toObject(ApiResult.accepted(value));
    }

    public <T> ResponseEntity<String> acceptedAsString(@NotNull final T value) {
        return apiResponse.toString(ApiResult.accepted(value), DefaultApiResult.class);
    }

    public <T> ResponseEntity<DefaultApiResultWithValue<T>> acceptedWithValue(
            @Nullable final String message, @Nullable final T value) {
        return apiResponse.toObject(ApiResult.acceptedWithValue(message, value));
    }

    public <T> ResponseEntity<String> acceptedWithValueAsStringAsString(
            @Nullable final String message, @Nullable final T value) {
        return apiResponse.toString(ApiResult.acceptedWithValue(message, value), DefaultApiResultWithValue.class);
    }

    public ResponseEntity<DefaultApiResult> failed(@NotNull final String error) {
        logger.error("Responding with: " + error);
        return apiResponse.toObject(ApiResult.failed(error));
    }

    public ResponseEntity<String> failedAsString(@NotNull final String error) {
        logger.error("Responding with: " + error);
        return apiResponse.toString(ApiResult.failed(error), DefaultApiResult.class);
    }

    public <T> ResponseEntity<T> failed(@NotNull final T errorValue) {
        return apiResponse.toObject(ApiResult.failed(errorValue));
    }

    public <T> ResponseEntity<String> failedAsString(@NotNull final T errorValue) {
        final ResponseEntity<String> errorResponse =
                apiResponse.toString(ApiResult.failed(errorValue), DefaultApiResult.class);
        logger.error("Responding with: " + errorResponse.getBody().toString());
        return errorResponse;
    }

    public <T> ResponseEntity<DefaultApiResultWithValue<T>> failedWithValue(@Nullable final String error,
                                                                            @Nullable final T errorValue) {
        logger.error("Responding with: " + error);
        return apiResponse.toObject(ApiResult.failedWithValue(error, errorValue));
    }

    public <T> ResponseEntity<String> failedWithValueAsString(@Nullable final String error,
                                                              @Nullable final T errorValue) {
        logger.error("Responding with: " + error);
        return apiResponse.toString(ApiResult.failedWithValue(error, errorValue), DefaultApiResultWithValue.class);
    }

    private HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Disposition", "inline");
        return headers;
    }
}
