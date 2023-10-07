package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger.InfoGetDoc;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResultWithValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/info/get/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Development"},
            summary = InfoGetDoc.SUMMARY, description = InfoGetDoc.DESCRIPTION,
            parameters = @io.swagger.v3.oas.annotations.Parameter(
                    name = InfoGetDoc.KEY_NAME,
                    description = InfoGetDoc.KEY_DESCRIPTION, required = true,
                        examples = {
                                @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        name = InfoGetDoc.KEY_EXAMPLE_1_NAME,
                                        description = InfoGetDoc.KEY_EXAMPLE_1_DESCRIPTION,
                                        value = InfoGetDoc.KEY_EXAMPLE_1_VALUE
                                ),
                                @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        name = InfoGetDoc.KEY_EXAMPLE_2_NAME,
                                        description = InfoGetDoc.KEY_EXAMPLE_2_DESCRIPTION,
                                        value = InfoGetDoc.KEY_EXAMPLE_2_VALUE
                                ),
                                @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        name = InfoGetDoc.KEY_EXAMPLE_3_NAME,
                                        description = InfoGetDoc.KEY_EXAMPLE_3_DESCRIPTION,
                                        value = InfoGetDoc.KEY_EXAMPLE_3_VALUE
                                )
                        }
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = InfoGetDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InfoGetDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResultWithValue.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = InfoGetDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InfoGetDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<String> infoGet(@PathVariable @NotNull final InfoKey key) {
        try {
            final String value = infoTable.getInfoValueNewTransaction(key);
            if(value == null) {
                return apiHelper.failedAsString("Info element for key " + key + " not found!");
            }

            return apiHelper.okWithValueAsString(null, value);
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failedAsString(exception.toString());
        }
    }
}
