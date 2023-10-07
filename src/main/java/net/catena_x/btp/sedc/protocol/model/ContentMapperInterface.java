package net.catena_x.btp.sedc.protocol.model;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;

import javax.validation.constraints.NotNull;

public interface ContentMapperInterface {
    DataBlock deserialize(@NotNull final String serializedBlock, @NotNull final String contentType)
            throws BtpException;

    <T> String serialize(@NotNull final HeaderBlock headerBlock, @NotNull final DataBlock<T> dataBlock)
            throws BtpException;
}
