package net.catena_x.btp.libraries.bamm.custom.adaptionvalues;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdaptionValues {
    public final static String BAMM_ID = "urn:bamm:mockup.digitaltwin:1.0.0#AdaptionValues";

    private BammStatus status;
    private double[] values;
}
