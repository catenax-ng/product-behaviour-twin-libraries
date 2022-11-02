package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Cacheable(false)
@Table(name = "vehicles", uniqueConstraints={@UniqueConstraint(columnNames = {"id"}),
                                             @UniqueConstraint(columnNames = {"van"})})
@Getter
@Setter
public class Vehicle {
    @Id
    @Column(name="id", length=50, nullable=false)
    private String id;

    @Column(name="van", length=50, nullable=false)
    private String van;

    @Column(name="production_date", nullable=false, unique=false)
    private Instant productionDate;

    @Column(name="update_timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
            insertable=false, updatable=false, nullable=false)
    private Instant updateTimestamp;

    @Column(name="newest_collective_id", length=50)
    private String newestCollectiveId;

    @Column(name="newest_adaption_values_id", length=50)
    private String newestAdaptionValuesId;
}