package net.catena_x.btp.sedc.protocol.model;

import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;
import net.catena_x.btp.sedc.protocol.model.channel.StreamChannelReader;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;

public class InputStream implements StreamChannelReader {
    @Override
    public void open(@NotNull final BufferedReader stream)
    {
    }

    @Override
    public HeaderBlock readNextHeader() {
        return new HeaderBlock();
    }

    @Override
    public <T> DataBlock<T> readNextData(@NotNull final HeaderBlock header) {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
