package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResultType;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResult;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.util.ApiHelper;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.sync.SyncTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsDataTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@EnableTransactionManagement
@RequestMapping("/api/rawdata")
public class RawdataInputController {
    @Autowired private InfoTable infoTable;
    @Autowired private VehicleTable vehicleTable;
    @Autowired private TelematicsDataTable telematicsDataTable;
    @Autowired private SyncTable syncTable;
    @Autowired private ApiHelper apiHelper;

    @PostMapping("/info/set")
    public ResponseEntity<ApiResult> infoSet(@RequestBody @NotNull final InputInfo item) {
        InfoKey key;

        try {
            key = InfoKey.valueOf(item.key());
        }
        catch(Exception exception) {
            return apiHelper.failed(exception.toString());
        }

        try {
            infoTable.setInfoItemNewTransaction(key, item.value());
            return apiHelper.ok(item.key() + " set to \"" + item.value() + "\"");
        }
        catch(OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @GetMapping("/info/get/{key}")
    public ResponseEntity<ApiResult> infoGet(@PathVariable @NotNull final InfoKey key) {
        try {
            return apiHelper.okWithValue(infoTable.getInfoValueNewTransaction(key));
        }
        catch(OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @GetMapping("/info/init")
    public ResponseEntity<ApiResult> infoInit() {
        try {
            infoInitIntern();
            return apiHelper.okWithValue("");
        }
        catch(OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    private void infoInitIntern() throws OemDatabaseException {
        infoTable.setInfoItemNewTransaction(InfoKey.DATAVERSION, "DV_0.0.99");
        infoTable.setInfoItemNewTransaction(InfoKey.ADAPTIONVALUEINFO, "{}");
        infoTable.setInfoItemNewTransaction(InfoKey.LOADSPECTRUMINFO,
                "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
    }

    @GetMapping("/reset")
    public ResponseEntity<ApiResult> reset() throws OemDatabaseException {
        try {
            infoTable.deleteAllNewTransaction();

            infoInitIntern();

            vehicleTable.deleteAllNewTransaction();
            telematicsDataTable.deleteAllNewTransaction();
            syncTable.reInitDefaultNewTransaction();

            return apiHelper.ok("Rawdata database cleared and reinitialized.");
        }
        catch(OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @PostMapping("/vehicle/register")
    public ResponseEntity<ApiResult> registerVehicle(@RequestBody @NotNull final VehicleInfo vehicle) {
        try {
            vehicleTable.registerVehicleNewTransaction(vehicle);
            return apiHelper.ok("Vehicle registered.");
        }
        catch(OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @PostMapping("/telematicsdata/add")
    public ResponseEntity<ApiResult> addTelemeticsData(@RequestBody @NotNull final InputTelematicsData telematicsData) {
        try {
            vehicleTable.appendTelematicsDataNewTransaction(telematicsData);
            return apiHelper.ok("Telematics data added.");
        }
        catch(OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

}
