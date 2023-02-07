package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.DataProviderApiConfig;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface.util.TestResult;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface.util.TestResultStore;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE_TEST)
public class DataProviderControllerRegisterPostResult {
    @Autowired TestResultStore testResultStore;
    @Autowired ApiHelper apiHelper;

    @PostMapping(value = "/registerpostresult", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Integration tests"},
            summary = "Registers the result of the postresult endpoint."
    )
    public ResponseEntity<DefaultApiResult> registerPostResult(
            @RequestParam(required = false) @Nullable String contentType,
            @RequestBody @NotNull final byte[] result) {
        if(contentType == null) {
            contentType = "application/json";
        }
        testResultStore.setResultPostRequest(new TestResult(contentType, result));
        return apiHelper.ok("Result registered.");
    }
}
