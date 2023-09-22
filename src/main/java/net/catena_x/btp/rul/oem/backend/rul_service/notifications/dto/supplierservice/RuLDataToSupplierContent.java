package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice.items.RuLInput;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLDataToSupplierContent {
    private String requestRefId;
    private List<RuLInput> endurancePredictorInputs;
}
