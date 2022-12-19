package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger.InfoSetDoc;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(DataUpdaterApiConfig.API_PATH_BASE)
public class DataUpdaterControllerInfoSet {
    @Autowired private ApiHelper apiHelper;
    @Autowired private InfoTable infoTable;

    @PostMapping(value = "/info/set", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            tags = {"Development"},
            summary = InfoSetDoc.SUMMARY, description = InfoSetDoc.DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = InfoSetDoc.BODY_DESCRIPTION, required = true,
                    content =  @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InfoSetDoc.BODY_EXAMPLE_1_NAME,
                                            description = InfoSetDoc.BODY_EXAMPLE_1_DESCRIPTION,
                                            value = InfoSetDoc.BODY_EXAMPLE_1_VALUE
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InfoSetDoc.BODY_EXAMPLE_2_NAME,
                                            description = InfoSetDoc.BODY_EXAMPLE_2_DESCRIPTION,
                                            value = InfoSetDoc.BODY_EXAMPLE_2_VALUE
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = InfoSetDoc.BODY_EXAMPLE_3_NAME,
                                            description = InfoSetDoc.BODY_EXAMPLE_3_DESCRIPTION,
                                            value = InfoSetDoc.BODY_EXAMPLE_3_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = InfoSetDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InfoSetDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = InfoSetDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = InfoSetDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> infoSet(@RequestBody @NotNull final InputInfo item) {
        InfoKey key;

        try {
            key = InfoKey.valueOf(item.key());
        }
        catch(final Exception exception) {
            return apiHelper.failed(exception.toString());
        }

        try {
            infoTable.setInfoItemNewTransaction(key, item.value());
            return apiHelper.ok(item.key() + " set to \"" + item.value() + "\"");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }
}
