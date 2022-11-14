package net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.base.BammBase;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective.items.CLCBody;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective.items.CLCHeader;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective.items.CLCMetaData;

@Getter
@Setter
public class ClassifiedLoadSpectrum implements BammBase {
    public final static String BAMM_ID = "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum";

    private String targetComponentID;
    private CLCMetaData metadata;
    private CLCHeader header;
    private CLCBody body;

    @Override public String getBammId() {
        return BAMM_ID;
    }
}
