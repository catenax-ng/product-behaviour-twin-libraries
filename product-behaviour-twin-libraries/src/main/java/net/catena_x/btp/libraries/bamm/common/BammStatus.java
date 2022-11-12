package net.catena_x.btp.libraries.bamm.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BammStatus {
    private String routeDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private String operatingTime; //FA: String correct?
    private Long mileage;
}
