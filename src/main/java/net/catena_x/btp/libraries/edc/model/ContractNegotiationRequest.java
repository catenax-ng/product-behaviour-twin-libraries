package net.catena_x.btp.libraries.edc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.general.Type;
import net.catena_x.btp.libraries.edc.model.negotiation.Offer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractNegotiationRequest {
    @JsonProperty("@context")
    private Context context = Context.DEFAULT_CONTEXT_CONTRACT_NEGOTIATION;

    @JsonProperty("@type")
    private Type type = Type.NEGOTIATION_INITIATE_REQUEST;

    private String counterPartyAddress = null;
    private CatalogProtocol protocol = CatalogProtocol.HTTP;
    private String providerId = null;
    private Offer offer = null;
}