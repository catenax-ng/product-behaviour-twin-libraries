package net.catena_x.btp.sedc.apps.oem.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import net.catena_x.btp.libraries.edc.api.EdcApi;
import net.catena_x.btp.libraries.edc.model.CatalogRequest;
import net.catena_x.btp.libraries.edc.model.CatalogResult;
import net.catena_x.btp.libraries.edc.model.catalog.QuerySpec;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.apps.oem.backend.calculation.CalculationConnection;
import net.catena_x.btp.sedc.apps.oem.backend.receiver.ResultReceiver;
import net.catena_x.btp.sedc.apps.oem.backend.receiver.ResultReceiverChannel;
import net.catena_x.btp.sedc.apps.oem.backend.sender.RawdataSender;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.elements.Backchannel;
import net.catena_x.btp.sedc.protocol.model.blocks.elements.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/")
public class OemDataCollectorTestController {
    @Autowired private ApiHelper apiHelper;
    @Autowired private EdcApi edcApi;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(OemDataCollectorTestController.class);
    private final Map<String, CalculationConnection> calculateinConnections = new ConcurrentHashMap<>();

    @GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> catalog() {
        final CatalogRequest catalogRequest = new CatalogRequest();
        catalogRequest.setQuerySpec(new QuerySpec());
        catalogRequest.setCounterPartyAddress("https://supplier-btp-test.dev.demo.catena-x.net/api/v1/dsp");

        try {
            final CatalogResult result = edcApi.requestCatalog(catalogRequest).getBody();
            return apiHelper.ok("ok: " + objectMapper.writeValueAsString(result));
        } catch (final BtpException | JsonProcessingException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @GetMapping(value = "/asset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> assetRegistration() {
        try {
            edcApi.registerAsset("testAsset2", "https://jsonplaceholder.typicode.com/todos/1");
            return apiHelper.ok("ok");
        } catch (final BtpException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @GetMapping(value = "/policy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> policyRegistration() {
        try {
            edcApi.registerPolicy("testPolicy2", "BPNL00000008GX34");
            return apiHelper.ok("ok");
        } catch (final BtpException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @GetMapping(value = "/contract", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> contractRegistration() {
        try {
            edcApi.registerContract("testContract2", "testAsset2", "testPolicy2");
            return apiHelper.ok("ok");
        } catch (final BtpException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @GetMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized ResponseEntity<DefaultApiResult> start() {
        try {
            final ConfigBlock configBlock = getConfiguration();
            final CalculationConnection connection = new CalculationConnection();
            connection.setStreamId(configBlock.getStream().getStreamId());
            connection.setReceiver(new ResultReceiverChannel());
            calculateinConnections.put(configBlock.getStream().getStreamId(), connection);

            logger.info("Try to open result stream.");
            connection.getReceiver().open(
                    /*"https://supplier-btp-test.dev.demo.catena-x.net",*/
                    "http://localhost:25562/calculate",
                    "peakloadcalculationasset",
                    configBlock, getHeaders());
            logger.info("Result stream opened, start receiving results...");
            ResultReceiver.startReceivingResultsAsync(connection.getReceiver().getRawReceiver(),
                    connection.getStreamId());

            return apiHelper.ok("ok");
        } catch (final BtpException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    @PostMapping(value = "/input", produces = MediaType.ALL_VALUE)
    public ResponseEntity<StreamingResponseBody> input(@RequestBody @NotNull final ConfigBlock configBlock,
                                                       @NotNull final HttpServletResponse response) {
        logger.info("Request for rawdata stream.");
        final CalculationConnection connection = calculateinConnections.get(configBlock.getStream().getStreamId());
        if(connection == null) {
            logger.error("Requested unknown stream id \"" + configBlock.getStream().getStreamId() + "\"!");
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        connection.setSender(new RawdataSender());
        return new ResponseEntity(connection.getSender().getStreamingResponseBody(), HttpStatus.OK);
    }

    private ConfigBlock getConfiguration() {
        final ConfigBlock configBlock = new ConfigBlock();
        configBlock.setStream(new Stream());
        configBlock.getStream().setVersion("V1");
        configBlock.getStream().setStreamId(UUID.randomUUID().toString());
        configBlock.getStream().setStreamType("PeakLoadRequestStream");
        configBlock.getStream().setTimestamp(Instant.now());

        configBlock.setBackchannel(new Backchannel());
        configBlock.getBackchannel().setBpn("BPN000X");
        configBlock.getBackchannel().setAddress("http://localhost:25560/input");
        configBlock.getBackchannel().setAssetId("AssetID:urn:");

        return configBlock;
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64.getEncoder().withoutPadding()
                .encodeToString(("user:pass").getBytes(StandardCharsets.UTF_8)));
        return headers;
    }
}
