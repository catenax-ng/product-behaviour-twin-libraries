package net.catena_x.btp.sedc.transmit;

import net.catena_x.btp.libraries.edc.model.Edr;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotNull;

public interface ReceiverChannelInterface<T> {
    void open(@NotNull final Edr edr, @NotNull final ConfigBlock config, @Nullable HttpHeaders headers)
            throws BtpException;
}