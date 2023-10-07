package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.DataProviderApiConfig;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface.util.TestResult;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface.util.TestResultStore;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE_TEST)
public class DataProviderControllerRegisterGetResult {
    @Autowired TestResultStore testResultStore;
    @Autowired ApiHelper apiHelper;

    @PostMapping(value = "/registergetresult", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Integration tests"},
            summary = "Registers the result of the getresult endpoint."
    )
    public ResponseEntity<DefaultApiResult> registerGetResult(
            @RequestParam(required = false) @Nullable String contentType,
            @RequestBody @NotNull final byte[] result) {
        if(contentType == null) {
            contentType = MediaType.APPLICATION_JSON_VALUE;
        }
        testResultStore.setResultGetRequest(new TestResult(contentType, result));
        return apiHelper.ok("Result registered.");
    }
}
