package net.catena_x.btp.rul.oem.backend.rul_service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.rul.oem.backend.util.enums.RuLStarterCalculationType;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RuLStarterApiResult {
    private Instant timestamp;
    private RuLStarterCalculationType type;
    private String message;
}
