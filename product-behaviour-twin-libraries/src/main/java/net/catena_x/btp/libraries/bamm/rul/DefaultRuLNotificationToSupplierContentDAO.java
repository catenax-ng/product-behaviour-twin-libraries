package net.catena_x.btp.libraries.bamm.rul;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultRuLNotificationToSupplierContentDAO {
    private String requestRefId;
    private List<DefaultRuLInputDAO> endurancePredictorInputs;
}
