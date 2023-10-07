package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.DataProviderApiConfig;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE_TEST)
public class DataProviderControllerTestPost {
    @Autowired ApiHelper apiHelper;

    @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Integration tests"},
            summary = "Test endpoint with post method, returns embedded post body.",
            description = "Returns a default JSON object, with embedded request post body."
    )
    public ResponseEntity<DefaultApiResult> postResult(@RequestBody @Nullable final byte[] result) {
        if(result == null) {
            return apiHelper.ok("Ok, but no body was sent.");
        }

        try {
            return apiHelper.ok("Ok, you sent me: " + convertResult(result));
        } catch (final Exception exception) {
            return apiHelper.ok("Maybe ok, but you sent me not a valid string.");
        }
    }

    private String convertResult(@NotNull final byte[] result) {
        final String converted = new String(Arrays.copyOfRange(result, 0, Math.min(10000, result.length)),
                                            StandardCharsets.UTF_8);
        if(result.length > 10000) {
            return "First 10000 characters of your input:\n" + converted + " ...";
        }

        return converted;
    }
}
