package net.catena_x.btp.hi.oem.common.model.dao.knowledgeagent;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HIKAOutputBindingDAO {
    private HIKAVariableDAO van;
    private HIKAVariableDAO aggregate;
    private HIKAVariableDAO assembly;
    private HIKAVariableDAO healthType;
    private HIKAVariableDAO recordDate;
    private HIKAVariableDAO mileage;
    private HIKAVariableDAO operatingTime;
    private HIKAVariableDAO health;
}
