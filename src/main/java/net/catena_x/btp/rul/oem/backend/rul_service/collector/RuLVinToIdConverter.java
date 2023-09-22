package net.catena_x.btp.rul.oem.backend.rul_service.collector;

import net.catena_x.btp.rul.oem.backend.model.dto.vinrelation.RuLVinRelation;
import net.catena_x.btp.rul.oem.backend.model.dto.vinrelation.RuLVinRelationTable;
import net.catena_x.btp.rul.oem.backend.rul_service.collector.util.exceptions.OemRuLUnknownVehicleException;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLVinToIdConverter {
    @Autowired RuLVinRelationTable vinRelationTable;

    public RuLVinRelation getByVin(@NotNull final String vin) throws OemRuLException {
        final RuLVinRelation relation = vinRelationTable.getByVinNewTransaction(vin);
        assetRelation(relation, vin);

        return relation;
    }

    private void assetRelation(@NotNull final RuLVinRelation relation, @NotNull final String vin)
            throws OemRuLException {
        if(relation == null) {
            throw new OemRuLUnknownVehicleException("Unknown VIN " + vin + "!");
        }
    }
}
