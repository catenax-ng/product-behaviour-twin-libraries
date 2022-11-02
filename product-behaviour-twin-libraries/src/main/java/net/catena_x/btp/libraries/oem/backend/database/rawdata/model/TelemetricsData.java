package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.AdaptionValuesPersistenceConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsDBEntityDestination;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Cacheable(false)
@Table(name = "telemetrics_data")
@Getter
@Setter
public class TelemetricsData implements TelemetricsDBEntityDestination {
    @Id
    @Column(name="id", length=50, nullable=false, unique=true)
    private String id;

    @Column(name="storage_timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
            insertable=false, updatable=false, nullable=false)
    private Instant storageTimestamp;

    @Column(name="vehicle_id", length=50, nullable=false)
    private String vehicleId;

    @Column(name = "creation_timestamp", nullable=false)
    private Instant creationTimestamp;

    @Column(name="mileage", nullable=false)
    private float mileage;

    @Column(name="operating_seconds", nullable=false)
    private long operatingSeconds;

    @Column(name="load_collectives", nullable=false)
    @ElementCollection
    private List<String> loadCollectives;

    @Column(name="adaption_values", nullable=false)
    @Convert(converter = AdaptionValuesPersistenceConverter.class)
    private List<double[]> adaptionValues;
}
