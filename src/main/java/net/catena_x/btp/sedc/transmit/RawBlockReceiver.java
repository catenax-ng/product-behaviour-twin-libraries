package net.catena_x.btp.sedc.transmit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.protocol.model.RawBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.type.BlockType;
import net.catena_x.btp.sedc.protocol.model.blocks.type.BlockTypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.io.IOException;

public class RawBlockReceiver {
    public class Result {
        public HeaderBlock header = null;
        public RawBlock content = null;
        public boolean successful = false;
        public boolean closed = false;
    }

    private BufferedInputStream inputStream = null;
    final ObjectMapper objectMapper = ObjectMapperFactoryBtp.createObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(RawBlockReceiver.class);

    public void init(@NotNull final BufferedInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void close() throws BtpException {
        try {
            inputStream.close();
        } catch (final IOException exception) {
            throw new BtpException(exception);
        }
    }

    public Result receiveNext() throws BtpException {

        final HeaderBlock headerBlock = receiveTyped(HeaderBlock.class);
        if(headerBlock == null) {
            return new Result();
        }

        final RawBlock block = RawBlock.fromStream(inputStream);
        if(block.getBlockType() == BlockType.END) {
            logger.info("Stream closed by partner.");
            final Result result = new Result();
            result.closed = true;
            return result;
        }

        if(block.getBlockType() != BlockType.DATA) {
            throw new BtpException("Expected next block to be a data block, but get block with shortcut \""
                    + block.getShortcut() + "\"!");
        }

        final Result result = new Result();
        result.header = headerBlock;
        result.content = block;
        result.successful = true;
        return result;
    }

    private <T> T receiveTyped(@NotNull final Class<T> typeOfT) throws BtpException {
        final RawBlock block = RawBlock.fromStream(inputStream);

        if(block.getBlockType() == BlockType.END) {
            logger.info("Stream closed by partner.");

            if(BlockTypeInfo.blockTypeFromClass(typeOfT) != BlockType.END) {
                return null;
            }
        }

        if(BlockTypeInfo.blockTypeFromClass(typeOfT) != block.getBlockType()) {
            throw new BtpException("Expected a config block, but get block with shortcut \""
                    + block.getShortcut() + "\"!");
        }

        try {
            return objectMapper.readValue(block.getContent(), typeOfT);
        } catch (final JsonProcessingException exception) {
            throw new BtpException(exception);
        }
    }
}