package net.catena_x.btp.libraries.bamm.util;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@FunctionalInterface
public interface StatusFromBammFunction {
    Instant getTimestamp(@NotNull final Object item);
}
