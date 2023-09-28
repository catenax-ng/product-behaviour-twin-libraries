package net.catena_x.btp.sedc.protocol.model.blocks;

import lombok.Getter;
import net.catena_x.btp.sedc.protocol.model.Block;

import javax.validation.constraints.NotNull;

@Getter
public class DataBlock<T> extends Block {
    private T data = null;

    public DataBlock(@NotNull final T data) {
        this.data = data;
    }

    @Override
    public char getShortcut() {
        return 'D';
    }
}