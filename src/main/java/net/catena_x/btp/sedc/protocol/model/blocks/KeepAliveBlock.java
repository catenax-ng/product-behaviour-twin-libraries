package net.catena_x.btp.sedc.protocol.model.blocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.sedc.protocol.model.Block;

@AllArgsConstructor
@Getter
@Setter
public class KeepAliveBlock extends Block {
    @Override public char getShortcut() {
        return 'K';
    }
}

