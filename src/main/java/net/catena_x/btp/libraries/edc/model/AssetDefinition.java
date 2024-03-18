package net.catena_x.btp.libraries.edc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.asset.DataAddress;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.general.Type;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetDefinition {
    @JsonProperty("@context")
    private Context context = Context.DEFAULT_CONTEXT_ASSET;

    @JsonProperty("@type")
    private Type type = Type.ASSET_DEFINITION;

    @JsonProperty("@id")
    private String id = null;

    private Map<String, String> properties = new HashMap<>();

    //private Map<String, String> privateProperties = new HashMap<>();

    private DataAddress dataAddress = null;
}