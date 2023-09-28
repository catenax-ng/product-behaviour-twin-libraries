package net.catena_x.btp.sedc.protocol.model.channel;

import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;

import javax.validation.constraints.NotNull;

public interface StreamChannelWriter {
    void open(@NotNull final ConfigBlock config, @NotNull final String address) /*throws*/;
    <T> void write(@NotNull final HeaderBlock header, @NotNull final DataBlock<T> data) /*throws*/;
    void close();
    boolean isOpen();
}