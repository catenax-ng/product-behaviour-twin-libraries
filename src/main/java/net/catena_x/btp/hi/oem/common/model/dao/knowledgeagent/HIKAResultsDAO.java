package net.catena_x.btp.hi.oem.common.model.dao.knowledgeagent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIKAResultsDAO<BindingType> {
    private List<BindingType> bindings;
}
