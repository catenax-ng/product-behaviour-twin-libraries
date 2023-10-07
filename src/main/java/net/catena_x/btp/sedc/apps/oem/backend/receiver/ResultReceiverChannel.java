package net.catena_x.btp.sedc.apps.oem.backend.receiver;

import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.transmit.ReceiverChannelImplBase;
import net.catena_x.btp.sedc.transmit.ReceiverChannelInterface;

public class ResultReceiverChannel extends ReceiverChannelImplBase
        implements ReceiverChannelInterface<PeakLoadResult> {}