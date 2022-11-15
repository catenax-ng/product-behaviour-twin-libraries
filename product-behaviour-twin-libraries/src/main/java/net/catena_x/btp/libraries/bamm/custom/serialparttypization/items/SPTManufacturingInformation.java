package net.catena_x.btp.libraries.bamm.custom.serialparttypization.items;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SPTManufacturingInformation {
    private Instant date;
    private String country;
}
