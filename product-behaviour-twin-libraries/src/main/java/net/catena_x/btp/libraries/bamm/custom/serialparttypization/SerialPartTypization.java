package net.catena_x.btp.libraries.bamm.custom.serialparttypization;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.base.BammBase;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTLocalIdentifier;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTManufacturingInformation;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTPartTypeInformation;

import java.util.List;

@Getter
@Setter
public class SerialPartTypization implements BammBase {
    public final static String BAMM_ID = "urn:bamm:io.catenax.serial_part_typization:1.1.0#SerialPartTypization";

    private String catenaXId;
    private List<SPTLocalIdentifier> localIdentifiers;
    private SPTManufacturingInformation manufacturingInformation;
    private SPTPartTypeInformation partTypeInformation;

    @Override public String getBammId() {
        return BAMM_ID;
    }
}
