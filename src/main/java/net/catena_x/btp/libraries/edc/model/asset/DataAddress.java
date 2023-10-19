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
    private String proxyMethod = null;
    private String proxyBody = null;
    private String proxyQueryParams = null;
}
