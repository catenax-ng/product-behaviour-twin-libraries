package net.catena_x.btp.libraries.bamm.testdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestData {
    private List<DigitalTwin> digitalTwins;
    private ClassifiedLoadSpectrum clutchLoadSpectrumGreen = null;
    private ClassifiedLoadSpectrum clutchLoadSpectrumYellow = null;
    private ClassifiedLoadSpectrum clutchLoadSpectrumRed = null;
}
