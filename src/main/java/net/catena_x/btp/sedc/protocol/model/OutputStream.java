package net.catena_x.btp.sedc.protocol.model;

import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;
import net.catena_x.btp.sedc.protocol.model.channel.StreamChannelWriter;

import javax.validation.constraints.NotNull;

public class OutputStream implements StreamChannelWriter {
    public void open(@NotNull final ConfigBlock config, @NotNull final String address) {

    }

    public <T> void write(@NotNull final HeaderBlock header, @NotNull final DataBlock<T> data) {

    }

    public void close() {

    }

    public boolean isOpen() {
        return false;
    }
}
