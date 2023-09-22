package net.catena_x.btp.hi.oem.common.model.dao.knowledgeagent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class HIKAInputsDAO extends HIKAExchangeDAOBase<HIKAInputBindingDAO> {
    public HIKAInputsDAO(@Nullable final HIKAHeaderDAO head,
                         @Nullable final HIKAResultsDAO<HIKAInputBindingDAO> results) {
        super(head, results);
    }
}