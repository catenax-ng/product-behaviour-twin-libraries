package net.catena_x.btp.rul.oem.backend.model.dto.vinrelation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLVinRelation {
    private String vin;
    private String refId;
    private boolean noData;
}
