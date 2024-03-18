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

    @JsonProperty("Catalog")
    CATALOG_PLAIN,

    @JsonProperty("dcat:DataService")
    CATALOG_DATA_SERVICE,

    @JsonProperty("DataService")
    CATALOG_DATA_SERVICE_PLAIN,

    @JsonProperty("dcat:Dataset")
    CATALOG_DATASET,

    @JsonProperty("Dataset")
    CATALOG_DATASET_PLAIN,

    @JsonProperty("dcat:Distribution")
    CATALOG_DISTRIBUTION,

    @JsonProperty("Distribution")
    CATALOG_DISTRIBUTION_PLAIN,

    @JsonProperty("odrl:Set")
    POLICY,

    @JsonProperty("PolicyDefinitionRequestDto")
    POLICY_DEFINITION_REQUEST_DTO,

    @JsonProperty("LogicalConstraint")
    LOGICAL_CONSTRAINT,

    @JsonProperty("Constraint")
    CONSTRAINT,

    @JsonProperty("dcat:DataService")
    DCAT_DATA_SERVICE,

    @JsonProperty("DataService")
    DCAT_DATA_SERVICE_PLAIN,

    @JsonProperty("Set")
    SET_PLAIN,

    @JsonProperty("NegotiationInitiateRequestDto")
    NEGOTIATION_INITIATE_REQUEST,

    @JsonProperty("edc:IdResponse")
    ID_RESPONSE,

    @JsonProperty("IdResponse")
    ID_RESPONSE_PLAIN,

    @JsonProperty("edc:ContractNegotiation")
    CONTRACT_NEGOTIATION,

    @JsonProperty("ContractNegotiation")
    CONTRACT_NEGOTIATION_PLAIN,

    CONSUMER,

    @JsonProperty("edc:TransferProcess")
    TRANSFER_PROCESS,

    @JsonProperty("TransferProcess")
    TRANSFER_PROCESS_PLAIN,

    @JsonProperty("edc:DataAddress")
    DATA_ADDRESS,

    @JsonProperty("DataAddress")
    DATA_ADDRESS_PLAIN
}