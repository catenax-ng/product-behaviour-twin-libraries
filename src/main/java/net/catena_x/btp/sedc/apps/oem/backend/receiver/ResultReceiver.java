package net.catena_x.btp.sedc.apps.oem.backend.receiver;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.mapper.PeakLoadContentMapper;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.protocol.model.ContentMapperInterface;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.transmit.RawBlockReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

public class ResultReceiver {
    private final static Logger logger = LoggerFactory.getLogger(ResultReceiver.class);
    private final static ContentMapperInterface contentMapper = new PeakLoadContentMapper();

    public static void startReceivingResultsAsync(@NotNull final RawBlockReceiver rawReceiver,
                                                  @NotNull final String streamId) throws BtpException {
        try {
            new Thread(() -> {
                try {
                    long count = 0L;
                    while (true) {
                        if(count == 0) {
                            logger.info("Wait for first result ...");
                        }

                        final RawBlockReceiver.Result result = rawReceiver.receiveNext();
                        if(result.end != null) {
                            rawReceiver.close();
                            return;
                        }

                        if(count == 0) {
                            logger.info("Received data for first result.");
                        }

                        if(result.content == null) {
                            logger.error("Receiving " + ((count == 0)? "first " : "") + "result not successful!");

                            rawReceiver.close();
                            return;
                        }

                        final DataBlock<PeakLoadResult> rawData = (DataBlock<PeakLoadResult>)contentMapper.deserialize(
                                result.content.getContent(), result.header.getContentType());

                        if(((count < 500000L) && (((count % 100L) == 0L) || (count < 10L))) || (count % 4000L == 0L)) {
                            logger.info("Result received for id \"" + result.header.getId() + "\", value: "
                                    + rawData.getData().getPeakLoadCapability() + ".");
                        }
                        ++count;
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