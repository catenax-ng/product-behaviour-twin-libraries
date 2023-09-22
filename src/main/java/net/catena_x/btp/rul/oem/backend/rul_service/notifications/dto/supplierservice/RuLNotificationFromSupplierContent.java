package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items.RuLOutput;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuLNotificationFromSupplierContent {
    private String requestRefId;
    private String componentType;
    private List<RuLOutput> endurancePredictorOutputs;
}
