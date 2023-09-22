package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.common.BammStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CLSMetaData {
    private String projectDescription;
    private LoadSpectrumType componentDescription;
    private String routeDescription;
    private BammStatus status;
}
