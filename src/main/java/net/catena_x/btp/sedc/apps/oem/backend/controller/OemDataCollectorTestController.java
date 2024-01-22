package net.catena_x.btp.sedc.apps.oem.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.edc.api.EdcApi;
import net.catena_x.btp.libraries.edc.model.CatalogResult;
import net.catena_x.btp.libraries.edc.model.Edr;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.libraries.util.threads.Threads;
import net.catena_x.btp.sedc.apps.oem.backend.receiver.ResultReceiver;
import net.catena_x.btp.sedc.apps.oem.backend.receiver.ResultReceiverChannel;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.elements.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class OemDataCollectorTestController {
    @Autowired private ApiHelper apiHelper;
    @Autowired private EdcApi edcApi;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;

    @Value("${peakload.partner.url}") private String peakloadPartnerUrl;
    @Value("${peakload.partner.assetid}") private String peakloadPartnerAssetId;
    @Value("${peakload.partner.bpn}") private String peakloadPartnerBpn;
    @Value("${edc.dataplane.replacement.url:#{null}}") private String edcDataplaneReplacementUrl;
    @Value("${edc.dataplane.replacement.user:#{null}}") private String edcDataplaneReplacementUser;
    @Value("${edc.dataplane.replacement.pass:#{null}}") private String edcDataplaneReplacementPass;
    @Value("${edc.negotiation.delayinseconds:0}") private long edcNegotiationDelayInSeconds;

    private final Logger logger = LoggerFactory.getLogger(OemDataCollectorTestController.class);

    @GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> catalog() {
        try {
            final CatalogResult result = edcApi.requestCatalog(peakloadPartnerUrl);
            return apiHelper.ok("ok: " + objectMapper.writeValueAsString(result));
        } catch (final BtpException | JsonProcessingException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    /* Starts the streaming in a thread and returns immediately with HTTP OK. */
    @GetMapping(value = "/startasync", produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized ResponseEntity<DefaultApiResult> startAsync() {
        logger.info("<>/startasync: Version 1.0.1");

        try {
            new Thread(() -> {
                try {
                    final ResultReceiverChannel receiver = openStream();
                    ResultReceiver.startReceivingResultsAsync(receiver.getRawReceiver());
                } catch (final BtpException exception) {
                    logger.error("Starting calcualtions failed: " + exception.getMessage());
                } catch (final Exception exception) {
                    logger.error("Starting calcualtions failed: " + exception.getMessage());
                }
            }).start();

            return apiHelper.ok("Started processing asynchronously...");
        } catch (final Exception exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    /* Opens the stream. */
    private ResultReceiverChannel openStream() throws BtpException {
        final ConfigBlock configBlock = getConfiguration();
        final ResultReceiverChannel receiver = new ResultReceiverChannel();

        final Edr edr = requestStreamingAsset();
        receiver.open(edr, configBlock, null);
        logger.info("Result stream opened, start receiving results.");

        return receiver;
    }

    private Edr requestStreamingAsset() throws BtpException {
        logger.info("Try to open result stream.");

        if(edcDataplaneReplacementUrl != null) {
            /* Direct streaming without EDCs. */
            final Edr edr = getReplacementEdr(edcDataplaneReplacementUrl, edcDataplaneReplacementUser,
                                              edcDataplaneReplacementPass);

            /* Simulate negotiation time (delay). */
            if (edcNegotiationDelayInSeconds != 0L) {
                Threads.sleepWithoutExceptions(edcNegotiationDelayInSeconds * 1000L);
            }

            return edr;
        }

        /* Streaming with EDC dataplane as a proxy. */
        return edcApi.getEdrForAsset(
                        peakloadPartnerUrl, peakloadPartnerBpn, peakloadPartnerAssetId,
                        CatalogProtocol.HTTP, MediaType.APPLICATION_OCTET_STREAM, false);
    }

    private ConfigBlock getConfiguration() {
        final ConfigBlock configBlock = new ConfigBlock();
        configBlock.setStream(new Stream());
        configBlock.getStream().setVersion("V1");
        configBlock.getStream().setStreamId(UUID.randomUUID().toString());
        configBlock.getStream().setStreamType("PeakLoadRequestStream");
        configBlock.getStream().setTimestamp(Instant.now());

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