package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum LoadSpectrumType {
    @JsonProperty("GearSet")
    GEAR_SET,
    @JsonProperty("GearOil")
    GEAR_OIL,
    @JsonProperty("Clutch")
    CLUTCH
}
