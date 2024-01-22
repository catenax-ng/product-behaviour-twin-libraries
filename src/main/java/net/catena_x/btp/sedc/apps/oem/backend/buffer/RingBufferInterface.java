package net.catena_x.btp.sedc.apps.oem.backend.buffer;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer.PeakLoadRingBufferElement;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.model.rawvalues.PeakLoadRawValues;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public interface RingBufferInterface {
    /* If timestamp is null, the current timestamp (Instant.now()) is used. */
    void addRawdata(@NotNull final String id, @Nullable final Instant timestamp,
                    @NotNull final PeakLoadRawValues rawValues) throws BtpException;
    void addResult(@NotNull final String id, @NotNull final PeakLoadResult result) throws BtpException;
    List<PeakLoadRingBufferElement> getAll() throws BtpException;
    long getCount() throws BtpException;
    long getMaxCount() throws BtpException;
}
