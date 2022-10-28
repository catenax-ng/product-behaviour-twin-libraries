package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "loadcollectives")
@Getter
@Setter
@NamedNativeQuery(name ="LoadCollectives.getNewerThan",
        query ="SELECT (id, vehicleid, timestamp, productionDate, mileage, operatingseconds, loadcollectives) FROM loadcollectives WHERE timestamp >= ?1")
@NamedNativeQuery(name ="LoadCollectives.upload",
        query ="INSERT INTO loadcollectives (id, vehicleid, timestamp, productionDate, mileage, operatingseconds, loadcollectives) VALUES ?1")
public class LoadCollectives {
    @Id
    @Column(length=50, nullable=false, unique=true)
    private String id;

    @Column(length=50, nullable=false)
    private String vehicleid;

    @Column(nullable=false)
    private java.time.Instant timestamp;

    @Column(nullable=false)
    private java.time.Instant productionDate; //Redundant

    @Column(nullable=false)
    private float mileage;

    @Column(nullable=false)
    private long operatingseconds;

    @Type(type = "string-array")
    @Column(nullable=false,columnDefinition="text[]")
    private String[] loadcollective;
}
