package net.catena_x.btp.libraries.edc.model.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.edc.model.catalog.FormatId;
import net.catena_x.btp.libraries.edc.model.general.Type;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDestination {
    @JsonProperty("@type")
    private Type basicType = null;

    @JsonProperty("edc:type")
    private FormatId edcType = null;

    private FormatId type = FormatId.HTTP_PROXY;
}
