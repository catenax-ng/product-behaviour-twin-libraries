package net.catena_x.btp.libraries.edc.model.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FormatId {
    @JsonProperty("HttpProxy")
    HTTP_PROXY,

    @JsonProperty("HttpProxy-PUSH")
    HTTP_PROXY_PUSH,

    @JsonProperty("HttpData-PULL")
    HTTP_DATA_PULL,

    @JsonProperty("AmazonS3")
    AMAZON_S3,

    @JsonProperty("AmazonS3-PUSH")
    AMAZON_S3_PUSH,

    @JsonProperty("AzureStorage-PUSH")
    AZURE_STORAGE_PUSH
}
