package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsDBEntityDestination;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "loadcollectives")
@Getter
@Setter
@NamedNativeQuery(name ="LoadCollectives.getNewerThan",
        query ="SELECT * FROM loadcollectives WHERE timestamp >= ?1")
@NamedNativeQuery(name ="LoadCollectives.upload",
        query ="INSERT INTO loadcollectives (id, vehicleid, timestamp, mileage, operatingseconds, loadcollectives) VALUES ?1")
public class LoadCollectives implements TelemetricsDBEntityDestination {
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
    @ElementCollection
    private List<String> loadcollectives;
}
