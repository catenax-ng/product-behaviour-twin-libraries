package net.catena_x.btp.libraries.edc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.general.EdcElementType;
import net.catena_x.btp.libraries.edc.model.policy.Policy;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyDefinition {
    @JsonProperty("@context")
    private Context context = Context.DEFAULT_CONTEXT_POLICY;

    @JsonProperty("@type")
    EdcElementType type = EdcElementType.POLICY_DEFINITION_REQUEST_DTO;

    @JsonProperty("@id")
    private String id = null;

    private Policy policy = null;
}
