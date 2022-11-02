package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Cacheable(false)
@Table(name = "info")
@Getter
@Setter
public class InfoItem {
    public enum InfoKey {
        dataversion,
        adaptionvalueinfo,
        collectiveinfo
    }

    @Id
    @Column(name="key", length=50, nullable=false, unique=true)
    @Enumerated(EnumType.STRING)
    private InfoKey key;

    @Column(name="value", nullable=false)
    private String value;

    @Formula("current_timestamp")
    @Column(name="query_timestamp", nullable=false)
    private Instant queryTimestamp;
}
