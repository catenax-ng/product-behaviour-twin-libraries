package net.catena_x.btp.libraries.util.apihelper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultApiResultWithValue<T> extends DefaultApiResult {
    private T value;

    public DefaultApiResultWithValue(@NotNull final Instant timestamp, @NotNull final ApiResultType result,
                                     @NotNull final String message, @NotNull final T value) {
        super(timestamp, result, message);
        this.value = value;
    }
}
