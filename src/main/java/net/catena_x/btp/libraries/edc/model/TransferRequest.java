package net.catena_x.btp.libraries.edc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.transfer.DataDestination;
import net.catena_x.btp.libraries.edc.model.transfer.TransferType;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRequest {
    @JsonProperty("@context")
    private Context context = Context.DEFAULT_CONTEXT_TRANSFER;
    private String assetId = null;
    private String connectorAddress = null;
    private String contractId = null;
    private String connectorId = null;
    private DataDestination dataDestination = null;
    private Boolean managedResources = null;
    private Map<String, String> privateProperties = null;
    private CatalogProtocol protocol = CatalogProtocol.HTTP;
    private TransferType transferType = null;
}