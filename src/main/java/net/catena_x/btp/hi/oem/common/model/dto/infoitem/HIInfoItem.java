package net.catena_x.btp.hi.oem.common.model.dto.infoitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.hi.oem.common.model.enums.HIInfoKey;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIInfoItem {
    private HIInfoKey key;
    private String value;
}
