package net.catena_x.btp.rul.oem.backend.model.dto.serviceoption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLServiceOptionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLServiceOption {
    private RuLServiceOptionType key;
    private String value;
}
