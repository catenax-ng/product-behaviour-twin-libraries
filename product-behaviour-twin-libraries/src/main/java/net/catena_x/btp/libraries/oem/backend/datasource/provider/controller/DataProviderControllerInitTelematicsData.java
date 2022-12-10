package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi.TelematicsDataUpdater;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE)
public class DataProviderControllerInitTelematicsData {
    @Autowired private ApiHelper apiHelper;
    @Autowired private TelematicsDataUpdater telematicsDataUpdater;
    @Autowired private TestDataManager testDataManager;

    @GetMapping(value = "/init/telematicsdata", produces = "application/json")
    public ResponseEntity<DefaultApiResult> initTelematicsData() {
        try {
            testDataManager.lock();
            telematicsDataUpdater.updateFromTestDataCetegorized(testDataManager.getTestDataCategorized(
                    null, true));
            return apiHelper.ok("Telematics data initialized.");
        }
        catch(final DataProviderException exception) {
            return apiHelper.failed(exception.toString());
        } finally {
            testDataManager.unlock();
        }
    }
}
