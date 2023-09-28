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
public class Constraint {
    @JsonProperty("odrl:leftOperand")
    private PolicyLeftOperand leftOperand = null;

    @JsonProperty("odrl:operator")
    private PolicyOperator operator = null;

    @JsonProperty("odrl:rightOperand")
    private String rightOperand = null;
}
