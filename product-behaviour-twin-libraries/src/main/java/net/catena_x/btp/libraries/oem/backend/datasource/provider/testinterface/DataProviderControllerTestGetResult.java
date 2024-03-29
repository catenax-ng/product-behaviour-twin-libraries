package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.DataProviderApiConfig;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface.util.TestResultStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE_TEST)
public class DataProviderControllerTestGetResult {
    @Autowired TestResultStore testResultStore;

    @GetMapping(value = "/getresult")
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Integration tests"},
            summary = "Test endpoint with get method, returns preregistered content.",
            description = "Returns the content that was preregistered with the registergetresult endpoint."
    )
    public ResponseEntity<byte[]> getResult() {
        return testResultStore.makeGetResponse();
    }
}
