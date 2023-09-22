package net.catena_x.btp.hi.oem.backend.hi_service.collector.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIUpdateOptions {
    private boolean renameLoadSpectrumToLoadCollective = false;
    private boolean limitVehicleTwinCount = false;
    private int maxVehicleTwins = -1;
    private boolean recalculateAllVehicles = false;
    private boolean forceCalculationIgnoringQueue = false;
    private boolean resetHiDatabase = false;
    private boolean usePredefinedResults = false;
    private boolean useKnowledgeAgent = false;

    public boolean appliesChanges() {
        return renameLoadSpectrumToLoadCollective || limitVehicleTwinCount
                || recalculateAllVehicles || forceCalculationIgnoringQueue || resetHiDatabase || useKnowledgeAgent
                || usePredefinedResults;
    }

    public boolean equals(@Nullable final HIUpdateOptions other) {
        if(this.renameLoadSpectrumToLoadCollective != other.renameLoadSpectrumToLoadCollective){ return false;}
        if(this.limitVehicleTwinCount != other.limitVehicleTwinCount){ return false;}
        if(this.maxVehicleTwins != other.maxVehicleTwins){ return false;}
        if(this.recalculateAllVehicles != other.recalculateAllVehicles){ return false;}
        if(this.forceCalculationIgnoringQueue != other.forceCalculationIgnoringQueue){ return false;}
        if(this.resetHiDatabase != other.resetHiDatabase){ return false;}
        if(this.usePredefinedResults != other.usePredefinedResults){ return false;}
        if(this.useKnowledgeAgent != other.useKnowledgeAgent){ return false;}

        return true;
    }
}