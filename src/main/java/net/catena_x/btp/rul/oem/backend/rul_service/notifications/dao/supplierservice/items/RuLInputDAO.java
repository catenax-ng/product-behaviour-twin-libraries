package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLInputDAO {
    private String componentId;
    private ClassifiedLoadSpectrum classifiedLoadSpectrumGearSet;
    private ClassifiedLoadSpectrum classifiedLoadSpectrumGearOil;
}
