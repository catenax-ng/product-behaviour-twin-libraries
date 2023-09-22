package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuLOutput {
    private String componentId;
    private String componentType;
    private RemainingUsefulLife remainingUsefulLife;
}
