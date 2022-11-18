package net.catena_x.btp.libraries.notification.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum NFStatus {
    @JsonProperty("CREATED")
    CREATED,
    @JsonProperty("SENT")
    SENT,
    @JsonProperty("RECEIVED")
    RECEIVED,
    @JsonProperty("ACKNOWLEDGED")
    ACKNOWLEDGED,
    @JsonProperty("ACCEPTED")
    ACCEPTED,
    @JsonProperty("DECLINED")
    DECLINED,
    @JsonProperty("CLOSED")
    CLOSED
}
