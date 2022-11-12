package net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CLCChannel {
    private String unit;
    private long numberOfBins;
    private String channelName;
    private double upperLimit;
    private double lowerLimit;
}
