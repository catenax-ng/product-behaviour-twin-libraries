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
public class Distribution {
    @JsonProperty("@type")
    private Type type = Type.CATALOG_DISTRIBUTION;

    @JsonProperty("dct:format")
    private DistributionFormat format = null;

    @JsonProperty("dcat:accessService")
    private String accessService = null;
}
