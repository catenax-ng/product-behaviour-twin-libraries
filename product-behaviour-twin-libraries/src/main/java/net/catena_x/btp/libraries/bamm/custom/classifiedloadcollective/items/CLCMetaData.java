package net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective.items;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.common.BammStatus;

@Getter
@Setter
public class CLCMetaData {
    private String projectDescription;
    private String componentDescription;
    private String routeDescription;
    private BammStatus status;
}
