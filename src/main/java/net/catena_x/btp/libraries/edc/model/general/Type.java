package net.catena_x.btp.libraries.edc.model.general;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Type {
    @JsonProperty("Asset")
    ASSET_DEFINITION,

    @JsonProperty("PolicyDefinition")
    POLICY_DEFINITION,

    @JsonProperty("ContractDefinition")
    CONTRACT_DEFINITION,

    @JsonProperty("dcat:Catalog")
    CATALOG_RESULT,

    @JsonProperty("dcat:DataService")
    CATALOG_DATA_SERVICE
}