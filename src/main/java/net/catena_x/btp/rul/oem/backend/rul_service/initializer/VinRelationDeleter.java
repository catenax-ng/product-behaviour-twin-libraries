package net.catena_x.btp.rul.oem.backend.rul_service.initializer;

import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.rul.oem.backend.model.dto.vinrelation.RuLVinRelationTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class VinRelationDeleter {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RuLVinRelationTable rulVinRelationTable;

    public ResponseEntity<DefaultApiResult> deleteRelation(@NotNull final String vin) {
        try {
            rulVinRelationTable.deleteByVinNewTransaction(vin);
        } catch (final Exception exception) {
            return apiHelper.failed("VIN relation deletion failed: " + exception.getMessage());
        }

        return apiHelper.ok("VIN relation deleted successfully.");
    }
}
