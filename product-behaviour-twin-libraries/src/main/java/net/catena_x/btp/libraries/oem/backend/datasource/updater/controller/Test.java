package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.InfoTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.VehicleTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawAdaptionValuesRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawInfoItemRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawLoadCollectivesRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawVehicleRepository;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;


@RestController
@EnableTransactionManagement
@ComponentScan(basePackages={"net.catena_x.btp.libraries.oem.backend.database.rawdata"})
public class Test {
    @Autowired
    private RawInfoItemRepository rawInfoItemRepository;

    @Autowired
    private RawVehicleRepository rawVehicleRepository;

    @Autowired
    private RawAdaptionValuesRepository rawAdaptionValuesRepository;

    @Autowired
    private RawLoadCollectivesRepository rawLoadCollectivesRepository;

    @Autowired
    private VehicleTable vehicleTable;

    @Autowired
    private InfoTable infoTable;


    @GetMapping("/api/test")
    public ResponseEntity<byte[]> damageJson() {
        try {
            vehicleTable.registerVehicle(new VehicleInfo("addd4a4545", "b5adddsd2", Instant.now() ));
            return ok("");
        }
        catch(OemDatabaseException exception) {
            return failed(exception.toString());
        }
   }

   public ResponseEntity<byte[]> init() throws OemDatabaseException {
       try {
           infoTable.setInfoItem(InfoItem.InfoKey.dataversion, "DV_0.0.99");
           infoTable.setInfoItem(InfoItem.InfoKey.adaptionvalueinfo, "{}");
           infoTable.setInfoItem(InfoItem.InfoKey.collectiveinfo, "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
           return ok("");
       }
       catch(OemDatabaseException exception) {
           return failed(exception.toString());
       }
    }

    private ResponseEntity<byte[]> ok(String info) {
        return new ResponseEntity<>( String.format( "<html><head><title>HI Backend - Test-EndPoint</title></head><body style=\"font-family:Segoe UI Light,Segoe UI,Calibri,Arial,Helvetia\"><h1>HI-BackEnd</h1><h2>Test-Endpoint</h2><p style=\"color:rgb(24,192,0);font-family:Segoe UI,Segoe UI,Calibri,Arial,Helvetia;font-style:bold\">OK %s</p></body></html>",
                info).getBytes(),
                new HttpHeaders(), HttpStatus.OK);
    }

   private ResponseEntity<byte[]> failed(String error) {
       return new ResponseEntity<>( String.format( "<html><head><title>HI Backend - Test-EndPoint</title></head><body style=\"font-family:Segoe UI Light,Segoe UI,Calibri,Arial,Helvetia\"><h1>HI-BackEnd</h1><h2>Test-Endpoint</h2><p style=\"color:rgb(192,12,0);font-family:Segoe UI,Segoe UI,Calibri,Arial,Helvetia;font-style:bold\">Failed: %s</p></body></html>",
               error).getBytes(),
               new HttpHeaders(), HttpStatus.OK);
   }
}
