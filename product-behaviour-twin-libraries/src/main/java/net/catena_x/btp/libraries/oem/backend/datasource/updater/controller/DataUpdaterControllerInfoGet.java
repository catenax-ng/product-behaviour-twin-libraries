package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(DataUpdaterApiConfig.API_PATH_BASE)
public class DataUpdaterControllerInfoGet {
    @Autowired private ApiHelper apiHelper;
    @Autowired private InfoTable infoTable;

    @GetMapping(value = "/info/get/{key}", produces = "application/json")
    public ResponseEntity<String> infoGet(@PathVariable @NotNull final InfoKey key) {
        try {
            return apiHelper.okWithValueAsString(null, infoTable.getInfoValueNewTransaction(key));
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failedAsString(exception.toString());
        }
    }
}
