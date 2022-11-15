package net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Instant creationTimestamp;
    private float mileage;
    private long operatingSeconds;
    private List<String> loadSpectra;
    private List<double[]> adaptionValues;
}