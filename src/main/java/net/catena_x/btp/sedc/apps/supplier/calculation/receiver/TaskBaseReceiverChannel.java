package net.catena_x.btp.sedc.apps.supplier.calculation.receiver;

import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.transmit.ReceiverChannelImplBase;
import net.catena_x.btp.sedc.transmit.ReceiverChannelInterface;

public class TaskBaseReceiverChannel extends ReceiverChannelImplBase
        implements ReceiverChannelInterface<PeakLoadRawValues> {}