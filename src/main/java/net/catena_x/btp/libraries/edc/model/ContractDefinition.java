package net.catena_x.btp.libraries.edc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.contract.AssetsSelector;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.general.Type;
import net.catena_x.btp.libraries.edc.model.policy.Policy;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractDefinition {
    @JsonProperty("@context")
    private Context context = Context.DEFAULT_CONTEXT_CONTRACT;

    @JsonProperty("@type")
    private Type type = Type.ASSET_DEFINITION;

    @JsonProperty("@id")
    private String id = null;

    private String accessPolicyId = null;
    private String contractPolicyId = null;
    private AssetsSelector assetsSelector = null;
}