package net.catena_x.btp.libraries.bamm.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BammStatus {
    private String routeDescription;
    private Instant date;
    private String operatingTime;
    private long mileage;
}
