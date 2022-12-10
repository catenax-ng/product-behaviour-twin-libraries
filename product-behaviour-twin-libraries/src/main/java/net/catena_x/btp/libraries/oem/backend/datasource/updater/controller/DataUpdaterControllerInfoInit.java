package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.util.RawdataInfoTableInitializer;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DataUpdaterApiConfig.API_PATH_BASE)
public class DataUpdaterControllerInfoInit {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RawdataInfoTableInitializer rawdataInfoTableInitializer;

    @GetMapping(value = "/info/init", produces = "application/json")
    public ResponseEntity<DefaultApiResult> infoInit() {
        try {
            rawdataInfoTableInitializer.init();
            return apiHelper.ok("Initialized rawdata info elements.");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }
}