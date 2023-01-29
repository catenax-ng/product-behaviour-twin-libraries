package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.DataProviderApiConfig;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface.util.TestResultStore;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE_TEST)
public class DataProviderControllerTestPostResult {
    @Autowired TestResultStore testResultStore;

    @PostMapping(value = "/postresult")
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Integration tests"},
            summary = "Test endpoint with post method, returns preregistered content.",
            description = "Returns the content that was preregistered with the registerpostresult endpoint. The request body is ignored."
    )
    public ResponseEntity<byte[]> postResult(@RequestBody(required = false) @Nullable final byte[] body) {
        return testResultStore.makePostResponse();
    }
}
