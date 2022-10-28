package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "vehicles", uniqueConstraints={@UniqueConstraint(columnNames = {"id"}),
                                             @UniqueConstraint(columnNames = {"van"})})
@NamedNativeQuery(name ="Vehicle.register",
        query ="INSERT INTO vehicles (id, van, productionDate) VALUES(?1, ?2, ?3)")
@NamedNativeQuery(name ="Vehicle.getSinceDate",
        query ="SELECT * FROM vehicles WHERE updateTimestamp >= ?1")
@Getter
@Setter
public class Vehicle {
    @Id
    @Column(length=50, nullable=false)
    private String id;

    @Column(length=50, nullable=false)
    private String van;

    @Column(nullable=false, unique=false)
    private java.time.Instant productionDate;

    @Column()
    private java.time.Instant updateTimestamp;

    @Column(length=50)
    private String newestcollectiveid;

    @Column(length=50)
    private String newestadaptionvaluesid;
}
