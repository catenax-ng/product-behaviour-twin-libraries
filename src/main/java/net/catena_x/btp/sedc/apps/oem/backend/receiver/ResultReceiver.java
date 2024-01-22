package net.catena_x.btp.sedc.apps.oem.backend.receiver;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
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

    /* Starts a thread for receiving data in an endless while and returns immediately. */
    public static void startReceivingResultsAsync(@NotNull final RawBlockReceiver rawReceiver) throws BtpException {
        try {
            new Thread(() -> {
                try {
                    logger.info("Wait for first result ...");
                    while (true) {
                        receiveNext(rawReceiver);
                    }
                } catch (final BtpException exception) {
                    logger.error(exception.getMessage());
                }
            }).start();
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }
    }

    /* Receives one (the next) data block, checks and logs it. */
    private static void receiveNext(@NotNull final RawBlockReceiver rawReceiver) throws BtpException {
        final RawBlockReceiver.Result result = rawReceiver.receiveNext();
        if(!checkResult(result, rawReceiver)) { return; }

        final DataBlock<PeakLoadResult> rawData =
                (DataBlock<PeakLoadResult>)contentMapper.deserialize(
                        result.content.getContent(), result.header.getContentType());

        logResult(result, rawData.getData());
    }

    /* Checks a received data block. */
    private static boolean checkResult(final @Nullable RawBlockReceiver.Result result,
                                       @NotNull final RawBlockReceiver rawReceiver) throws BtpException {
        if(result.end != null) {
            rawReceiver.close();
            return false;
        }

        logger.info("Received data.");

        if(result.content == null) {
            logger.error("Receiving result not successful!");

            rawReceiver.close();
            return false;
        }

        return true;
    }

    /* Logs a received data block. */
    private static void logResult(final @NotNull RawBlockReceiver.Result result,
                                  final @NotNull PeakLoadResult peakLoadResult) {
        logger.info("Result received for id \"" + result.header.getId() + "\", value: "
                + peakLoadResult.getPeakLoadCapability() + ".");
    }
}