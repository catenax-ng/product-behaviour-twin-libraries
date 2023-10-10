package net.catena_x.btp.libraries.edc.model.policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.EdcElementType;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Policy {
    @JsonProperty("@type")
    private EdcElementType type = EdcElementType.POLICY;

    @JsonProperty("odrl:permission")
    private List<PolicyRestriction> permissions = null;

    @JsonProperty("odrl:prohibition")
    private List<PolicyRestriction> prohibitions = null;

    @JsonProperty("odrl:obligation")
    private List<PolicyRestriction> obligations = null;
}
