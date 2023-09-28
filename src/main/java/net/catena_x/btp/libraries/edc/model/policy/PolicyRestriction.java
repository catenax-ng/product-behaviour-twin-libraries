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
public class PolicyRestriction {
    @JsonProperty("odrl:action")
    private ActionType action;

    @JsonProperty("odrl:constraint")
    private List<Constraint> constraints;
}
