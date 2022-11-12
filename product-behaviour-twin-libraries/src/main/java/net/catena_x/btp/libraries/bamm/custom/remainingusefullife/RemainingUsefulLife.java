package net.catena_x.btp.libraries.bamm.custom.remainingusefullife;

import lombok.Getter;
import lombok.Setter;
import net.catena_x.btp.libraries.bamm.base.BammBase;
import net.catena_x.btp.libraries.bamm.common.BammLoaddataSource;
import net.catena_x.btp.libraries.bamm.common.BammStatus;

@Getter
@Setter
public class RemainingUsefulLife implements BammBase {
    public final static String BAMM_ID = "urn:bamm:io.catenax.rul:1.0.0##RemainingUsefulLife";

    private String remainingOperatingTime;
    private long remainingRunningDistance;
    private BammLoaddataSource determinationLoaddataSource;
    private BammStatus determinationStatus;

    @Override public String getBammId() {
        return BAMM_ID;
    }
}
