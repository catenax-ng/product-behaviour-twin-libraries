package net.catena_x.btp.libraries.edc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.catalog.CatelogService;
import net.catena_x.btp.libraries.edc.model.catalog.Dataset;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.general.Type;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogResult {
    @JsonProperty("@context")
    private Context context = Context.DEFAULT_CONTEXT_CATALOG_RESULT;

    @JsonProperty("@type")
    private Type type = Type.CATALOG;

    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("participantId")
    private String participantId = null;

    @JsonProperty("dcat:service")
    private CatelogService service = null;

    @JsonProperty("dcat:dataset")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Dataset> dataset = null;
}