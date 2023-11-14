package net.catena_x.btp.sedc.protocol.model.channel;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.ContentMapperInterface;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;

import javax.validation.constraints.NotNull;

public interface StreamChannelWriter {
    <T> void write(@NotNull final HeaderBlock header, @NotNull final DataBlock<T> data,
                   @NotNull final ContentMapperInterface contentMapper) throws BtpException;
    void keepAlive() throws BtpException;
    void close() throws BtpException;
    boolean isOpen();
}