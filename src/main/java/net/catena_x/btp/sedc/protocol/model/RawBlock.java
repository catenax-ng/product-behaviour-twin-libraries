package net.catena_x.btp.sedc.protocol.model;

import lombok.Getter;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.blocks.type.BlockType;
import net.catena_x.btp.sedc.protocol.model.blocks.type.BlockTypeInfo;
import net.catena_x.btp.sedc.transmit.RawBlockReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Getter
public class RawBlock {
    private char shortcut = '\0';
    private int length = 0;
    private String content = null;
    private static final Logger logger = LoggerFactory.getLogger(RawBlock.class);

    private RawBlock(@NotNull final String content, final char shortcut, final int length) {
        this.shortcut = shortcut;
        this.length = length;
        this.content = content;
    }

    public BlockType getBlockType() throws BtpException {
        return BlockTypeInfo.blockTypeFromShortcut(shortcut);
    }

    public static RawBlock fromStream(@NotNull final BufferedInputStream stream) throws BtpException {
        try {
            logger.info("Wait for block type.");
            waitForBytes(stream, 1);
            final int nextByte = stream.read();

            if(nextByte < 0) {
                throw new BtpException("Stream is closed!");
            }

            final char shortcut = switch (nextByte) {
                case (int)'C', (int)'E', (int)'H', (int)'D' -> (char)nextByte;
                default -> throw new BtpException("Unknown block type " + (char)nextByte + "!");
            };

            logger.info("Block type " + shortcut + ", wait for length.");

            waitForBytes(stream, 6);
            final byte[] lenBuffer = new byte[6];
            final int byteCountLen = stream.read(lenBuffer, 0, 6);
            if(byteCountLen < 6) {
                throw new BtpException("Stream has been closed!");
            }
            final int length = (100000 * charByteToDigit(lenBuffer[0])) + (10000 * charByteToDigit(lenBuffer[1]))
                    + (1000 * charByteToDigit(lenBuffer[2])) + (100 * charByteToDigit(lenBuffer[3]))
                    + (10 * charByteToDigit(lenBuffer[4])) + charByteToDigit(lenBuffer[5]);

            logger.info("Length is " + length + ", wait for content.");

            waitForBytes(stream, length);
            final byte[] contentBuffer = new byte[length];
            final int byteCountContent = stream.read(contentBuffer, 0, length);
            if(byteCountContent != length) {
                throw new BtpException("Stream has been closed!");
            }
            final String content = new String(contentBuffer, StandardCharsets.UTF_8);

            logger.info("Content is \"" + content + "\".");

            return new RawBlock(content, shortcut, length);
        } catch (final IOException exception) {
            throw new BtpException(exception);
        }
    }

    private static int charByteToDigit(final byte charByte) {
        return ((int)charByte - (int)'0');
    }

    private static void waitForBytes(@NotNull final BufferedInputStream stream,
                                     final int byteCount) throws IOException {
        while(stream.available() < byteCount) {
            try {
                Thread.sleep(100);
            } catch (final InterruptedException exception) {
            }
        }
    }
}
