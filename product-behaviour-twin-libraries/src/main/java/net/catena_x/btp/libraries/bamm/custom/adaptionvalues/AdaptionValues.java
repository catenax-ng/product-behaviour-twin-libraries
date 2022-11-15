package net.catena_x.btp.libraries.bamm.custom.adaptionvalues;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.common.BammStatus;

//Not specified as BAMM.
@Getter
@Setter
public class AdaptionValues {
    public final static String BAMM_ID = "urn:bamm:mockup.digitaltwin:1.0.0#AdaptionValues";

    BammStatus status;
    double[] values;
}
