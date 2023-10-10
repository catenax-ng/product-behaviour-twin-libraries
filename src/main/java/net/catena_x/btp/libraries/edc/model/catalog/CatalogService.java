package net.catena_x.btp.libraries.edc.model.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.EdcElementType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogService {

    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("@type")
    private EdcElementType type = null;

    @JsonProperty("dct:terms")
    private String terms = null;

    @JsonProperty("dct:endpointUrl")
    private String endpointUrl = null;
}
