package net.catena_x.btp.libraries.oem.backend.model.dto.infoitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoItem {
    private InfoKey key;
    private String value;
    private Instant queryTimestamp;
}
