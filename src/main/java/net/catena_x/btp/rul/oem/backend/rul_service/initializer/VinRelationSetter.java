package net.catena_x.btp.rul.oem.backend.rul_service.initializer;

import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.rul.oem.backend.database.rul.tables.vinrelation.RuLVinRelationTableInternal;
import net.catena_x.btp.rul.oem.backend.model.dto.vinrelation.RuLVinRelationTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class VinRelationSetter {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RuLVinRelationTable rulVinRelationTable;

    public ResponseEntity<DefaultApiResult> setRelation(@NotNull final String vin, @NotNull final String refId) {
        try {
            rulVinRelationTable.insertNewTransaction(vin, refId, refId.equals(RuLVinRelationTableInternal.NO_DATA_REF));
        } catch (final Exception exception) {
            return apiHelper.failed("Setting VIN relation failed: " + exception.getMessage());
        }

        return apiHelper.ok("VIN relation set successfully.");
    }
}
