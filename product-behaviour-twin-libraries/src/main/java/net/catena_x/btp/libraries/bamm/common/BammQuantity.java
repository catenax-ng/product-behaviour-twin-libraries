package net.catena_x.btp.libraries.bamm.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BammQuantity {
    private long quantityNumber;
    private BammMeasurementUnit measurementUnit;
}
