package net.catena_x.btp.libraries.util.apihelper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ApiResultType {
    @JsonProperty("Ok")
    OK,
    @JsonProperty("Error")
    ERROR
}
