package net.catena_x.btp.libraries.oem.backend.datasource.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ApiResultType {
    @JsonProperty("Ok")
    OK,
    @JsonProperty("Error")
    ERROR
}
