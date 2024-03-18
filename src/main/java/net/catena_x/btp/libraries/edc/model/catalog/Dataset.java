package net.catena_x.btp.libraries.edc.model.catalog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.Policy;
import net.catena_x.btp.libraries.edc.model.general.Type;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dataset {
    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("id")
    private String Id = null;

    @JsonProperty("@type")
    private Type type = Type.CATALOG_DATASET;

    @JsonProperty("odrl:hasPolicy")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Policy> hasPolicy = null;

    @JsonProperty("dcat:distribution")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Distribution> distribution = null;

    @JsonProperty("description")
    private String description = null;
}
