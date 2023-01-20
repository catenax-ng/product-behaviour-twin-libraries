package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.base.BammBase;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.CLSBody;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.CLSHeader;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.CLSMetaData;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassifiedLoadSpectrum implements BammBase {
    public final static String BAMM_ID = "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum";

    private String targetComponentID;
    private CLSMetaData metadata;
    private CLSHeader header;
    private CLSBody body;

    @Override public String getBammId() {
        return BAMM_ID;
    }
}
