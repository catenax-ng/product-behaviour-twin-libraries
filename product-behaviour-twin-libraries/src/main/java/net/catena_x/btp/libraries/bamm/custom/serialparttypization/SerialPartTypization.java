package net.catena_x.btp.libraries.bamm.custom.serialparttypization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.base.BammBase;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTLocalIdentifier;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTManufacturingInformation;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTPartTypeInformation;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class SerialPartTypization implements BammBase {
    public final static String BAMM_ID = "urn:bamm:io.catenax.serial_part_typization:1.1.0#SerialPartTypization";

    private String catenaXId;
    private List<SPTLocalIdentifier> localIdentifiers;
    private SPTManufacturingInformation manufacturingInformation;
    private SPTPartTypeInformation partTypeInformation;

    @Override public String getBammId() {
        return BAMM_ID;
    }

    public String getLocelIdentifier(String key) {
        if(localIdentifiers == null) {
            return null;
        }

        for (SPTLocalIdentifier localIdentifier : localIdentifiers) {
            if(localIdentifier.getKey().equals(key)) {
                return localIdentifier.getValue();
            }
        }

        return null;
    }
}
