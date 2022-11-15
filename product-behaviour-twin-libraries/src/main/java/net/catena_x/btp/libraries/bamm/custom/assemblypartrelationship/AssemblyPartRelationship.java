package net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.base.BammBase;
import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.items.APRChildPart;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssemblyPartRelationship implements BammBase {
    public final static String BAMM_ID = "urn:bamm:io.catenax.assembly_part_relationship:1.1.1#AssemblyPartRelationship";

    private String catenaXId;
    private List<APRChildPart> childParts;

    @Override public String getBammId() {
        return BAMM_ID;
    }
}
