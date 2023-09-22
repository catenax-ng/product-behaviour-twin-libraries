package net.catena_x.btp.hi.oem.common.model.dto.knowledgeagent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIKAOutputs {
    List<HIKAOutput> results;
}
