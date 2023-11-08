package net.catena_x.btp.sedc.apps.supplier.calculation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletResponse;
import net.catena_x.btp.libraries.edc.api.EdcApi;
import net.catena_x.btp.libraries.edc.model.Edr;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.apps.supplier.calculation.receiver.TaskBaseReceiverChannel;
import net.catena_x.btp.sedc.apps.supplier.calculation.sender.ResultSender;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
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

@RestController
@RequestMapping("/")
public class SupplierCalculationTestController {
    @Autowired private ApiHelper apiHelper;
    @Autowired private EdcApi edcApi;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;

    @Value("${app.baseurl}") private String appBaseUrl;
    @Value("${peakload.partner.bpn}") private String peakloadPartnerBpn;
    @Value("${peakload.asset.id}") private String peakloadAssetId;
    @Value("${peakload.policy.id}") private String peakloadPolicyId;
    @Value("${peakload.contract.id}") private String peakloadContractId;
    @Value("${peakload.asset.target}") private String peakloadAssetTarget;
    @Value("${edc.dataplane.replacement.url:#{null}}") private String edcDataplaneReplacementUrl;

    private final Logger logger = LoggerFactory.getLogger(SupplierCalculationTestController.class);

    @PostMapping(value = "peakload/calculate", produces = MediaType.ALL_VALUE)
    public ResponseEntity<StreamingResponseBody> calculate(@RequestBody @NotNull final ConfigBlock configBlock,
                                                           @NotNull final HttpServletResponse response) {
        logger.info("Request for result stream.");
        try {
            response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");

            final TaskBaseReceiverChannel receiver = new TaskBaseReceiverChannel();
            logger.info("Try to open rawdata stream.");

            final Edr edr = (edcDataplaneReplacementUrl != null)? getReplacementEdr(edcDataplaneReplacementUrl) :
                    edcApi.getEdrForAsset(
                        configBlock.getBackchannel().getAddress(), configBlock.getBackchannel().getBpn(),
                        configBlock.getBackchannel().getAssetId(), CatalogProtocol.HTTP,
                        MediaType.APPLICATION_OCTET_STREAM, false);

            logger.info("Received EDR for rawdata stream: " + edr.getEndpoint());

            receiver.open(edr, getConfiguration(configBlock.getStream().getStreamId()), null);
            logger.info("Rawdata stream opened.");
            final ResultSender sender = new ResultSender();
            return new ResponseEntity(sender.getStreamingResponseBody(receiver.getRawReceiver()), HttpStatus.OK);
        } catch (final BtpException exception) {
            return new ResponseEntity(null, HttpStatus.FAILED_DEPENDENCY);
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

    private HttpUrl getPeakloadAssetTargetAddress() {
        return HttpUrl.parse(appBaseUrl).newBuilder().addPathSegments(peakloadAssetTarget).build();
    }

    private Edr getReplacementEdr(@NotNull final String replacementUrl) {
        final Edr edr = new Edr();
        edr.setId("replacement");
        edr.setEndpoint(replacementUrl);
        return edr;
    }
}