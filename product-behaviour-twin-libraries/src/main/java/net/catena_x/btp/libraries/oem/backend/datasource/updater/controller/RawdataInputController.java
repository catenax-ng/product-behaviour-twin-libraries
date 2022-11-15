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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableTransactionManagement
public class RawdataInputController {
    @Autowired private InfoTable infoTable;
    @Autowired private VehicleTable vehicleTable;
    @Autowired private TelematicsDataTable telematicsDataTable;
    @Autowired private SyncTable syncTable;

    @PostMapping("/api/rawdata/info/set")
    public ResponseEntity<byte[]> infoSet(@RequestBody @NotNull final InputInfo item) {

        InfoKey key;

        try {
            key = InfoKey.valueOf(item.key());
        }
        catch(Exception exception) {
            return failed(exception.toString());
        }

        try {
            infoTable.setInfoItemNewTransaction(key, item.value());
            return ok(item.key() + " set to \"" + item.value() + "\"");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    @GetMapping("/api/rawdata/info/get/{key}")
    public ResponseEntity<byte[]> infoGet(@PathVariable @NotNull final InfoKey key) {
        try {
            final String value = infoTable.getInfoValueNewTransaction(key);

            final HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/plain");
            headers.add("Content-Disposition", "inline");

            return okBody(headers, value);
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    @PostMapping("/api/rawdata/info/init")
    public ResponseEntity<byte[]> infoInit() {
        try {
            infoTable.setInfoItemNewTransaction(InfoKey.DATAVERSION, "DV_0.0.99");
            infoTable.setInfoItemNewTransaction(InfoKey.ADAPTIONVALUEINFO, "{}");
            infoTable.setInfoItemNewTransaction(InfoKey.LOADSPECTRUMINFO,
                    "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
            return ok("");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    @PostMapping("/api/rawdata/reset")
    public ResponseEntity<byte[]> reset() throws OemDatabaseException {
        try {
            infoInit();
            vehicleTable.deleteAllNewTransaction();
            telematicsDataTable.deleteAllNewTransaction();
            syncTable.reInitDefaultNewTransaction();
            return ok("Rawdata database cleared and reinitialized.");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    @PostMapping("/api/rawdata/vehicle/register")
    public ResponseEntity<byte[]> registerVehicle(@RequestBody @NotNull final VehicleInfo vehicle) {
        try {
            vehicleTable.registerVehicleNewTransaction(vehicle);
            return ok("Vehicle registered.");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    @PostMapping("/api/rawdata/telematicsdata/add")
    public ResponseEntity<byte[]> addTelemeticsData(@RequestBody @NotNull final InputTelematicsData telematicsData){
        try {
            vehicleTable.appendTelematicsDataNewTransaction(telematicsData);
            return ok("Telematics data added.");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    private ResponseEntity<byte[]> ok(@NotNull final String info) {
        return new ResponseEntity<>( String.format( "{\n  \"result\" : \"ok\",\n  \"info\" : \"%s\"\n}",
                info).getBytes(),
                new HttpHeaders(), HttpStatus.OK);
    }

    private ResponseEntity<byte[]> okBody(@Nullable final HttpHeaders headers, @NotNull final String value) {
        return new ResponseEntity<>( value.getBytes(),
                (headers != null)? headers : new HttpHeaders(), HttpStatus.OK);
    }

    private ResponseEntity<byte[]> failed(@NotNull final String error) {
        return new ResponseEntity<>( String.format( "{\n  \"result\" : \"error\",\n  \"errorText\" : \"%s\"\n}",
                error).getBytes(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
