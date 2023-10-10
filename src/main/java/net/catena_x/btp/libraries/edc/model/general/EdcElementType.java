package net.catena_x.btp.libraries.edc.model.general;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EdcElementType {
    @JsonProperty("Policy")
    POLICY,

    @JsonProperty("PolicyDefinitionRequestDto")
    POLICY_DEFINITION_REQUEST_DTO,

    @JsonProperty("LogicalConstraint")
    LOGICAL_CONSTRAINT,

    @JsonProperty("Constraint")
    CONSTRAINT,

    @JsonProperty("dcat:DataService")
    DCAT_DATA_SERVIC
}
