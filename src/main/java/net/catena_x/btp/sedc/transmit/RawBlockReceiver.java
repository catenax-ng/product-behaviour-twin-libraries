package net.catena_x.btp.sedc.transmit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.protocol.model.RawBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.EndBlock;
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
        public ConfigBlock config = null;
        public HeaderBlock header = null;
        public RawBlock content = null;
        public EndBlock end = null;
    }

    private BufferedInputStream inputStream = null;
    private final ObjectMapper objectMapper = ObjectMapperFactoryBtp.createObjectMapper();
    private long keepAliveCount = 0L;

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
        HeaderBlock headerBlock = null;

        while(true) {
            final RawBlock nextBlock = RawBlock.fromStream(inputStream);

            switch(nextBlock.getBlockType()) {
                case CONFIG: {
                    if(headerBlock != null) {
                        throw new BtpException("Content data block was expected, but received config block!");
                    }

                    final Result result = new Result();
                    result.config = convertBlock(nextBlock, ConfigBlock.class);
                    return result;
                }

                case END: {
                    final Result result = new Result();
                    result.end = convertBlock(nextBlock, EndBlock.class);
                    logger.info("Stream closed by partner.");
                    return result;
                }

                case HEADER: {
                    if(headerBlock != null) {
                        throw new BtpException("Content data block was expected, but received header block again!");
                    }

                    headerBlock = convertBlock(nextBlock, HeaderBlock.class);
                    break;
                }

                case DATA: {
                    final Result result = new Result();
                    result.header = headerBlock;
                    result.content = nextBlock;
                    return result;
                }

                case KEEL_ALIVE: {
                    /* Ignore. */
                    if(((keepAliveCount < 500000L) && ((keepAliveCount == 0L)
                            || (keepAliveCount < 10L))) || (keepAliveCount % 4000L == 0L)) {
                        logger.info("Keep alive received (" + keepAliveCount + ").");
                    }
                    keepAliveCount += 1L;
                    break;
                }

                default: {
                    throw new BtpException("Unknown block type \"" + nextBlock.getShortcut() + "\" received!");
                }
            }
        }
    }

    public <T> T convertBlock(@NotNull final RawBlock rawBlock, @NotNull final Class<T> typeOfT) throws BtpException {
        try {
            return objectMapper.readValue(rawBlock.getContent(), typeOfT);
        } catch (final JsonProcessingException exception) {
            throw new BtpException(exception);
        }
    }
}