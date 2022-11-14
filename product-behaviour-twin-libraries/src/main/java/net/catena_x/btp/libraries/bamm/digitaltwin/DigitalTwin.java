package net.catena_x.btp.libraries.bamm.digitaltwin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.AssemblyPartRelationship;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.damage.Damage;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.SerialPartTypization;

import java.util.List;

@Getter
@Setter
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
}
