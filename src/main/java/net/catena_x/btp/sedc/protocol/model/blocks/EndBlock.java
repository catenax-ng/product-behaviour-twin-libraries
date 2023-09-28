package net.catena_x.btp.sedc.protocol.model.blocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.sedc.protocol.model.Block;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EndBlock extends Block {
    private boolean end = true;

    @Override
    public char getShortcut() {
        return 'E';
    }
}