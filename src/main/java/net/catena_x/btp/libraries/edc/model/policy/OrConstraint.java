package net.catena_x.btp.libraries.edc.model.policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.EdcElementType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrConstraint {
    @JsonProperty("@type")
    private EdcElementType type = EdcElementType.CONSTRAINT;

    @JsonProperty("odrl:leftOperand")
    private PolicyLeftOperand leftOperand = null;

    @JsonProperty("odrl:operator")
    private PolicyOperatorStructured operator = null;

    @JsonProperty("odrl:rightOperand")
    private String rightOperand = null;


}
