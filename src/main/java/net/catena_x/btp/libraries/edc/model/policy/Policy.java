package net.catena_x.btp.libraries.edc.model.policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Policy {
    @JsonProperty("odrl:permission")
    private List<PolicyRestriction> permissions;

    @JsonProperty("odrl:prohibition")
    private List<PolicyRestriction> prohibitions;

    @JsonProperty("odrl:obligation")
    private List<PolicyRestriction> obligations;
}
