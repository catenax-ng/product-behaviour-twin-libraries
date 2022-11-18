package net.catena_x.btp.libraries.bamm.custom.adaptionvalues;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.common.BammStatus;

//Not specified as BAMM.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdaptionValues {
    public final static String BAMM_ID = "urn:bamm:mockup.digitaltwin:1.0.0#AdaptionValues";

    BammStatus status;
    double[] values;
}
