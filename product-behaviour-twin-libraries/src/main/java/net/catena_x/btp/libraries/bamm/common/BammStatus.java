package net.catena_x.btp.libraries.bamm.common;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BammStatus {
    private String routeDescription;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Instant date;
    private String operatingTime;
    private Long mileage;
}
