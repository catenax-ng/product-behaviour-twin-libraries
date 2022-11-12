package net.catena_x.btp.libraries.bamm.custom.serialparttypization.items;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SPTManufacturingInformation {
    //FA: private Instant date; //Wrong timestamp format in input file!
    private String date;
    private String country;
}
