package net.catena_x.btp.libraries.edc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.catalog.CatalogProtocol;
import net.catena_x.btp.libraries.edc.model.catalog.QuerySpec;
import net.catena_x.btp.libraries.edc.model.general.Context;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogRequest {
    @JsonProperty("@context")
    private Context context = Context.DEFAULT_CONTEXT_CATALOG;

    private CatalogProtocol protocol = CatalogProtocol.HTTP;
    private String counterPartyAddress = null;
    private QuerySpec querySpec = null;
}
