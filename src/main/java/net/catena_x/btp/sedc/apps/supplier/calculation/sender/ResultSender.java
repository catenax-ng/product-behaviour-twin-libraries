package net.catena_x.btp.sedc.apps.supplier.calculation.sender;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.apps.supplier.calculation.service.PeakLoadCalculation;
import net.catena_x.btp.sedc.apps.supplier.calculation.service.PeakLoadCalculationInterface;
import net.catena_x.btp.sedc.mapper.PeakLoadContentMapper;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.protocol.model.ContentMapperInterface;
import net.catena_x.btp.sedc.protocol.model.OutputStream;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;
import net.catena_x.btp.sedc.transmit.RawBlockReceiver;
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

        //logger.info("Send result.");
        outputStream.write(headerBlock, new DataBlock<>(value), contentMapper);
        //logger.info("Result sent.");
    }

    public StreamingResponseBody getStreamingResponseBody(@NotNull final RawBlockReceiver rawReceiver) {
        final StreamingResponseBody stream = outputStream -> {
            try {
                final PeakLoadCalculationInterface calculation = new PeakLoadCalculation();
                this.outputStream.init(outputStream);

                boolean first = true;

                while (true) {
                    if(first) {
                        logger.info("Wait for first task ...");
                    }

                    final RawBlockReceiver.Result result = rawReceiver.receiveNext();

                    if(first) {
                        logger.info("Received data for first task.");
                    }

                    if(!result.successful) {
                        if(first) {
                            logger.error("Receiving first task not successful!");
                        }

                        rawReceiver.close();
                        return;
                    }

                    first = false;

                    final DataBlock<PeakLoadRawValues> rawData = contentMapper.deserialize(
                            result.content.getContent(), result.header.getContentType());

                    threadPool.submit(()->{
                        try {
                            if(result.header.getId().equals("1")) {
                                logger.info("Calculating first task with id " + result.header.getId() + ".");
                            }
                            final PeakLoadResult calculationResult = calculation.calculate(rawData.getData());

                            if(result.header.getId().equals("1")) {
                                logger.info("Sending first result with id " + result.header.getId() + ".");
                            }
                            send(calculationResult, result.header.getId());
                            if(result.header.getId().equals("1")) {
                                logger.info("First result with id " + result.header.getId() + " sent.");
                            }
                        } catch (final BtpException exception) {
                            logger.error(exception.getMessage());
                        }
                    });
                }
            } catch (final BtpException exception) {
                logger.error(exception.getMessage());
            }
        };

        return stream;
    }
}