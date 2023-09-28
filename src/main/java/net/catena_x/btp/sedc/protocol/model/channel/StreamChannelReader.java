package net.catena_x.btp.sedc.protocol.model.channel;

import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;

public interface StreamChannelReader {
    void open(@NotNull final BufferedReader stream) /*throws*/;
    HeaderBlock readNextHeader() /*throws*/;
    <T> DataBlock<T> readNextData(@NotNull final HeaderBlock header) /*throws*/;
    void close();
    boolean isOpen();
}
