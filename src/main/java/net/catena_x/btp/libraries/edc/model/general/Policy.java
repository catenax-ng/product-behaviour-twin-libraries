package net.catena_x.btp.libraries.edc.model.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.policy.PolicyRestriction;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Policy {
    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("@type")
    private Type type = Type.POLICY;

    @JsonProperty("odrl:permission")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<PolicyRestriction> permission = null;

    @JsonProperty("odrl:prohibition")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<PolicyRestriction> prohibition = null;

    @JsonProperty("odrl:obligation")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<PolicyRestriction> obligation = null;

    @JsonProperty("odrl:target")
    private String target = null;
}