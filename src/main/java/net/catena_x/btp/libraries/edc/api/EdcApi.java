package net.catena_x.btp.libraries.edc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.edc.api.controller.EdcEdrController;
import net.catena_x.btp.libraries.edc.model.*;
import net.catena_x.btp.libraries.edc.model.asset.DataAddress;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.edc.model.catalog.Dataset;
import net.catena_x.btp.libraries.edc.model.catalog.QuerySpec;
import net.catena_x.btp.libraries.edc.model.contract.AssetsSelector;
import net.catena_x.btp.libraries.edc.model.contract.ContractOperandLeft;
import net.catena_x.btp.libraries.edc.model.contract.ContractOperator;
import net.catena_x.btp.libraries.edc.model.general.Policy;
import net.catena_x.btp.libraries.edc.model.general.Type;
import net.catena_x.btp.libraries.edc.model.negotiation.ContractNegotiationResponse;
import net.catena_x.btp.libraries.edc.model.negotiation.IdResponse;
import net.catena_x.btp.libraries.edc.model.negotiation.Offer;
import net.catena_x.btp.libraries.edc.model.policy.*;
import net.catena_x.btp.libraries.edc.model.transfer.DataDestination;
import net.catena_x.btp.libraries.edc.model.transfer.TransferResponse;
import net.catena_x.btp.libraries.edc.model.transfer.TransferType;
import net.catena_x.btp.libraries.edc.util.exceptions.EdcException;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.libraries.util.threads.Threads;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EdcApi {
    private final static String ASSET_DELETION_PATH = "v2/assets";
    private final static String POLICY_DELETION_PATH = "v2/policydefinitions";
    private final static String CONTRACT_DELETION_PATH = "v2/contractdefinitions";
    private final static String CATALOG_REQUEST_PATH = "v2/catalog/request";
    private final static String ASSET_REGISTRATION_PATH = "v3/assets";
    private final static String POLICY_REGISTRATION_PATH = "v2/policydefinitions";
    private final static String CONTRACT_REGISTRATION_PATH = "v2/contractdefinitions";
    private final static String CONTRACT_NEGOTIATION_PATH = "v2/contractnegotiations";
    private final static String CONTRACT_TRANSFER_PATH = "v2/transferprocesses";
    private final static String EDR_PROXY_PATH = "https://oem-edrproxy.dev.demo.catena-x.net/edr";

    private final static String API_KEY_KEY = "X-Api-Key";
    private final static String CONTENT_TYPE = "Content-Type";

    private final static String RECEIVER_HTTP_ENDPOINT = "receiverHttpEndpoint";

    @Autowired private RestTemplate restTemplate;

    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;
    @Autowired EdcEdrController edcEdrController;

    @Value("${edc.api.management.url}") private String managementUrl;
    @Value("${edc.api.management.apikey}") private String apiKey;
    @Value("${app.baseurl}") private String appBaseUrl;

    public CatalogResult requestCatalog(@NotNull final String counterPartyAddress)
            throws BtpException {
        final CatalogRequest catalogRequest = new CatalogRequest();
        catalogRequest.setQuerySpec(new QuerySpec());
        catalogRequest.setCounterPartyAddress(counterPartyAddress);
        return requestCatalog(catalogRequest);
    }

    public String requestCatalogAsString(@NotNull final String counterPartyAddress)
            throws BtpException {
        final CatalogRequest catalogRequest = new CatalogRequest();
        catalogRequest.setQuerySpec(new QuerySpec());
        catalogRequest.setCounterPartyAddress(counterPartyAddress);
        return requestCatalogAsString(catalogRequest);
    }

    public CatalogResult requestCatalog(@NotNull final CatalogRequest catalogRequest)
            throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<CatalogRequest> request = new HttpEntity<>(catalogRequest, headers);
        final HttpUrl requestUrl = getCatalogRequestUrl();

        try {
            return restTemplate.exchange(requestUrl.uri(), HttpMethod.POST, request, CatalogResult.class).getBody();
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }
    }

    public String requestCatalogAsString(@NotNull final CatalogRequest catalogRequest)
            throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<CatalogRequest> request = new HttpEntity<>(catalogRequest, headers);
        final HttpUrl requestUrl = getCatalogRequestUrl();

        try {
            return restTemplate.exchange(requestUrl.uri(), HttpMethod.POST, request, String.class).getBody();
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }
    }

    public List<Dataset> requestAssetsFromCatalog(@NotNull final CatalogRequest catalogRequest,
                                                  @NotNull final String assetId) throws BtpException {
        return assetsFromCatalogResult(requestCatalog(catalogRequest), assetId, true);
    }

    public List<Dataset> requestAssetsFromCatalog(@NotNull final String counterPartyAddress,
                                                  @NotNull final String assetId,
                                                  final boolean throwIfNoneFound) throws BtpException {
        return assetsFromCatalogResult(requestCatalog(counterPartyAddress), assetId, throwIfNoneFound);
    }

    public void deleteAsset(@NotNull final String assetId) throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpUrl requestUrl = getAssetDeletionUrl(assetId);
        final HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity response = null;

        try {
            response = restTemplate.exchange(requestUrl.uri(), HttpMethod.DELETE, request, Object.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }
    }

    public void deletePolicy(@NotNull final String policyId) throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpUrl requestUrl = getPolicyDeletionUrl(policyId);
        final HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity response = null;

        try {
            response = restTemplate.exchange(requestUrl.uri(), HttpMethod.DELETE, request, Object.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }
    }

    public void deleteContract(@NotNull final String contractId) throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpUrl requestUrl = getContractDeletionUrl(contractId);
        final HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity response = null;

        try {
            response = restTemplate.exchange(requestUrl.uri(), HttpMethod.DELETE, request, Object.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }
    }

    public void registerAsset(@NotNull final AssetDefinition assetDefinition) throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<AssetDefinition> request = new HttpEntity<>(assetDefinition, headers);
        final HttpUrl requestUrl = getAssetRegistrationUrl();

        ResponseEntity response = null;

        try {
            response = restTemplate.exchange(requestUrl.uri(), HttpMethod.POST, request, Object.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }
    }

    public void registerAsset(@NotNull final String assetId, @NotNull final HttpUrl dataAddress,
                              final boolean isPostMethode, final boolean hasQueryParameters,
                              final boolean nonChunkedTransfer) throws BtpException {
        final AssetDefinition assetDefinition = new AssetDefinition();
        assetDefinition.setId(assetId);
        assetDefinition.setDataAddress(new DataAddress());
        assetDefinition.getDataAddress().setBaseUrl(dataAddress.toString());
        assetDefinition.getDataAddress().setNonChunkedTransfer(String.valueOf(nonChunkedTransfer));

        if(isPostMethode) {
            assetDefinition.getDataAddress().setProxyMethod("true");
            assetDefinition.getDataAddress().setProxyBody("true");
        }

        if(hasQueryParameters) {
            assetDefinition.getDataAddress().setProxyQueryParams("true");
        }

        registerAsset(assetDefinition);
    }

    public void registerPolicy(@NotNull final PolicyDefinition policyDefinition) throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<PolicyDefinition> request = new HttpEntity<>(policyDefinition, headers);
        final HttpUrl requestUrl = getPolicyRegistrationUrl();

        ResponseEntity response = null;

        try {
            response = restTemplate.exchange(requestUrl.uri(), HttpMethod.POST, request, Object.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }
    }

    public void registerPolicy(@NotNull final String policyId, @NotNull final String partnerBPN) throws BtpException {
        final PolicyDefinition policyDefinition = new PolicyDefinition();
        policyDefinition.setId(policyId);
        policyDefinition.setPolicy(new Policy());
        policyDefinition.getPolicy().setProhibition(new ArrayList<>());
        policyDefinition.getPolicy().setObligation(new ArrayList<>());
        policyDefinition.getPolicy().setPermission(new ArrayList<>());

        final PolicyRestriction permission = new PolicyRestriction();
        permission.setAction(new Action(ActionType.USE));
        permission.setConstraint(new ArrayList<>());

        final Constraint constraint = new Constraint();
        constraint.setType(Type.LOGICAL_CONSTRAINT);

        final OrConstraint or = new OrConstraint();
        or.setLeftOperand(PolicyLeftOperand.BUSINESS_PARTNER_NUMBER);
        or.setOperator(new PolicyOperatorStructured(PolicyOperatorStructuredId.EQUALS));
        or.setRightOperand(partnerBPN);
        constraint.setOr(new ArrayList<>());
        constraint.getOr().add(or);
        permission.getConstraint().add(constraint);

        policyDefinition.getPolicy().getPermission().add(permission);

        registerPolicy(policyDefinition);
    }

    public void registerContract(@NotNull final ContractDefinition contractDefinition) throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<ContractDefinition> request = new HttpEntity<>(contractDefinition, headers);
        final HttpUrl requestUrl = getContractRegistrationUrl();

        ResponseEntity response = null;

        try {
            response = restTemplate.exchange(requestUrl.uri(), HttpMethod.POST, request, Object.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }
    }

    public void registerContract(@NotNull final String contractId, @NotNull final String referencedAssetId,
                                 @NotNull final String referencedPolicyId) throws BtpException {
        final ContractDefinition contractDefinition = new ContractDefinition();

        contractDefinition.setId(contractId);
        contractDefinition.setAccessPolicyId(referencedPolicyId);
        contractDefinition.setContractPolicyId(referencedPolicyId);

        contractDefinition.setAssetsSelector(new AssetsSelector());
        contractDefinition.getAssetsSelector().setOperandLeft(ContractOperandLeft.ASSET_ID);
        contractDefinition.getAssetsSelector().setOperator(ContractOperator.EQUALS);
        contractDefinition.getAssetsSelector().setOperandRight(referencedAssetId);

        registerContract(contractDefinition);
    }

    public String negotiateContract(@NotNull final String providerConnectorDspAddress, @NotNull final String providerBpn,
                                    @NotNull final Dataset asset, @NotNull final Policy policy,
                                    @NotNull final CatalogProtocol protocol) throws BtpException {
        return negotiateContract(generateContractNegotiationRequest(
                providerConnectorDspAddress, providerBpn, asset, policy, protocol));
    }

    public String negotiateContract(@NotNull final ContractNegotiationRequest contractNegotiationRequest)
            throws BtpException {
        final IdResponse negotiationResponse = initNegotiation(contractNegotiationRequest);

        while(true) {
            final ContractNegotiationResponse response = requestNegotiationState(negotiationResponse.getId());
            switch (response.getState()) {
                case INITIAL:
                case REQUESTING:
                case REQUESTED:
                case OFFERING:
                case OFFERED:
                case ACCEPTING:
                case ACCEPTED:
                case AGREEING:
                case AGREED:
                case VERIFYING:
                case VERIFIED:
                case FINALIZING: {
                    Threads.sleepWithoutExceptions(250);
                    break;
                }

                case FINALIZED: {
                    return response.getContractAgreementId();
                }

                case TERMINATING:
                case TERMINATED: {
                    throw new BtpException("Negotiation failed!");
                }
            }
        }
    }

    public String startTransfer(@NotNull final String providerConnectorDspAddress, @NotNull final String connectorId,
                                @NotNull final String contractAgreementId, @NotNull final String assetId,
                                @NotNull final CatalogProtocol protocol,
                                @NotNull final MediaType mediaType, final boolean isFinite,
                                @NotNull final HttpUrl backendAddress) throws BtpException {
        final TransferRequest transferRequest = new TransferRequest();

        final Map<String, String> privateProperties = new HashMap<>();
        privateProperties.put(RECEIVER_HTTP_ENDPOINT, backendAddress.toString());

        transferRequest.setAssetId(assetId);
        transferRequest.setConnectorAddress(providerConnectorDspAddress);
        transferRequest.setConnectorId(connectorId);
        transferRequest.setContractId(contractAgreementId);
        transferRequest.setDataDestination(new DataDestination());
        transferRequest.setManagedResources(false);
        transferRequest.setPrivateProperties(privateProperties);
        transferRequest.setProtocol(protocol);
        transferRequest.setTransferType(new TransferType(mediaType.getType(), isFinite));

        return startTransfer(transferRequest);
    }

    public String startTransfer(@NotNull final TransferRequest transferRequest)
            throws BtpException {
        final IdResponse transferResponse = initTransfer(transferRequest);

        while(true) {
            final TransferResponse response = requestTransferState(transferResponse.getId());
            switch (response.getState()) {
                case INITIAL:
                case PROVISIONING:
                case PROVISIONING_REQUESTED:
                case PROVISIONED:
                case REQUESTING:
                case REQUESTED:
                case STARTING: {
                    Threads.sleepWithoutExceptions(250);
                    break;
                }

                case STARTED: {
                    return response.getContractId();
                }

                case SUSPENDING:
                case SUSPENDED:
                case COMPLETING:
                case COMPLETED:
                case TERMINATING:
                case TERMINATED:
                case DEPROVISIONING:
                case DEPROVISIONING_REQUESTED:
                case DEPROVISIONED: {
                    throw new BtpException("Transfer failed!");
                }
            }
        }
    }

    /* In case of using edr proxy:
    public Edr getEdr(@NotNull final String transferId) throws BtpException {
        final HttpHeaders headers = getNewEdrProxyApiHeaders();
        final HttpEntity<Edr> request = new HttpEntity<>(headers);
        final HttpUrl requestUrl = HttpUrl.parse(getEdrBaseProxyUrl().url().toString()).newBuilder().
                addPathSegment(transferId).build();

        ResponseEntity<Edr> response = null;

        long count = 0;
        while(true) {
            ++count;

            try {
                response = restTemplate.exchange(
                        requestUrl.uri(), HttpMethod.GET, request, Edr.class);
            } catch (final Exception exception) {
                if(count < 100) {
                    Threads.sleepWithoutExceptions(150L);
                    continue;
                }
                throw new BtpException(exception);
            }

            if(!response.getStatusCode().is2xxSuccessful()) {
                if(count < 100) {
                    Threads.sleepWithoutExceptions(150L);
                    continue;
                }
                throw new BtpException("Got status code " + response.getStatusCode().value());
            }

            break;
        }

        return response.getBody();
    }

    private HttpUrl getEdrBaseProxyUrl() {
        return HttpUrl.parse(EDR_PROXY_PATH);
    }

    private HttpHeaders getNewEdrProxyApiHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Basic " + Base64.getEncoder().withoutPadding()
                .encodeToString(("xxxxxxx:xxxxxxx").getBytes(StandardCharsets.UTF_8)));
        headers.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
    */

    public Edr getEdrByContractId(@NotNull final String contractId) throws BtpException {
        long count = 0;
        while(true) {
            ++count;
            try {
                return edcEdrController.getEdrByContractId(contractId);
            } catch (final BtpException exception) {
                if(count < 100) {
                    Threads.sleepWithoutExceptions(150L);
                    continue;
                }
                throw new BtpException(exception);
            }
        }
    }

    public Edr getEdrForAsset(@NotNull final String counterPartyAddress,
                              @NotNull final String counterPartyBpn,
                              @NotNull final String assetId,
                              @NotNull final CatalogProtocol protocol,
                              @NotNull final MediaType mediaType,
                              final boolean isFinite) throws BtpException {
        final List<Dataset> assets = requestAssetsFromCatalog(counterPartyAddress, assetId, true);

        final String contractAgreementId = negotiateContract(counterPartyAddress, counterPartyBpn, assets.get(0),
                assets.get(0).getHasPolicy().get(0), protocol);

        final String contractId = startTransfer(counterPartyAddress, counterPartyBpn, contractAgreementId,
                assets.get(0).getId(), protocol, mediaType, isFinite, getEdrCallbackAddress());

        return getEdrByContractId(contractId);
    }

    private HttpUrl getEdrCallbackAddress() {
        return HttpUrl.parse(appBaseUrl).newBuilder().addPathSegment("edr").addPathSegment("callback").build();
    }

    private IdResponse initTransfer(@NotNull final TransferRequest transferRequest)
            throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<TransferRequest> request = new HttpEntity<>(transferRequest, headers);

        ResponseEntity<IdResponse> response = null;

        try {
            response = restTemplate.exchange(
                    getTransferUrl().uri(), HttpMethod.POST, request, IdResponse.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }

        return response.getBody();
    }

    private ContractNegotiationResponse requestNegotiationState(@NotNull final String negotiationId)
            throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<ContractNegotiationResponse> request = new HttpEntity<>(headers);
        final HttpUrl requestUrl = getContractNegotiationUrl(negotiationId);

        ResponseEntity<ContractNegotiationResponse> response = null;

        try {
            response = restTemplate.exchange(
                    requestUrl.uri(), HttpMethod.GET, request, ContractNegotiationResponse.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }

        return response.getBody();
    }

    private TransferResponse requestTransferState(@NotNull final String transferId)
            throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<TransferResponse> request = new HttpEntity<>(headers);

        final HttpUrl requestUrl = getTransferUrl(transferId);
        ResponseEntity<TransferResponse> response = null;

        try {
            response = restTemplate.exchange(requestUrl.uri(), HttpMethod.GET, request, TransferResponse.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }

        return response.getBody();
    }

    private ContractNegotiationRequest generateContractNegotiationRequest(
            @NotNull final String providerConnectorDspAddress, @NotNull final String providerBpn,
            @NotNull final Dataset asset, @NotNull final Policy policy, @NotNull final CatalogProtocol protocol) {

        final ContractNegotiationRequest negotiationRequest = new ContractNegotiationRequest();

        negotiationRequest.setCounterPartyAddress(providerConnectorDspAddress);
        negotiationRequest.setProtocol(protocol);
        negotiationRequest.setProviderId(providerBpn);

        final Offer offer = new Offer();
        offer.setAssetId(asset.getId());
        offer.setOfferId(policy.getId());

        offer.setPolicy(policy);

        negotiationRequest.setOffer(offer);

        return negotiationRequest;
    }

    private IdResponse initNegotiation(@NotNull final ContractNegotiationRequest negotiationRequest)
            throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<ContractNegotiationRequest> request = new HttpEntity<>(negotiationRequest, headers);

        ResponseEntity<IdResponse> response = null;

        try {
            response = restTemplate.exchange(
                    getContractNegotiationUrl().uri(), HttpMethod.POST, request, IdResponse.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new BtpException("Got status code " + response.getStatusCode().value());
        }

        return response.getBody();
    }

    private HttpUrl getAssetDeletionUrl(@NotNull final String assetId) {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(ASSET_DELETION_PATH)
                .addPathSegments(assetId).build();
    }

    private HttpUrl getPolicyDeletionUrl(@NotNull final String policyId) {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(POLICY_DELETION_PATH)
                .addPathSegments(policyId).build();
    }

    private HttpUrl getContractDeletionUrl(@NotNull final String contractId) {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(CONTRACT_DELETION_PATH)
                .addPathSegments(contractId).build();
    }

    private HttpUrl getCatalogRequestUrl() {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(CATALOG_REQUEST_PATH).build();
    }

    private HttpUrl getAssetRegistrationUrl() {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(ASSET_REGISTRATION_PATH).build();
    }

    private HttpUrl getPolicyRegistrationUrl() {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(POLICY_REGISTRATION_PATH).build();
    }

    private HttpUrl getContractRegistrationUrl() {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(CONTRACT_REGISTRATION_PATH).build();
    }

    private HttpUrl getContractNegotiationUrl() {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(CONTRACT_NEGOTIATION_PATH).build();
    }

    private HttpUrl getContractNegotiationUrl(@NotNull final String negotiationId) {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(CONTRACT_NEGOTIATION_PATH)
                .addPathSegment(negotiationId).build();
    }

    private HttpUrl getTransferUrl() {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(CONTRACT_TRANSFER_PATH).build();
    }

    private HttpUrl getTransferUrl(@NotNull final String transferId) {
        return HttpUrl.parse(managementUrl).newBuilder()
                .addPathSegments(CONTRACT_TRANSFER_PATH)
                .addPathSegment(transferId).build();
    }

    private HttpHeaders getNewManagementApiHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(API_KEY_KEY, apiKey);
        headers.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private List<Dataset> assetsFromCatalogResult(@NotNull final CatalogResult catalogResult,
                                                  @NotNull final String assetId,
                                                  final boolean throwIfNoneFound) throws BtpException {
        final List<Dataset> assets = new ArrayList<>(5);

        catalogResult.getDataset().forEach(x -> {
            if(x.getId().equals(assetId)) {
                assets.add(x);
            }
        });

        if(throwIfNoneFound && assets.isEmpty()) {
            String assetList = "";
            for (Dataset dataset :catalogResult.getDataset()) {
                assetList += "  \"" + dataset.getId() + "\"";
            }

            throw new BtpException("No asset with id \"" + assetId + "\" found in catalog. Available "
                    + String.valueOf(catalogResult.getDataset().size()) + " assets are:"
                    + assetList);
        }

        return assets;
    }





    /* Keep old API (temp). */
    public <ResponseType> ResponseEntity<ResponseType> get(
            @NotNull final HttpUrl partnerUrl, @NotNull final String asset, @NotNull Class<ResponseType> responseClass,
            @Nullable final HttpHeaders headers) throws EdcException {
        throw new EdcException("Not implemented!");
    }

    public <BodyType, ResponseType> ResponseEntity<ResponseType> post(
            @NotNull final HttpUrl partnerUrl, @NotNull final String asset, @NotNull Class<ResponseType> responseClass,
            @NotNull BodyType body, @Nullable HttpHeaders headers) throws EdcException {
        throw new EdcException("Not implemented!");
    }
}
