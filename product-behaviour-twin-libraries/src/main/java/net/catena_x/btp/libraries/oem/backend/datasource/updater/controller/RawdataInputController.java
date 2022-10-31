package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.InfoTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.VehicleTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;


@RestController
@EnableTransactionManagement
public class RawdataInputController {
    @Autowired
    private InfoTable infoTable;

    @Autowired
    private VehicleTable vehicleTable;

    @PostMapping("/api/rawdata/info/set")
    public ResponseEntity<byte[]> infoSet(@RequestBody InfoItem item) {
        try {
            infoTable.setInfoItem(item.getKey(), item.getValue());
            return ok("");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    @GetMapping("/api/rawdata/info/get/{key}")
    public ResponseEntity<byte[]> infoGet(@PathVariable InfoItem.InfoKey key) {
        try {
            String value = infoTable.getInfoValue(key);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/plain");
            headers.add("Content-Disposition", "inline");

            return new ResponseEntity<>( value.getBytes(), headers, HttpStatus.OK);
        }
        catch(OemDatabaseException exception) {
            return new ResponseEntity<>( String.format( "<html><head><title>HI Backend - Test-EndPoint</title></head><body style=\"font-family:Segoe UI Light,Segoe UI,Calibri,Arial,Helvetia\"><h1>HI-BackEnd</h1><h2>Test-Endpoint</h2><p style=\"color:rgb(192,12,0);font-family:Segoe UI,Segoe UI,Calibri,Arial,Helvetia;font-style:bold\">Failed: %s</p></body></html>",
                    exception.toString()).getBytes(),
                    new HttpHeaders(), HttpStatus.OK);
        }
    }

    @PostMapping("/api/rawdata/info/init")
    public ResponseEntity<byte[]> infoInit() throws OemDatabaseException {
        try {
            infoTable.setInfoItem(InfoItem.InfoKey.dataversion, "DV_0.0.99");
            infoTable.setInfoItem(InfoItem.InfoKey.adaptionvalueinfo, "{}");
            infoTable.setInfoItem(InfoItem.InfoKey.collectiveinfo, "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
            infoTable.setInfoItem(InfoItem.InfoKey.lastcalculationstarted, null);
            return ok("");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    @PostMapping("/api/rawdata/vehicle/register")
    public ResponseEntity<byte[]> registerVehicle(@RequestBody VehicleInfo vehicle) throws OemDatabaseException {
        try {
            vehicleTable.registerVehicle(vehicle);
            return ok("");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
    }

    private ResponseEntity<byte[]> ok(String info) {
        return new ResponseEntity<>( String.format( "{\n  \"result\" : \"ok\",\n  \"info\" : \"%s\"\n}",
                info).getBytes(),
                new HttpHeaders(), HttpStatus.OK);
    }

    private ResponseEntity<byte[]> failed(String error) {
        return new ResponseEntity<>( String.format( "{\n  \"result\" : \"error\",\n  \"errorText\" : \"%s\"\n}",
                error).getBytes(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
