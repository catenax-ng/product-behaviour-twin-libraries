package net.catena_x.btp.sedc.transmit;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotNull;

public interface ReceiverChannelInterface<T> {
    void open(@NotNull final String partnerConnectorAddress,
              @NotNull final String assetId, @NotNull final ConfigBlock config,
              @Nullable final HttpHeaders headers) throws BtpException;
}