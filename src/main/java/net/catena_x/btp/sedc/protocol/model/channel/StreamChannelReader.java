package net.catena_x.btp.sedc.protocol.model.channel;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.ContentMapperInterface;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;

public interface StreamChannelReader {
    void open(@NotNull final BufferedReader stream) throws BtpException;
    HeaderBlock readNextHeader() throws BtpException;
    <T> DataBlock<T> readNextData(@NotNull final HeaderBlock header,
                                  @NotNull final ContentMapperInterface contentMapper) throws BtpException;
    void close();
    boolean isOpen();
}