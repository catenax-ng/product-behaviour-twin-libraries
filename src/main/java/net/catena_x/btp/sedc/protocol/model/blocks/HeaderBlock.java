package net.catena_x.btp.sedc.protocol.model.blocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.sedc.protocol.model.Block;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HeaderBlock extends Block {
    private String contentType;
    private String contentVersion;
    private Instant timestamp;
    private String id;

    @Override
    public char getShortcut() {
        return 'H';
    }
}