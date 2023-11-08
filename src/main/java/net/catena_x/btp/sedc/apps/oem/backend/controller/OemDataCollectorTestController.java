package net.catena_x.btp.sedc.apps.oem.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletResponse;
import net.catena_x.btp.libraries.edc.api.EdcApi;
import net.catena_x.btp.libraries.edc.model.CatalogResult;
import net.catena_x.btp.libraries.edc.model.Edr;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.apps.oem.backend.buffer.RingBufferImpl;
import net.catena_x.btp.sedc.apps.oem.backend.calculation.CalculationConnection;
import net.catena_x.btp.sedc.apps.oem.backend.receiver.ResultReceiver;
import net.catena_x.btp.sedc.apps.oem.backend.receiver.ResultReceiverChannel;
import net.catena_x.btp.sedc.apps.oem.backend.sender.RawdataSender;
import net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer.PeakLoadRingBufferTable;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.elements.Backchannel;
import net.catena_x.btp.sedc.protocol.model.blocks.elements.Stream;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/")
public class OemDataCollectorTestController {
    @Autowired private ApiHelper apiHelper;
    @Autowired private EdcApi edcApi;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;
    @Autowired private RingBufferImpl ringBuffer;
    @Autowired private PeakLoadRingBufferTable ringBufferTable;

    @Value("${app.baseurl}") private String appBaseUrl;
    @Value("${peakload.partner.url}") private String peakloadPartnerUrl;
    @Value("${peakload.partner.assetid}") private String peakloadPartnerAssetId;
    @Value("${peakload.partner.bpn}") private String peakloadPartnerBpn;
    @Value("${edc.public.bpn}") private String edcPublicBpn;
    @Value("${edc.public.url}") private String edcPublicUrl;
    @Value("${peakload.asset.id}") private String peakloadAssetId;
    @Value("${peakload.policy.id}") private String peakloadPolicyId;
    @Value("${peakload.contract.id}") private String peakloadContractId;
    @Value("${peakload.asset.target}") private String peakloadAssetTarget;
    @Value("${edc.dataplane.replacement.url:#{null}}") private String edcDataplaneReplacementUrl;

    private boolean started = false;
    private final Logger logger = LoggerFactory.getLogger(OemDataCollectorTestController.class);
    private final Map<String, CalculationConnection> calculateinConnections = new ConcurrentHashMap<>();

    @GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> catalog() {
        try {
            final CatalogResult result = edcApi.requestCatalog(peakloadPartnerUrl);
            return apiHelper.ok("ok: " + objectMapper.writeValueAsString(result));
        } catch (final BtpException | JsonProcessingException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @GetMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> assetRegistration() {
        try {
            edcApi.registerAsset(peakloadAssetId, getPeakloadAssetTargetAddress(), true, false);
            edcApi.registerPolicy(peakloadPolicyId, peakloadPartnerBpn);
            edcApi.registerContract(peakloadContractId, peakloadAssetId, peakloadPolicyId);
            return apiHelper.ok("Asset, policy and contract registration successful.");
        } catch (final BtpException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @GetMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> assetDeletion() {
        try {
            edcApi.deleteContract(peakloadContractId);
            edcApi.deletePolicy(peakloadPolicyId);
            edcApi.deleteAsset(peakloadAssetId);
            return apiHelper.ok("Asset, policy and contract deletion successful.");
        } catch (final BtpException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized ResponseEntity<DefaultApiResult> test() {
        try {
            final Edr edr = (edcDataplaneReplacementUrl != null)? getReplacementEdr(edcDataplaneReplacementUrl) :
                    edcApi.getEdrForAsset(
                        peakloadPartnerUrl, peakloadPartnerBpn, peakloadPartnerAssetId, CatalogProtocol.HTTP,
                        MediaType.APPLICATION_OCTET_STREAM, true);
            return apiHelper.ok("Test successful, got endpoint: \"" + edr.getEndpoint() + "\".");
        } catch (final Exception exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @GetMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized ResponseEntity<DefaultApiResult> start() {
        if(started) {
            return apiHelper.failed("Processing already started!");
        }

        try {
            started = true;

            ringBufferTable.deleteAllNewTransaction();
            ringBuffer.init(30L);

            final ConfigBlock configBlock = getConfiguration();
            final CalculationConnection connection = new CalculationConnection();
            connection.setStreamId(configBlock.getStream().getStreamId());
            connection.setRingBuffer(ringBuffer);
            connection.setReceiver(new ResultReceiverChannel());
            calculateinConnections.put(configBlock.getStream().getStreamId(), connection);

            logger.info("Try to open result stream.");

            final Edr edr = (edcDataplaneReplacementUrl != null)? getReplacementEdr(edcDataplaneReplacementUrl) :
                    edcApi.getEdrForAsset(
                        peakloadPartnerUrl, peakloadPartnerBpn, peakloadPartnerAssetId, CatalogProtocol.HTTP,
                        MediaType.APPLICATION_OCTET_STREAM, false);

            connection.getReceiver().open(edr, configBlock, null);
            logger.info("Result stream opened, start receiving results.");
            ResultReceiver.startReceivingResultsAsync(connection.getReceiver().getRawReceiver(),
                    connection.getStreamId(), ringBuffer);

            return apiHelper.ok("Started processing...");
        } catch (final BtpException exception) {
            return apiHelper.failed(exception.getMessage());
        } catch (final Exception exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @PostMapping(value = "peakload/input", produces = MediaType.ALL_VALUE)
    public ResponseEntity<StreamingResponseBody> input(@RequestBody @NotNull final ConfigBlock configBlock,
                                                       @NotNull final HttpServletResponse response) {
        logger.info("Request for rawdata stream.");
        response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
        final CalculationConnection connection = calculateinConnections.get(configBlock.getStream().getStreamId());
        if(connection == null) {
            logger.error("Requested unknown stream id \"" + configBlock.getStream().getStreamId() + "\"!");
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        connection.setSender(new RawdataSender(ringBuffer));
        return new ResponseEntity(connection.getSender().getStreamingResponseBody(), HttpStatus.OK);
    }

    private HttpUrl getPeakloadAssetTargetAddress() {
        return HttpUrl.parse(appBaseUrl).newBuilder().addPathSegments(peakloadAssetTarget).build();
    }

    private ConfigBlock getConfiguration() {
        final ConfigBlock configBlock = new ConfigBlock();
        configBlock.setStream(new Stream());
        configBlock.getStream().setVersion("V1");
        configBlock.getStream().setStreamId(UUID.randomUUID().toString());
        configBlock.getStream().setStreamType("PeakLoadRequestStream");
        configBlock.getStream().setTimestamp(Instant.now());

        configBlock.setBackchannel(new Backchannel());
        configBlock.getBackchannel().setBpn(edcPublicBpn);
        configBlock.getBackchannel().setAddress(edcPublicUrl);
        configBlock.getBackchannel().setAssetId(peakloadAssetId);

        return configBlock;
    }

    private Edr getReplacementEdr(@NotNull final String replacementUrl) {
        final Edr edr = new Edr();
        edr.setId("replacement");
        edr.setEndpoint(replacementUrl);
        return edr;
    }
}