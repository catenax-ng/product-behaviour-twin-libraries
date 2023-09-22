package net.catena_x.btp.libraries.oem.backend.model.dto.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sync {
    private long syncCounter;
    private Instant queryTimestamp;
}
