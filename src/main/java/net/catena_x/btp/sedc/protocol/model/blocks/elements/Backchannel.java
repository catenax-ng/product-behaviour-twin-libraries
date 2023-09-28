package net.catena_x.btp.sedc.protocol.model.blocks.elements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Backchannel {
    private String bpn;
    private String address;
    private String assetId;
}
