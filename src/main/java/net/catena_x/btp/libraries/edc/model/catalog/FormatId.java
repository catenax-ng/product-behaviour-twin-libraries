package net.catena_x.btp.libraries.edc.model.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FormatId {
    @JsonProperty("HttpProxy")
    HTTP_PROXY,

    @JsonProperty("AmazonS3")
    AMAZON_S3
}
