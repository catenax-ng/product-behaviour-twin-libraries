package net.catena_x.btp.sedc.apps.oem.backend.generator;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.model.rawvalues.PeakLoadRawValues;

public interface StreamValuesSource<T> {
    PeakLoadRawValues getNext() throws BtpException;
}