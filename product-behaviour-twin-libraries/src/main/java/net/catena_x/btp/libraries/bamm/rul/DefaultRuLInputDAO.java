package net.catena_x.btp.libraries.bamm.rul;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultRuLInputDAO {
    private String componentId;
    private ClassifiedLoadSpectrum classifiedLoadSpectrumGearSet;
    private ClassifiedLoadSpectrum classifiedLoadSpectrumGearOil;
}
