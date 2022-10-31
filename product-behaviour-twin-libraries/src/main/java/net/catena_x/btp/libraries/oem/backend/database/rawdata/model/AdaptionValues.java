package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.AdaptionValuesPersistenceConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsDBEntityDestination;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "adaptionvalues")
@Getter
@Setter
@NamedNativeQuery(name ="AdaptionValues.getNewerThan",
        query ="SELECT * FROM adaptionvalues WHERE timestamp >= ?1")
@NamedNativeQuery(name ="AdaptionValues.upload",
        query ="INSERT INTO adaptionvalues (id, vehicleid, timestamp, mileage, operatingseconds, adaptionvalues) VALUES ?1")
public class AdaptionValues implements TelemetricsDBEntityDestination {
    @Id
    @Column(length=50, nullable=false, unique=true)
    private String id;

    @Column(length=50, nullable=false)
    private String vehicleid;

    @Column(nullable=false)
    private java.time.Instant timestamp;

    @Column(nullable=false)
    private float mileage;

    @Column(nullable=false)
    private long operatingseconds;

    @Column(nullable=false)
    @Convert(converter = AdaptionValuesPersistenceConverter.class)
    private List<double[]> adaptionvalues;
}
