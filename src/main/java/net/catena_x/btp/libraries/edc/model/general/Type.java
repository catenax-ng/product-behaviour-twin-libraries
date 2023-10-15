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
    CATALOG,

    @JsonProperty("dcat:DataService")
    CATALOG_DATA_SERVICE,

    @JsonProperty("dcat:Dataset")
    CATALOG_DATASET,

    @JsonProperty("dcat:Distribution")
    CATALOG_DISTRIBUTION,

    @JsonProperty("Policy")
    POLICY,

    @JsonProperty("PolicyDefinitionRequestDto")
    POLICY_DEFINITION_REQUEST_DTO,

    @JsonProperty("LogicalConstraint")
    LOGICAL_CONSTRAINT,

    @JsonProperty("Constraint")
    CONSTRAINT,

    @JsonProperty("dcat:DataService")
    DCAT_DATA_SERVICE,

    @JsonProperty("odrl:Set")
    SET,

    @JsonProperty("NegotiationInitiateRequestDto")
    NEGOTIATION_INITIATE_REQUEST,

    @JsonProperty("edc:IdResponse")
    ID_RESPONSE,

    @JsonProperty("edc:ContractNegotiation")
    CONTRACT_NEGOTIATION,

    CONSUMER,

    @JsonProperty("edc:TransferProcess")
    TRANSFER_PROCESS,

    @JsonProperty("edc:DataAddress")
    DATA_ADDRESS
}