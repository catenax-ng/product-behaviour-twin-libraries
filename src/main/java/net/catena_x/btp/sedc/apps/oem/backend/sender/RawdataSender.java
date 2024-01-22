package net.catena_x.btp.sedc.apps.oem.backend.sender;

import lombok.Getter;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.threads.Threads;
import net.catena_x.btp.sedc.apps.oem.backend.generator.TestDataGenerator;
import net.catena_x.btp.sedc.mapper.PeakLoadContentMapper;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
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

public class RawdataSender implements SenderInterface<PeakLoadRawValues> {
    private final OutputStream outputStream = new OutputStream();
    private final ContentMapperInterface contentMapper = new PeakLoadContentMapper();
    private final Logger logger = LoggerFactory.getLogger(RawdataSender.class);

    @Getter private String streamId = null;

    @Override public void send(@NotNull final PeakLoadRawValues value, @NotNull final String id) throws BtpException {
        final HeaderBlock headerBlock = new HeaderBlock();
        headerBlock.setId(id);
        headerBlock.setContentType("PeakLoadRawValues");
        headerBlock.setTimestamp(Instant.now());
        headerBlock.setContentVersion("V1");

        outputStream.write(headerBlock, new DataBlock<>(value), contentMapper);
    }

    @Override public void keepAlive() throws BtpException {
        outputStream.keepAlive();
    }

    public StreamingResponseBody getStreamingResponseBody() {
        final StreamingResponseBody stream = outputStream -> {
            try {
                logger.info("Start sending rawdata over stream ...");

                this.outputStream.init(outputStream);

                final TestDataGenerator generator = new TestDataGenerator();

                keepAlive();
                logger.info("Sent keep alive.");

                long id = 0;
                while(true) {
                    id += 1;
                    final PeakLoadRawValues rawValues = generator.getNext();
                    if(rawValues == null) {
                        break;
                    }

                    if(id == 1) {
                        logger.info("Sending first rawdata block.");
                    } else if(id <= 5 || id % 100 == 0) {
                        logger.info("Sending rawdata block nr {}.", id);
                    }

                    send(rawValues, String.valueOf(id));

                    if(id == 1) {
                        logger.info("First rawdata block sent.");
                    } else if(id <= 5 || id % 100 == 0) {
                        logger.info("Rawdata block nr {} sent.", id);
                    }

                    Threads.sleepWithoutExceptions(700L);
                }
            } catch (final Exception exception) {
                logger.error("Error while sending raw values: " + exception.getMessage());
            }
        };

        return stream;
    }
}