package net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.PeakLoadResult;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeakLoadRingBufferElement {
    private String id = null;
    private Instant timestamp = null;
    private PeakLoadRawValues rawValues = null;
    private PeakLoadResult result = null;
}