package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger.ResetDoc;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.util.RawdataInfoTableInitializer;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.sync.SyncTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsDataTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DataUpdaterApiConfig.API_PATH_BASE)
public class DataUpdaterControllerReset {
    @Autowired private RawdataInfoTableInitializer rawdataInfoTableInitializer;
    @Autowired private ApiHelper apiHelper;
    @Autowired private InfoTable infoTable;
    @Autowired private VehicleTable vehicleTable;
    @Autowired private TelematicsDataTable telematicsDataTable;
    @Autowired private SyncTable syncTable;

    @GetMapping(value = "/reset", produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
            summary = ResetDoc.SUMMARY, description = ResetDoc.DESCRIPTION,
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = ResetDoc.RESPONSE_OK_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = ResetDoc.RESPONSE_OK_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = ResetDoc.RESPONSE_ERROR_DESCRIPTION,
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    value = ResetDoc.RESPONSE_ERROR_VALUE
                                            )},
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = DefaultApiResult.class)
                            ))
            }
    )
    public ResponseEntity<DefaultApiResult> reset() throws OemDatabaseException {
        try {
            infoTable.deleteAllNewTransaction();

            rawdataInfoTableInitializer.init();

            vehicleTable.deleteAllNewTransaction();
            telematicsDataTable.deleteAllNewTransaction();
            syncTable.reInitDefaultNewTransaction();

            return apiHelper.ok("Rawdata database cleared and reinitialized.");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }
}