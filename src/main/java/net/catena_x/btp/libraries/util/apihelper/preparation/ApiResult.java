package net.catena_x.btp.libraries.util.apihelper.preparation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catena_x.btp.libraries.util.apihelper.model.ApiResultType;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResultWithValue;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@NoArgsConstructor
public class ApiResult<T> {
    private T apiResult;
    private HttpHeaders headers;
    private HttpStatus statusCode;

    public static ApiResult<DefaultApiResult> ok(@NotNull final String message) {
        return ok(new DefaultApiResult(Instant.now(), ApiResultType.OK, message));
    }

    public static <T> ApiResult<T> ok(@NotNull final T result) {
        return result(result, HttpStatus.OK);
    }

    public static <T> ApiResult<DefaultApiResultWithValue<T>> okWithValue(@Nullable final String message,
                                                                          @NotNull final T value) {
        return result(new DefaultApiResultWithValue<T>(Instant.now(), ApiResultType.OK, message, value), HttpStatus.OK);
    }

    public static ApiResult<DefaultApiResult> accepted(@NotNull final String message) {
        return accepted(new DefaultApiResult(Instant.now(), ApiResultType.OK, message));
    }

    public static <T> ApiResult<T> accepted(@NotNull final T result) {
        return result(result, HttpStatus.ACCEPTED);
    }

    public static <T> ApiResult<DefaultApiResultWithValue<T>> acceptedWithValue(@NotNull final T value) {
        return acceptedWithValue(null, value);
    }

    public static <T> ApiResult<DefaultApiResultWithValue<T>> acceptedWithValue(@Nullable final String message,
                                                                                @NotNull final T value) {
        return result(new DefaultApiResultWithValue<T>(Instant.now(), ApiResultType.OK, message, value),
                HttpStatus.ACCEPTED);
    }

    public static ApiResult<DefaultApiResult> failed(@NotNull final String message) {
        return failed(new DefaultApiResult(Instant.now(), ApiResultType.ERROR, message));
    }

    public static <T> ApiResult<T> failed(@NotNull final T result) {
        return result(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ApiResult<DefaultApiResultWithValue<T>> failedWithValue(@Nullable final String message,
                                                                              @NotNull final T value) {
        return result(new DefaultApiResultWithValue<T>(Instant.now(), ApiResultType.ERROR, message, value),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static <T> ApiResult<T> result(@NotNull final T result, @NotNull final HttpStatus statusCode ) {
        final ApiResult<T> preparedResult = new ApiResult<>();
        preparedResult.apiResult = result;
        preparedResult.headers = generateDefaultHeaders();
        preparedResult.statusCode = statusCode;
        return preparedResult;
    }

    private static HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Disposition", "inline");
        return headers;
    }
}
