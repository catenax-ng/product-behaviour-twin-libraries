package net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective.items;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CLCHeader {
    private String countingValue;
    private String countingUnit;
    private String countingMethod;
    List<CLCChannel> channels;
}
