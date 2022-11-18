package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CLSChannel {
    private String unit;
    private long numberOfBins;
    private String channelName;
    private double upperLimit;
    private double lowerLimit;
}
