package net.catena_x.btp.sedc.apps.oem.dashboard.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeakLoadRingBufferResultApiDAO {
    private String errorMessage = null;
    private List<PeakLoadRingBufferElementApiDAO> ringbuffer = null;
}