package net.catena_x.btp.sedc.apps.oem.backend.calculation;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.sedc.apps.oem.backend.buffer.RingBufferInterface;
import net.catena_x.btp.sedc.apps.oem.backend.receiver.ResultReceiverChannel;
import net.catena_x.btp.sedc.apps.oem.backend.sender.RawdataSender;

@Getter
@Setter
public class CalculationConnection {
    private String streamId = null;
    private ResultReceiverChannel receiver = null;
    private RawdataSender sender = null;
    private RingBufferInterface ringBuffer = null;
}
