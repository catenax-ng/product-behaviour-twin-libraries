package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.DataProviderApiConfig;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE_TEST)
public class DataProviderControllerTestGet {
    @Autowired ApiHelper apiHelper;

    @GetMapping(value = "/get", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Integration tests"},
            summary = "Test endpoint with get method.",
            description = "Returns a default JSON object."
    )
    public ResponseEntity<DefaultApiResult> getResult() {
        return apiHelper.ok("Ok");
    }
}
