package net.catena_x.btp.sedc.protocol.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.protocol.model.blocks.type.BlockType;

public abstract class Block {
    @JsonIgnore public abstract char getShortcut();

    @JsonIgnore public BlockType getBlockType() throws BtpException {
        return switch (getShortcut()) {
            case 'C' -> BlockType.CONFIG;
            case 'E' -> BlockType.END;
            case 'H' -> BlockType.HEADER;
            case 'D' -> BlockType.DATA;
            default -> throw new BtpException("Unknown block type \"" + getShortcut() + "\"!" );
        };
    }
}
