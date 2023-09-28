package net.catena_x.btp.sedc.protocol.model.blocks.elements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stream {
    private String streamId;
    private String version;
    private String streamType;
    private Instant timestamp;
}
