package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CLSHeader {
    private String countingValue;
    private String countingUnit;
    private String countingMethod;
    List<CLSChannel> channels;
}
