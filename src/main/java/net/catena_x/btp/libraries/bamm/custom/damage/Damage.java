package net.catena_x.btp.libraries.bamm.custom.damage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.base.BammBase;
import net.catena_x.btp.libraries.bamm.common.BammLoaddataSource;
import net.catena_x.btp.libraries.bamm.common.BammStatus;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Damage implements BammBase {
    public final static String BAMM_ID = "urn:bamm:io.catenax.damage:1.0.0#Damage";

    private double damageValue;
    private BammLoaddataSource determinationLoaddataSource;
    private String determinationMethod;
    private BammStatus determinationStatus;

    @Override public String getBammId() {
        return BAMM_ID;
    }
}

