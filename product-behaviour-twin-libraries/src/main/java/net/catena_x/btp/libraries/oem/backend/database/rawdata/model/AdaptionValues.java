package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "adaptionvalues")
@Getter
@Setter
@NamedNativeQuery(name ="AdaptionValues.getNewerThan",
        query ="SELECT (id, vehicleid, timestamp, productionDate, mileage, operatingseconds, adaptionvalues) FROM adaptionvalues WHERE timestamp >= ?1")
@NamedNativeQuery(name ="AdaptionValues.upload",
        query ="INSERT INTO adaptionvalues (id, vehicleid, timestamp, productionDate, mileage, operatingseconds, adaptionvalues) VALUES ?1")
public class AdaptionValues {
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

    @Type(type = "double-array")
    @Column(nullable=false, columnDefinition = "float8[]")
    private double[] adaptionvalues;
}
