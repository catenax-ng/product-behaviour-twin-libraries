package net.catena_x.btp.libraries.edc.model.policy;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PolicyLeftOperand {
    @JsonProperty("https://w3id.org/tractusx/v0.0.1/ns/BusinessPartnerNumber")
    BUSINESS_PARTNER_NUMBER,

    @JsonProperty("https://w3id.org/tractusx/v0.0.1/ns/BusinessPartnerGroup")
    BUSINESS_PARTNER_GROUP
}