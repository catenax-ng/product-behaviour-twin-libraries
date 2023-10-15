package net.catena_x.btp.libraries.edc.model.negotiation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.general.Context;
import net.catena_x.btp.libraries.edc.model.general.Type;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdResponse {
    @JsonProperty("@context")
    private Context context = null;

    @JsonProperty("@type")
    private Type type = Type.ID_RESPONSE;

    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("edc:createdAt")
    private Long createdAt = null;
}
