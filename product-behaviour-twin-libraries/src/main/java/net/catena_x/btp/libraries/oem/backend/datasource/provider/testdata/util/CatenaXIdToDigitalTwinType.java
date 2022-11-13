package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public
interface CatenaXIdToDigitalTwinType {
    DigitalTwinType determine(@NotNull final String id);
}
