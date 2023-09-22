package net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelematicsData {
    private String id;
    private Instant storageTimestamp;
    private long syncCounter;
    private String vehicleId;
    private List<ClassifiedLoadSpectrum> loadSpectra;
    private List<AdaptionValues> adaptionValues;
}
