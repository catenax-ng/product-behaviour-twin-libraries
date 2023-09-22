package net.catena_x.btp.libraries.notification.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum NFSeverity {
    @JsonProperty("MINOR")
    MINOR,
    @JsonProperty("MAJOR")
    MAJOR,
    @JsonProperty("CRITICAL")
    CRITICAL,
    @JsonProperty("LIFE-THREATENING")
    LIFE_THREATENING
}
