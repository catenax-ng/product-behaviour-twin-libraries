package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CLSChannel {
    private String unit;
    private long numberOfBins;
    private String channelName;
    private double upperLimit;
    private double lowerLimit;
}
