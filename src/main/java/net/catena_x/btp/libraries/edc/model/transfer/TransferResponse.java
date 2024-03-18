package net.catena_x.btp.libraries.edc.model.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.general.Type;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferResponse {
    @JsonProperty("@context")
    private Context context = null;

    @JsonProperty("@type")
    private Type type = Type.TRANSFER_PROCESS;

    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("correlationId")
    private String correlationId = null;

    @JsonProperty("state")
    private TransferState state = null;

    @JsonProperty("stateTimestamp")
    private Long stateTimestamp = null;

    @JsonProperty("type")
    private Type edcType = Type.CONSUMER;

    @JsonProperty("assetId")
    private String assetId = null;

    @JsonProperty("contractId")
    private String contractId = null;

    @JsonProperty("callbackAddresses")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> callbackAddresses = null;

    @JsonProperty("dataDestination")
    private DataDestination dataDestination = null;

    @JsonProperty("connectorId")
    private String counterPartyId = null;
}