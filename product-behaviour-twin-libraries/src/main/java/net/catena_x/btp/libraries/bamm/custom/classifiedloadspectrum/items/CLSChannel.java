package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CLSChannel {
    private String unit;
    private long numberOfBins;
    private String channelName;
    private double upperLimit;
    private double lowerLimit;
}
