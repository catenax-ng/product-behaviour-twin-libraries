package net.catena_x.btp.sedc.apps.oem.backend.buffer;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer.PeakLoadRingBufferElement;
import net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer.PeakLoadRingBufferTable;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

@Component
public class RingBufferImpl implements RingBufferInterface {
    @Autowired PeakLoadRingBufferTable peakLoadRingBufferTable;

    private LinkedList<PeakLoadRingBufferElement> values = new LinkedList<>();
    private Semaphore mutex = new Semaphore(1);

    private final Logger logger = LoggerFactory.getLogger(RingBufferImpl.class);
    private long count = 0;
    private long maxCount = 0;

    @Override
    public void addRawdata(@NotNull final String id, @Nullable final Instant timestamp,
                           @NotNull final PeakLoadRawValues rawValues) throws BtpException {
        try {
            mutex.acquire();
            addRawdataToListAndDatabase(id, timestamp, rawValues);
        } catch (final InterruptedException exception) {
            throw new BtpException(exception);
        } finally {
            mutex.release();
        }
    }

    @Override
    public void addResult(@NotNull final String id, @NotNull final PeakLoadResult result) throws BtpException {
        try {
            mutex.acquire();
            addResultToListAndDatabase(id, result);
        } catch (final InterruptedException exception) {
            throw new BtpException(exception);
        } finally {
            mutex.release();
        }
    }

    @Override
    public List<PeakLoadRingBufferElement> getAll() throws BtpException {
        try {
            mutex.acquire();
            return values;
        } catch (final InterruptedException exception) {
            throw new BtpException(exception);
        } finally {
            mutex.release();
        }
    }

    @Override
    public long getCount() throws BtpException {
        try {
            mutex.acquire();
            return count;
        } catch (final InterruptedException exception) {
            throw new BtpException(exception);
        } finally {
            mutex.release();
        }
    }

    @Override
    public long getMaxCount() throws BtpException {
        try {
            mutex.acquire();
            return maxCount;
        } catch (final InterruptedException exception) {
            throw new BtpException(exception);
        } finally {
            mutex.release();
        }
    }

    public void init(long maxCount) throws BtpException {
        try {
            mutex.acquire();
            this.maxCount = maxCount;
            loadFromDatabase();
        } catch (final InterruptedException exception) {
            throw new BtpException(exception);
        } finally {
            mutex.release();
        }
    }

    private void addRawdataToListAndDatabase(@NotNull final String id, @Nullable final Instant timestamp,
                                             @NotNull final PeakLoadRawValues rawValues) throws BtpException {
        final PeakLoadRingBufferElement valuesSet = getById(id);
        if(valuesSet != null) {
            throw new BtpException("There were already raw values for id \"" + id + "\"!");
        }

        final PeakLoadRingBufferElement newValuesSet = new PeakLoadRingBufferElement(
                id, (timestamp != null)? timestamp : Instant.now(), rawValues, null);

        values.add(newValuesSet);

        if(count < maxCount) {
            ++count;
            addToAndRemoveFromDatabase(newValuesSet, null);
        } else {
            final PeakLoadRingBufferElement removedValuesSet = values.remove(0);
            addToAndRemoveFromDatabase(newValuesSet, removedValuesSet);
        }
    }

    private void addResultToListAndDatabase(@NotNull final String id,
                                            @NotNull final PeakLoadResult result) throws BtpException {

        final PeakLoadRingBufferElement valuesSet = getById(id);
        if(valuesSet == null) {
            logger.warn("Id \"" + id + "\" not found in raw data, maybe too old? Result is ignored!");
            return;
        }

        if(valuesSet.getResult() != null) {
            throw new BtpException("There was already a result for id \"" + id + "\"!");
        }

        valuesSet.setResult(result);
        addResultToDatabase(valuesSet);
    }

    private PeakLoadRingBufferElement getById(@NotNull final String id) {
        final Iterator<PeakLoadRingBufferElement> iterator = values.iterator();
        while(iterator.hasNext()) {
            final PeakLoadRingBufferElement currentElement = iterator.next();
            if(currentElement.getId().equals(id)) {
                return currentElement;
            }
        }

        return null;
    }

    private void loadFromDatabase() throws BtpException {
        values.clear();

        final List<PeakLoadRingBufferElement> buffer = peakLoadRingBufferTable.getAllSortByTimestampNewTransaction();
        final Iterator<PeakLoadRingBufferElement> iterator = buffer.iterator();

        while(iterator.hasNext()) {
            values.add(iterator.next());
        }
    }

    private void addToAndRemoveFromDatabase(@NotNull final PeakLoadRingBufferElement add,
                                            @Nullable final PeakLoadRingBufferElement remove) throws BtpException {
        if(remove != null) {
            peakLoadRingBufferTable.insertAndRemoveNewTransaction(add, remove.getId());
        } else {
            peakLoadRingBufferTable.insertNewTransaction(add);
        }
    }

    private void addResultToDatabase(@NotNull final PeakLoadRingBufferElement valuesSet) throws BtpException {
        peakLoadRingBufferTable.setPeakLoadCapabilityNewTransaction(valuesSet.getId(),
                valuesSet.getResult().getPeakLoadCapability());
    }
}