package net.catena_x.btp.sedc.protocol.model.blocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.sedc.protocol.model.Block;
import net.catena_x.btp.sedc.protocol.model.blocks.elements.Backchannel;
import net.catena_x.btp.sedc.protocol.model.blocks.elements.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConfigBlock extends Block {
    private Stream stream;
    private Backchannel backchannel;

    @Override
    public char getShortcut() {
        return 'C';
    }
}
