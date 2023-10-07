package net.catena_x.btp.libraries.edc.model.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.Type;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatelogService {
    @JsonProperty("@type")
    private Type type = Type.CATALOG_DATA_SERVICE;

    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("dct:terms")
    private String terms = null;

    @JsonProperty("dct:endpointUrl")
    private String endpointUrl = null;
}
