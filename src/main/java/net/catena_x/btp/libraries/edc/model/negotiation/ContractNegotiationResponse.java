package net.catena_x.btp.libraries.edc.model.negotiation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.general.Type;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractNegotiationResponse {
    @JsonProperty("@context")
    private Context context = null;

    @JsonProperty("@type")
    private Type type = Type.CONTRACT_NEGOTIATION;

    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("type")
    private Type edcType = Type.CONSUMER;

    @JsonProperty("protocol")
    private CatalogProtocol protocol = null;

    @JsonProperty("state")
    private ContractNegotiationState state = null;

    @JsonProperty("counterPartyId")
    private String counterPartyId = null;

    @JsonProperty("counterPartyAddress")
    private String counterPartyAddress = null;

    @JsonProperty("callbackAddresses")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> callbackAddresses = null;

    @JsonProperty("createdAt")
    private Long createdAt = null;

    @JsonProperty("contractAgreementId")
    private String contractAgreementId = null;
}
