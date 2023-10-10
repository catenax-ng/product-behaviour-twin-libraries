package net.catena_x.btp.sedc.apps.supplier.calculation.controller;

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
import net.catena_x.btp.sedc.apps.supplier.calculation.receiver.TaskBaseReceiverChannel;
import net.catena_x.btp.sedc.apps.supplier.calculation.sender.ResultSender;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
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

@RestController
@RequestMapping("/")
public class SupplierCalculationTestController {
    @Autowired private ApiHelper apiHelper;
    @Autowired private EdcApi edcApi;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(SupplierCalculationTestController.class);

    @PostMapping(value = "/calculate", produces = MediaType.ALL_VALUE)
    public ResponseEntity<StreamingResponseBody> calculate(@RequestBody @NotNull final ConfigBlock configBlock,
                                                           @NotNull final HttpServletResponse response) {

        logger.info("Request for result stream.");
        try {
            final TaskBaseReceiverChannel receiver = new TaskBaseReceiverChannel();
            logger.info("Try to open rawdata stream.");
            receiver.open(configBlock.getBackchannel().getAddress(),
                    configBlock.getBackchannel().getAssetId(),
                    getConfiguration(configBlock.getStream().getStreamId()), getHeaders());
            logger.info("Rawdata stream opened.");
            final ResultSender sender = new ResultSender();
            return new ResponseEntity(sender.getStreamingResponseBody(receiver.getRawReceiver()), HttpStatus.OK);
        } catch (final BtpException exception) {
            return new ResponseEntity(null, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> catalog() {
        final CatalogRequest catalogRequest = new CatalogRequest();
        catalogRequest.setQuerySpec(new QuerySpec());
        catalogRequest.setCounterPartyAddress("https://bmw-btp-test.dev.demo.catena-x.net/api/v1/dsp");

        try {
            final CatalogResult result = edcApi.requestCatalog(catalogRequest).getBody();
            return apiHelper.ok("ok: " + objectMapper.writeValueAsString(result));
        } catch (final BtpException | JsonProcessingException exception) {
            return apiHelper.failed(exception.getMessage());
        }
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

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64.getEncoder().withoutPadding()
                .encodeToString(("user:pass").getBytes(StandardCharsets.UTF_8)));
        return headers;
    }
}
