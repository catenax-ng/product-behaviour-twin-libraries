package net.catena_x.btp.libraries.bamm.custom.serialparttypization.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SPTPartTypeInformation {
    private String classification;
    private String manufacturerPartId;
    private String nameAtManufacturer;
    private String customerPartId;
    private String nameAtCustomer;
}
