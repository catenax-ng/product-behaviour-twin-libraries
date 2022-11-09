package net.catena_x.btp.libraries.oem.backend.database.rawdata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoItem {
    public enum InfoKey {
        dataversion,
        adaptionvalueinfo,
        collectiveinfo
    }

    private InfoKey key;
    private String value;
    private Instant queryTimestamp;
}
