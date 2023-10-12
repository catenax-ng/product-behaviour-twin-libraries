package net.catena_x.btp.libraries.edc.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.edc.model.*;
import net.catena_x.btp.libraries.edc.model.asset.DataAddress;
import net.catena_x.btp.libraries.edc.model.catalog.Dataset;
import net.catena_x.btp.libraries.edc.model.catalog.QuerySpec;
import net.catena_x.btp.libraries.edc.model.contract.AssetsSelector;
import net.catena_x.btp.libraries.edc.model.contract.ContractOperandLeft;
import net.catena_x.btp.libraries.edc.model.contract.ContractOperator;
import net.catena_x.btp.libraries.edc.model.general.Policy;
import net.catena_x.btp.libraries.edc.model.general.Type;
import net.catena_x.btp.libraries.edc.model.policy.*;
import net.catena_x.btp.libraries.edc.util.exceptions.EdcException;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
public class EdcApi {
    private final static String CATALOG_REQUEST_PATH = "v2/catalog/request";
    private final static String ASSET_REGISTRATION_PATH = "v3/assets";
    private final static String POLICY_REGISTRATION_PATH = "v2/policydefinitions";
    private final static String CONTRACT_REGISTRATION_PATH = "v2/contractdefinitions";

    private final static String API_KEY_KEY = "X-Api-Key";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER)
    private ObjectMapper objectMapper;

    @Value("${edc.api.management.url}")
    private String managementUrl;
    @Value("${edc.api.management.apikey}")
    private String apiKey;

    public CatalogResult requestCatalog(@NotNull final String counterPartyAddress)
            throws BtpException {
        final CatalogRequest catalogRequest = new CatalogRequest();
        catalogRequest.setQuerySpec(new QuerySpec());
        catalogRequest.setCounterPartyAddress(counterPartyAddress);
        return requestCatalog(catalogRequest);
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

    public List<Dataset> requestAssetsFromCatalog(@NotNull final CatalogRequest catalogRequest,
                                                  @NotNull final String assetId) throws BtpException {
        return assetsFromCatalogResult(requestCatalog(catalogRequest), assetId);
    }

    public List<Dataset> requestAssetsFromCatalog(@NotNull final String counterPartyAddress,
                                                  @NotNull final String assetId) throws BtpException {
        return assetsFromCatalogResult(requestCatalog(counterPartyAddress), assetId);
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

    public void registerAsset(@NotNull final String assetId, @NotNull final String dataAddress) throws BtpException {
        final AssetDefinition assetDefinition = new AssetDefinition();
        assetDefinition.setId(assetId);
        assetDefinition.setDataAddress(new DataAddress());
        assetDefinition.getDataAddress().setBaseUrl(dataAddress);
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

    private HttpHeaders getNewManagementApiHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(API_KEY_KEY, apiKey);
        return headers;
    }

    private List<Dataset> assetsFromCatalogResult(@NotNull final CatalogResult catalogResult,
                                                  @NotNull final String assetId) {
        final List<Dataset> assets = new ArrayList<>(5);

        catalogResult.getDataset().forEach(x -> {
            if(x.getId().equals(assetId)) {
                assets.add(x);
            }
        });

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
