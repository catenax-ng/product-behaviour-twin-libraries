package net.catena_x.btp.libraries.util.apihelper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultApiResult {
    private Instant timestamp;
    private ApiResultType result;
    private String message;
}
