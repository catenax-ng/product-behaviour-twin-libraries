package net.catena_x.btp.hi.oem.backend.hi_service.controller.util;

import net.catena_x.btp.hi.oem.common.model.dto.calculation.HICalculationTable;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HIDbMaintainer {
    @Autowired private ApiHelper apiHelper;
    @Autowired private HICalculationTable hiCalculationTable;

    public ResponseEntity<DefaultApiResult> reset() {
        try {
            return resetDb();
        } catch (final Exception exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    private ResponseEntity<DefaultApiResult> resetDb() throws OemHIException {
        hiCalculationTable.resetDbNewTransaction();
        return apiHelper.ok("Hi database reset.");
    }
}
