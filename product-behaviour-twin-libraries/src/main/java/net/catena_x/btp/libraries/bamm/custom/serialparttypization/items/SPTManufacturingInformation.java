package net.catena_x.btp.libraries.bamm.custom.serialparttypization.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SPTManufacturingInformation {
    private Instant date;
    private String country;
}
