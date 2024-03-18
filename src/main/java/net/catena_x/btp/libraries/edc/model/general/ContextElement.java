package net.catena_x.btp.libraries.edc.model.general;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ContextElement {
    @JsonProperty("https://purl.org/dc/terms/")
    DCT_TERMS,

    @JsonProperty("https://w3id.org/tractusx/v0.0.1/ns/")
    TRACTUS_X,

    @JsonProperty("https://w3id.org/edc/v0.0.1/ns/")
    EDC_V0_0_1,

    @JsonProperty("https://www.w3.org/ns/dcat/")
    DCAT,

    @JsonProperty("http://www.w3.org/ns/odrl/2/")
    ODRL_2,

    @JsonProperty("https://w3id.org/dspace/v0.8/")
    DSPACE_V0_8,

    @JsonProperty("http://www.w3.org/ns/dcat#")
    DCAT_HASH
}