package net.catena_x.btp.libraries.edc.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.edc.model.*;
import net.catena_x.btp.libraries.edc.model.asset.DataAddress;
import net.catena_x.btp.libraries.edc.model.contract.AssetsSelector;
import net.catena_x.btp.libraries.edc.model.contract.ContractOperandLeft;
import net.catena_x.btp.libraries.edc.model.contract.ContractOperator;
import net.catena_x.btp.libraries.edc.model.general.EdcElementType;
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

@Component
public class EdcApi {
    private final static String CATALOG_REQUEST_PATH = "v2/catalog/request";
    private final static String ASSET_REGISTRATION_PATH = "v3/assets";
    private final static String POLICY_REGISTRATION_PATH = "v2/policydefinitions";
    private final static String CONTRACT_REGISTRATION_PATH = "v2/contractdefinitions";

    private final static String API_KEY_KEY = "X-Api-Key";

    @Autowired private RestTemplate restTemplate;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;

    @Value("${edc.api.management.url}") private String managementUrl;
    @Value("${edc.api.management.apikey}") private String apiKey;

    public ResponseEntity<CatalogResult> requestCatalog(@NotNull final CatalogRequest catalogRequest)
            throws BtpException {
        final HttpHeaders headers = getNewManagementApiHeaders();
        final HttpEntity<CatalogRequest> request = new HttpEntity<>(catalogRequest, headers);
        final HttpUrl requestUrl = getCatalogRequestUrl();

        /*
        try {
            final String body = objectMapper.writeValueAsString(request.getBody());
            System.out.println(body);
        } catch (final Exception exception) {
        }
        */


        // response = restTemplate.postForObject(requestUrl.toString(), request, CatalogResult.class);


        try {
            return restTemplate.exchange(requestUrl.uri(), HttpMethod.POST, request, CatalogResult.class);
        } catch (final Exception exception) {
            throw new BtpException(exception);
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
        policyDefinition.getPolicy().setProhibitions(new ArrayList<>());
        policyDefinition.getPolicy().setObligations(new ArrayList<>());
        policyDefinition.getPolicy().setPermissions(new ArrayList<>());

        final PolicyRestriction permission = new PolicyRestriction();
        permission.setAction(ActionType.USE);
        permission.setConstraints(new ArrayList<>());

        final Constraint constraint = new Constraint();
        constraint.setType(EdcElementType.LOGICAL_CONSTRAINT);

        final OrConstraint or = new OrConstraint();
        or.setLeftOperand(PolicyLeftOperand.BUSINESS_PARTNER_NUMBER);
        or.setOperator(new PolicyOperatorStructured(PolicyOperatorStructuredId.EQUALS));
        or.setRightOperand(partnerBPN);
        constraint.setOr(new ArrayList<>());
        constraint.getOr().add(or);
        permission.getConstraints().add(constraint);

        policyDefinition.getPolicy().getPermissions().add(permission);

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

/*
@Component
public class EdcApi {
    @Autowired private RestTemplate restTemplate;
    @Value("${edc.apiwrapper.url}") private String apiWrapperUrl;
    @Value("${edc.apiwrapper.submodelPath}") private String submodelPath;            //"api/service"
    @Value("${edc.apiwrapper.submodel}") private String submodel;                    //"submodel"
    @Value("${edc.apiwrapper.providerEdcUrlKey}") private String providerEdcUrlKey;  //"provider-connector-url"
    @Value("${edc.apiwrapper.username}") private String apiWrapperUsername;
    @Value("${edc.apiwrapper.password}") private String apiWrapperPassword;

    private final Logger logger = LoggerFactory.getLogger(EdcApi.class);

    public <ResponseType> ResponseEntity<ResponseType> get(
            @NotNull final HttpUrl partnerUrl, @NotNull final String asset, @NotNull Class<ResponseType> responseClass,
            @Nullable final HttpHeaders headers) throws EdcException {

        addAuthorizationHeaders(headers);

        final HttpEntity<String> request = new HttpEntity<>(headers);

        final HttpUrl requestUrl = buildApiWrapperUrl(partnerUrl, asset);

        logger.info("API-WRAPPER request: GET " + requestUrl.toString());

        final ResponseEntity<ResponseType> response = restTemplate.exchange(
                requestUrl.uri(), HttpMethod.GET, request, responseClass);

        try {
            ResponseChecker.checkResponse(response);
        } catch (final BtpException exception) {
            throw new EdcException(exception);
        }

        return response;
    }

    public <BodyType, ResponseType> ResponseEntity<ResponseType> post(
            @NotNull final HttpUrl partnerUrl,
            @NotNull final String asset,
            @NotNull Class<ResponseType> responseClass,
            @NotNull BodyType body,
            @Nullable HttpHeaders headers) throws EdcException {

        addAuthorizationHeaders(headers);
        final HttpEntity<BodyType> request = new HttpEntity<>(body, headers);

        final HttpUrl requestUrl = buildApiWrapperUrl(partnerUrl, asset);

        logger.info("API-WRAPPER request: POST " + requestUrl.toString());

        final ResponseEntity<ResponseType> response = restTemplate.postForEntity(
                requestUrl.uri(), request, responseClass);

        try {
            ResponseChecker.checkResponse(response);
        } catch (final BtpException exception) {
            throw new EdcException(exception);
        }

        return response;
    }

    private HttpUrl buildApiWrapperUrl(@NotNull final HttpUrl partnerUrl, @NotNull final String asset) {
        String partnerUrlAsString = partnerUrl.toString();
        if(partnerUrlAsString.endsWith("/")) {
            partnerUrlAsString = partnerUrlAsString.substring(0, partnerUrlAsString.length() - 1);
        }

        final HttpUrl url = HttpUrl.parse(this.apiWrapperUrl).newBuilder()
                                     .addPathSegments(submodelPath)
                                     .addEncodedPathSegment(asset)
                                     .addPathSegment(submodel)
                                     .addEncodedQueryParameter(providerEdcUrlKey, partnerUrlAsString)
                                     .build();

        return url;
    }

    private void addAuthorizationHeaders(final HttpHeaders headers) {
        headers.add("Authorization", getAuthString());
    }

    private String getAuthString() {
        StringBuilder sb = new StringBuilder();

        String authStr = sb.append(apiWrapperUsername).append(":").append(apiWrapperPassword).toString();
        sb.setLength(0);
        sb.append("Basic ").append(Base64.getEncoder().encodeToString(authStr.getBytes()));
        return sb.toString();
    }
}
*/
