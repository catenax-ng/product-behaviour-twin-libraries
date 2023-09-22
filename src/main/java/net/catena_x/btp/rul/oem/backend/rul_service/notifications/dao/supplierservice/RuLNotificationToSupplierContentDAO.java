package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.items.RuLInputDAO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLNotificationToSupplierContentDAO {
    private String requestRefId;
    private List<RuLInputDAO> endurancePredictorInputs;
}
