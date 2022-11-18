package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.common.BammStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CLSMetaData {
    private String projectDescription;
    private LoadSpectrumType componentDescription;
    private String routeDescription;
    private BammStatus status;
}
