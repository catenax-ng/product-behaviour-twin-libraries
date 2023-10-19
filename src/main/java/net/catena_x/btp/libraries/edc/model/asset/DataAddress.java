package net.catena_x.btp.libraries.edc.model.asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataAddress {
    private DataAddressType type = DataAddressType.HTTP_DATA;
    private String baseUrl = null;
    private Boolean proxyMethod = null;
    private Boolean proxyBody = null;
    private Boolean proxyQueryParams = null;
}
