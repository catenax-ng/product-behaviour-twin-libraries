package net.catena_x.btp.libraries.edc.model.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsSelector {
    private ContractOperandLeft operandLeft = null;
    private ContractOperator operator = null;
    private String operandRight = null;
}