package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CLSHeader {
    private String countingValue;
    private String countingUnit;
    private String countingMethod;
    List<CLSChannel> channels;
}
