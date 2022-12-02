package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.sync.SyncTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsDataTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

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

    @GetMapping("/info/get/{key}")
    public ResponseEntity<String> infoGet(@PathVariable @NotNull final InfoKey key) {
        try {
            return apiHelper.okWithValueAsString(null, infoTable.getInfoValueNewTransaction(key));
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failedAsString(exception.toString());
        }
    }

    @GetMapping("/info/init")
    public ResponseEntity<DefaultApiResult> infoInit() {
        try {
            infoInitInternal();
            return apiHelper.ok("Initialized rawdata info elements.");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    private void infoInitInternal() throws OemDatabaseException {
        infoTable.setInfoItemNewTransaction(InfoKey.DATAVERSION, "DV_0.0.99");
        infoTable.setInfoItemNewTransaction(InfoKey.ADAPTIONVALUEINFO, "{}");
        infoTable.setInfoItemNewTransaction(InfoKey.LOADSPECTRUMINFO,
                "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
    }

    @GetMapping("/reset")
    public ResponseEntity<DefaultApiResult> reset() throws OemDatabaseException {
        try {
            infoTable.deleteAllNewTransaction();

            infoInitInternal();

            vehicleTable.deleteAllNewTransaction();
            telematicsDataTable.deleteAllNewTransaction();
            syncTable.reInitDefaultNewTransaction();

            return apiHelper.ok("Rawdata database cleared and reinitialized.");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @PostMapping("/vehicle/register")
    public ResponseEntity<DefaultApiResult> registerVehicle(@RequestBody @NotNull final VehicleInfo vehicle) {
        try {
            vehicleTable.registerVehicleNewTransaction(vehicle);
            return apiHelper.ok("Vehicle registered.");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }

    @PostMapping("/telematicsdata/add")
    public ResponseEntity<DefaultApiResult> addTelemeticsData(@RequestBody @NotNull final InputTelematicsData telematicsData) {
        try {
            vehicleTable.appendTelematicsDataNewTransaction(telematicsData);
            return apiHelper.ok("Telematics data added.");
        }
        catch(final OemDatabaseException exception) {
            return apiHelper.failed(exception.toString());
        }
    }
}
