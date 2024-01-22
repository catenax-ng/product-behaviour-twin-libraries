package net.catena_x.btp.sedc.apps.supplier.calculation.sender;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.threads.Threads;
import net.catena_x.btp.sedc.mapper.PeakLoadContentMapper;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.protocol.model.ContentMapperInterface;
import net.catena_x.btp.sedc.protocol.model.OutputStream;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;
import net.catena_x.btp.sedc.transmit.SenderInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ResultSender implements SenderInterface<PeakLoadResult> {
    private final OutputStream outputStream = new OutputStream();
    private final ContentMapperInterface contentMapper = new PeakLoadContentMapper();
    private final Logger logger = LoggerFactory.getLogger(ResultSender.class);
    private final ThreadPoolExecutor threadPool =  (ThreadPoolExecutor)Executors.newCachedThreadPool();

    @Override
    public synchronized void send(@NotNull final PeakLoadResult value, @NotNull final String id) throws BtpException {
        final HeaderBlock headerBlock = new HeaderBlock();
        headerBlock.setId(id);
        headerBlock.setContentType("PeakLoadResult");
        headerBlock.setTimestamp(Instant.now());
        headerBlock.setContentVersion("V1");

        logger.info("Send result with id " + id + ".");
        outputStream.write(headerBlock, new DataBlock<>(value), contentMapper);
        logger.info("Result with id " + id + " sent.");
    }

    @Override public void keepAlive() throws BtpException {
        outputStream.keepAlive();
    }

    public StreamingResponseBody getStreamingResponseBody() throws BtpException {

        final StreamingResponseBody stream = outputStream -> {
            try {
                this.outputStream.init(outputStream);
                keepAlive();
                logger.info("Sent keep alive.");

                final PeakLoadResult result =new PeakLoadResult(1L);
                long currentId = 0L;

                while (true) {
                    Threads.sleepWithoutExceptions(700L);

                    ++currentId;

                    final String id = String.valueOf(currentId);

                    threadPool.submit(()->{
                        try {
                            Threads.sleepWithoutExceptions(1234L);
                            send(result, id);
                        } catch (final Exception exception) {
                            logger.error(exception.getMessage());
                        }
                    });
                }
            } catch (final Exception exception) {
                logger.error(exception.getMessage());
            }
        };

        return stream;
    }

    private String getAuthString(@NotNull final String userASCII, @NotNull final String passASCII) {
        StringBuilder sb = new StringBuilder();

        String authStr = sb.append(userASCII).append(":").append(passASCII).toString();
        sb.setLength(0);
        sb.append("Basic ").append(java.util.Base64.getEncoder().encodeToString(authStr.getBytes()));
        return sb.toString();
    }
}