package net.catena_x.btp.hi.oem.backend.hi_service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.util.S3EDCRequestMetadata;

@Getter
@Setter
@AllArgsConstructor
public class HIS3EDCRequestMetadata extends S3EDCRequestMetadata {
    private long lastCounter;
}
