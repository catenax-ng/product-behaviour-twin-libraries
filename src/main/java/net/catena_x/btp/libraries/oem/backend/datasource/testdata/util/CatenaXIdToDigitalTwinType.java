package net.catena_x.btp.libraries.oem.backend.datasource.testdata.util;

import javax.validation.constraints.NotNull;

@FunctionalInterface
public
interface CatenaXIdToDigitalTwinType {
    DigitalTwinType determine(@NotNull final String id);
}
