package net.catena_x.btp.sedc.apps.supplier.calculation.sender;

import kotlin.text.Charsets;
import net.catena_x.btp.libraries.edc.api.EdcApi;
import net.catena_x.btp.libraries.edc.model.Edr;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.threads.Threads;
import net.catena_x.btp.sedc.apps.supplier.calculation.receiver.TaskBaseReceiverChannel;
import net.catena_x.btp.sedc.apps.supplier.calculation.service.PeakLoadCalculation;
import net.catena_x.btp.sedc.apps.supplier.calculation.service.PeakLoadCalculationInterface;
import net.catena_x.btp.sedc.mapper.PeakLoadContentMapper;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.protocol.model.ContentMapperInterface;
import net.catena_x.btp.sedc.protocol.model.OutputStream;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.elements.Stream;
import net.catena_x.btp.sedc.transmit.RawBlockReceiver;
import net.catena_x.btp.sedc.transmit.SenderInterface;
import org.bouncycastle.util.encoders.Base64;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotNull;
import java.nio.charset.Charset;
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

    @Override public void keepAlive() throws BtpException {
        outputStream.keepAlive();
    }

    public StreamingResponseBody getStreamingResponseBody(@NotNull final ConfigBlock configBlock,
                                                          @NotNull final EdcApi edcApi,
                                                          @Nullable final String edcDataplaneReplacementUrl,
                                                          @Nullable final String dcDataplaneReplacementUser,
                                                          @Nullable final String edcDataplaneReplacementPass,
                                                          final long edcNegotiationDelayInSeconds)
            throws BtpException {

        final StreamingResponseBody stream = outputStream -> {
            try {
                this.outputStream.init(outputStream);

                keepAlive();
                logger.info("Sent keep alive.");

                final TaskBaseReceiverChannel receiver = new TaskBaseReceiverChannel();

                //receiver.getRawReceiver()

                logger.info("Try to open rawdata stream.");

                final Edr edr = (edcDataplaneReplacementUrl != null)? getReplacementEdr(
                        edcDataplaneReplacementUrl, dcDataplaneReplacementUser, edcDataplaneReplacementPass) :
                        edcApi.getEdrForAsset(
                                configBlock.getBackchannel().getAddress(), configBlock.getBackchannel().getBpn(),
                                configBlock.getBackchannel().getAssetId(), CatalogProtocol.HTTP,
                                MediaType.APPLICATION_OCTET_STREAM, false);

                if(edcNegotiationDelayInSeconds != 0L) {
                    Threads.sleepWithoutExceptions(edcNegotiationDelayInSeconds * 1000L);
                }

                logger.info("Received EDR for rawdata stream: " + edr.getEndpoint());

                receiver.open(edr, getConfiguration(configBlock.getStream().getStreamId()), null);
                logger.info("Rawdata stream opened.");

                final RawBlockReceiver rawReceiver = receiver.getRawReceiver();
                final PeakLoadCalculationInterface calculation = new PeakLoadCalculation();

                boolean first = true;

                while (true) {
                    if(first) {
                        logger.info("Wait for first task ...");
                    }

                    final RawBlockReceiver.Result result = rawReceiver.receiveNext();
                    if(result.end != null) {
                        rawReceiver.close();
                        return;
                    }

                    if(result.content == null) {
                        logger.error("Receiving " + (first? "first " : "") + "task not successful!");

                        rawReceiver.close();
                        return;
                    }

                    if(first) {
                        logger.info("Received data for first task.");
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

    private ConfigBlock getConfiguration(@NotNull final String streamId) {
        final ConfigBlock configBlock = new ConfigBlock();
        configBlock.setStream(new Stream());
        configBlock.getStream().setVersion("V1");
        configBlock.getStream().setStreamId(streamId);
        configBlock.getStream().setStreamType("PeakLoadResultStream");
        configBlock.getStream().setTimestamp(Instant.now());

        configBlock.setBackchannel(null);

        return configBlock;
    }

    private String getAuthString(@NotNull final String userASCII, @NotNull final String passASCII) {
        StringBuilder sb = new StringBuilder();

        String authStr = sb.append(userASCII).append(":").append(passASCII).toString();
        sb.setLength(0);
        sb.append("Basic ").append(java.util.Base64.getEncoder().encodeToString(authStr.getBytes()));
        return sb.toString();
    }

    private Edr getReplacementEdr(@NotNull final String replacementUrl,
                                  @NotNull final String userASCII, @NotNull final String passASCII) {
        final Edr edr = new Edr();
        edr.setId("replacement");
        edr.setEndpoint(replacementUrl);

        if((userASCII != null) && (passASCII != null)) {
            edr.setAuthKey("Authorization");
            edr.setAuthCode(getAuthString(userASCII, passASCII));
        }

        return edr;
    }
}