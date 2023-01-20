package net.catena_x.btp.libraries.bamm.testdata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestData {
    private List<DigitalTwin> digitalTwins;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ClassifiedLoadSpectrum clutchLoadSpectrumGreen = null;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ClassifiedLoadSpectrum clutchLoadSpectrumYellow = null;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ClassifiedLoadSpectrum clutchLoadSpectrumRed = null;
}
