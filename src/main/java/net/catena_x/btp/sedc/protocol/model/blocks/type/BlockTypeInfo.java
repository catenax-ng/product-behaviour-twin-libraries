package net.catena_x.btp.sedc.protocol.model.blocks.type;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.blocks.*;

import javax.validation.constraints.NotNull;

public class BlockTypeInfo {

    public static BlockType blockTypeFromShortcut(final char shortcut) throws BtpException {
        return switch (shortcut) {
            case 'C' -> BlockType.CONFIG;
            case 'E' -> BlockType.END;
            case 'H' -> BlockType.HEADER;
            case 'D' -> BlockType.DATA;
            case 'K' -> BlockType.KEEL_ALIVE;
            default -> throw new BtpException("Unknown block type \"" + shortcut + "\"!" );
        };
    }

    public static char  blockTypeToShortcut(final BlockType blockType) {
        return switch (blockType) {
            case CONFIG -> 'C';
            case END -> 'E';
            case HEADER -> 'H';
            case DATA -> 'D';
            case KEEL_ALIVE -> 'K';
        };
    }

    public static String blockTypeToString(final BlockType blockType) {
        return switch (blockType) {
            case CONFIG -> "config";
            case END -> "end";
            case HEADER -> "header";
            case DATA -> "data";
            case KEEL_ALIVE -> "keepAlive";
        };
    }

    public static BlockType blockTypeFromClass(@NotNull final Class<?> type) throws BtpException {
        if(type.equals(ConfigBlock.class)) {
            return BlockType.CONFIG;
        } else if(type.equals(EndBlock.class)) {
            return BlockType.END;
        } else if(type.equals(HeaderBlock.class)) {
            return BlockType.HEADER;
        } else if(type.equals(DataBlock.class)) {
            return BlockType.DATA;
        } else if(type.equals(KeepAliveBlock.class)) {
            return BlockType.KEEL_ALIVE;
        }

        throw new BtpException("Unknown block type class!" );
    }
}
