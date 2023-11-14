package net.catena_x.btp.sedc.transmit;

import net.catena_x.btp.libraries.util.exceptions.BtpException;

import javax.validation.constraints.NotNull;

public interface SenderInterface<T> {
    void send(@NotNull final T value, @NotNull final String id) throws BtpException;
    void keepAlive() throws BtpException;
}