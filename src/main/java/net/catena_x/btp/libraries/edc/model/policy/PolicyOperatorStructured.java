package net.catena_x.btp.libraries.edc.model.policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyOperatorStructured {
    @JsonProperty("@id")
    private PolicyOperatorStructuredId id = PolicyOperatorStructuredId.EQUALS;
}
