package net.catena_x.btp.sedc.protocol.model;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;
import net.catena_x.btp.sedc.protocol.model.channel.StreamChannelReader;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;

public class InputStream implements StreamChannelReader {
    @Override
    public void open(@NotNull final BufferedReader stream) throws BtpException
    {
    }

    @Override
    public HeaderBlock readNextHeader() throws BtpException {
        return new HeaderBlock();
    }

    @Override
    public <T> DataBlock<T> readNextData(@NotNull final HeaderBlock header,
                                         @NotNull final ContentMapperInterface contentMapper) throws BtpException {
        final String serializedBlock = null;
        return contentMapper.deserialize(serializedBlock, header.getContentType());
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
