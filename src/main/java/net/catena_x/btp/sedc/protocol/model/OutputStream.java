package net.catena_x.btp.sedc.protocol.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.apps.supplier.calculation.sender.ResultSender;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.EndBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;
import net.catena_x.btp.sedc.protocol.model.channel.StreamChannelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public class OutputStream implements StreamChannelWriter {
    private final ObjectMapper objectMapper = ObjectMapperFactoryBtp.createObjectMapper();
    private java.io.OutputStream outputStream = null;
    private boolean open = false;
    private final Logger logger = LoggerFactory.getLogger(ResultSender.class);

    @Override
    public <T> void write(@NotNull final HeaderBlock header, @NotNull final DataBlock<T> data,
                          @NotNull final ContentMapperInterface contentMapper) throws BtpException {
        if(outputStream == null) {
            throw new BtpException("Output stream is not initialized!");
        }

        try {
            sendBlock(header.getShortcut(), objectMapper.writeValueAsString(header));
            sendBlock(data.getShortcut(), contentMapper.serialize(header, data));
        } catch (final IOException exception) {
            throw new BtpException(exception);
        }
    }

    @Override
    public void close() throws BtpException {
        if(!isOpen()) {
            return;
        }

        if(outputStream == null) {
            throw new BtpException("Output stream is not initialized!");
        }

        try {
            final EndBlock endBlock = new EndBlock();
            sendBlock(endBlock.getShortcut(), objectMapper.writeValueAsString(endBlock));
            outputStream.close();
        } catch (final IOException exception) {
            throw new BtpException(exception);
        }

        open = false;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    public void init(@NotNull final java.io.OutputStream outputStream) {
        open = (outputStream != null);
        this.outputStream = outputStream;
    }

    private void sendBlock(final char prefix, @NotNull final String content) throws BtpException {
        try {
            //logger.info("Send block of type " + prefix + ".");
            outputStream.write((int)prefix);

            final byte[] contentUtf8 = content.getBytes("utf-8");
            outputStream.write(String.format("%06d", contentUtf8.length).getBytes("utf-8"));
            outputStream.write(contentUtf8);

            outputStream.flush();
            //logger.info("Stream flushed.");
        } catch (final IOException exception) {
            throw new BtpException(exception);
        }
    }
}
