package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.requester;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLNotificationToRequesterContentDAO {
    private RemainingUsefulLife rul;
}
