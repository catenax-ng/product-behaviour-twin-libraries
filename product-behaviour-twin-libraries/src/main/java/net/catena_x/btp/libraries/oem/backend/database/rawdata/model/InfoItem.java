package net.catena_x.btp.libraries.oem.backend.database.rawdata.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
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
    @Column(length=50, nullable=false, unique=true)
    @Enumerated(EnumType.STRING)
    private InfoKey key;

    @Column(nullable=false)
    private String value;
}
