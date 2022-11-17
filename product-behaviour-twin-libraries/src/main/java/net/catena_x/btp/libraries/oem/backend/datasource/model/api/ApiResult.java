package net.catena_x.btp.libraries.oem.backend.datasource.model.api;

import java.time.Instant;

public record ApiResult(
        Instant timestamp,
        ApiResultType result,
        String message,
        String value
) {}
