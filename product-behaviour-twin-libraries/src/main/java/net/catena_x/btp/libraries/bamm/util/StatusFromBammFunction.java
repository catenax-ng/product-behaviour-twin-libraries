package net.catena_x.btp.libraries.bamm.util;

import javax.validation.constraints.NotNull;

import java.time.Instant;

@FunctionalInterface
public interface StatusFromBammFunction {
    Instant getTimestamp(@NotNull final Object item);
}
