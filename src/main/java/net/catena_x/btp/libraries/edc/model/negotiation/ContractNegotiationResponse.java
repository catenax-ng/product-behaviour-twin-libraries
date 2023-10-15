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

    @JsonProperty("edc:type")
    private Type edcType = Type.CONSUMER;

    @JsonProperty("edc:protocol")
    private CatalogProtocol protocol = null;

    @JsonProperty("edc:state")
    private ContractNegotiationState state = null;

    @JsonProperty("edc:counterPartyId")
    private String counterPartyId = null;

    @JsonProperty("edc:counterPartyAddress")
    private String counterPartyAddress = null;

    @JsonProperty("edc:callbackAddresses")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> callbackAddresses = null;

    @JsonProperty("edc:createdAt")
    private Long createdAt = null;

    @JsonProperty("edc:contractAgreementId")
    private String contractAgreementId = null;
}
