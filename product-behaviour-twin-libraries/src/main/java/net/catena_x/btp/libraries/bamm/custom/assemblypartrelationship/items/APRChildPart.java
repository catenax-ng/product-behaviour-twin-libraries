package net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.items;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.common.BammQuantity;

import java.time.Instant;

@Getter
@Setter
public class APRChildPart {
    private String childCatenaXId;
    private BammQuantity quantity;
    private String lifecycleContext;
    private Instant assembledOn;
    private Instant lastModifiedOn;
}
