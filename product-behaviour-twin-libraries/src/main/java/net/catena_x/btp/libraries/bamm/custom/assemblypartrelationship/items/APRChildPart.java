package net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.common.BammQuantity;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APRChildPart {
    private String childCatenaXId;
    private BammQuantity quantity;
    private String lifecycleContext;
    private Instant assembledOn;
    private Instant lastModifiedOn;
}
