package net.catena_x.btp.libraries.bamm.custom.serialparttypization.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SPTPartTypeInformation {
    private String classification;
    private String manufacturerPartId;
    private String nameAtManufacturer;
    private String customerPartId;
    private String nameAtCustomer;
}
