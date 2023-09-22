package net.catena_x.btp.libraries.bamm.digitaltwin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.AssemblyPartRelationship;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.damage.Damage;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.SerialPartTypization;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalTwin {
    private String catenaXId;

    @JsonProperty(SerialPartTypization.BAMM_ID)
    private List<SerialPartTypization> serialPartTypizations;

    @JsonProperty(AssemblyPartRelationship.BAMM_ID)
    private List<AssemblyPartRelationship> assemblyPartRelationships;

    @JsonProperty(RemainingUsefulLife.BAMM_ID)
    private List<RemainingUsefulLife> remainingUsefulLifes;

    @JsonProperty(Damage.BAMM_ID)
    private List<Damage> damages;

    @JsonProperty(ClassifiedLoadSpectrum.BAMM_ID)
    private List<ClassifiedLoadSpectrum> classifiedLoadSpectra;

    @JsonProperty(AdaptionValues.BAMM_ID)
    private List<AdaptionValues> adaptionValues;
}
