package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
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

    @OneToOne(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH}, optional=true)
    @JoinColumn(name="newest_telemetrics_id", referencedColumnName = "id", nullable = true)
    private TelemetricsData telemetricsData;
}