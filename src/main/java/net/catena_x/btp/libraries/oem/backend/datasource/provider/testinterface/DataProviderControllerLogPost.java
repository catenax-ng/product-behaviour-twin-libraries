package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.DataProviderApiConfig;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(DataProviderApiConfig.API_PATH_BASE_TEST)
public class DataProviderControllerLogPost {
    @Autowired ApiHelper apiHelper;

    private final Logger logger = LoggerFactory.getLogger(DataProviderControllerLogPost.class);

    @PostMapping(value = "/logpost", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Integration tests"},
            summary = "Logs the body of the post request."
    )
    public ResponseEntity<DefaultApiResult> logPost(
            @RequestBody @NotNull final byte[] result) {
        logger.info("Received post request with body: =======================================\n"
                                + new String(result, StandardCharsets.UTF_8)
                                + "\n=======================================" );
        return apiHelper.ok("Result logged.");
    }
}
