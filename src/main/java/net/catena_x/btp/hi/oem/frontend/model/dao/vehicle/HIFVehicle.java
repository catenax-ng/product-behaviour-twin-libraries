package net.catena_x.btp.hi.oem.frontend.model.dao.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.hi.oem.frontend.model.enums.HIFHealthState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIFVehicle {
    private String vehicleId;
    private String van;
    private String gearboxId;
    private String productionDate;
    private String updateTimestamp;
    private HIFHealthState healthStateLoadSpectra;
    private HIFHealthState healthStateAdaptionValues;
}
