package net.catena_x.btp.libraries.edc.model.policy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.Type;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Constraint {
    @JsonProperty("@type")
    private Type type = Type.LOGICAL_CONSTRAINT;

    @JsonProperty("odrl:or")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<OrConstraint> or = null;

    @JsonProperty("odrl:leftOperand")
    private PolicyLeftOperand leftOperand = null;

    @JsonProperty("odrl:operator")
    private PolicyOperator operator = null;

    @JsonProperty("odrl:rightOperand")
    private String rightOperand = null;
}
