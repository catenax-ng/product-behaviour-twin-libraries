package net.catena_x.btp.sedc.apps.oem.backend.receiver;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.apps.oem.backend.buffer.RingBufferInterface;
import net.catena_x.btp.sedc.mapper.PeakLoadContentMapper;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.protocol.model.ContentMapperInterface;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.transmit.RawBlockReceiver;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

public class ResultReceiver {
    private final static Logger logger = LoggerFactory.getLogger(ResultReceiver.class);
    private final static ContentMapperInterface contentMapper = new PeakLoadContentMapper();

    public static void startReceivingResultsAsync(@NotNull final RawBlockReceiver rawReceiver,
                                                  @NotNull final String streamId,
                                                  @Nullable final RingBufferInterface ringBuffer) throws BtpException {
        try {
            new Thread(() -> {
                try {
                while (true) {
                    logger.info("Wait for next result...");
                    final RawBlockReceiver.Result result = rawReceiver.receiveNext();
                    if(!result.successful) {
                        rawReceiver.close();
                        return;
                    }

                    final DataBlock<PeakLoadResult> rawData = (DataBlock<PeakLoadResult>)contentMapper.deserialize(
                            result.content.getContent(), result.header.getContentType());

                    if(ringBuffer != null) {
                        ringBuffer.addResult(result.header.getId(), rawData.getData());
                    }

                    logger.info("Result received for id \"" + result.header.getId() + "\", value: "
                            + rawData.getData().getPeakLoadCapability() + "." );
                    }
                } catch (final BtpException exception) {
                    logger.error(exception.getMessage());
                }
            }).start();
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }
    }
}
