package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelemetricsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {VehicleTable.class})
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
                               "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata.model"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleTableTest {
    @Autowired private VehicleTable vehicleTable;
    @Autowired private TelemetricsDataTable telemetricsDataTable;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(vehicleTable).isNotNull();
    }

    @Test
    void registerVehicle() throws OemDatabaseException {
        VehicleInfo newVehilce = new VehicleInfo("veh1", "van1",
                Instant.parse("2020-10-27T14:35:24.00Z"));
        vehicleTable.registerVehicle(newVehilce);

        List<Vehicle> vehicles = vehicleTable.getAll();

        /*
        Instant now = Instant.now();

        try {
            Thread.sleep(1000);
        }
        catch(Exception exception) {
        }

        vehicles.get(0).setVan("BLABLA");
        vehicleTable.flushAndClearCache();

        List<Vehicle> vehicles2 = vehicleTable.getAll();

        assertEquals(vehicles.size(), 1);

*/
    }

    @Test
    void checkTelematicsRelation() throws OemDatabaseException {
        VehicleInfo newVehilce = new VehicleInfo("veh1", "van1",
                Instant.parse("2020-10-27T14:35:24.00Z"));
        vehicleTable.registerVehicle(newVehilce);

        InputTelemetricsData newData = new InputTelemetricsData( helperGenerateState("veh1"),
                helperGenerateLoadCollectives(), helperGenerateAdaptionValues() );

        vehicleTable.appendTelematicsDataById("veh1", newData);
        vehicleTable.flushAndClearCache();

        List<Vehicle> vehicles1 = vehicleTable.getAll();
        List<TelemetricsData> telemetricsData1 = telemetricsDataTable.getAll();

        newData = new InputTelemetricsData( helperGenerateState("veh1"),
                helperGenerateLoadCollectives(), helperGenerateAdaptionValues() );
        vehicleTable.appendTelematicsDataById("veh1", newData);
        vehicleTable.flushAndClearCache();

        List<Vehicle> vehicles2 = vehicleTable.getAll();
        List<TelemetricsData> telemetricsData2 = telemetricsDataTable.getAll();
    }

    VehicleState helperGenerateState(String id) {
        return new VehicleState(id, Instant.now(), 12345.6f, 12345678 );
    }

    List<String> helperGenerateLoadCollectives() {
        List<String> list = new ArrayList<>();
        list.add("{ \"TEST_COLLECTIVE_JSON\": \"TEST\" }");
        return list;
    }

    List<double[]> helperGenerateAdaptionValues() {
        List<double[]> list = new ArrayList<>();
        list.add(new double[]{0.4, 0.7, 1.8});
        return list;
    }

    @Test
    void getVehiclesUpdatedSince() {
    }

/*
    List<Vehicle> vehicles = vehicleTable.getVehiclesProducedBetween(Instant.parse("2020-12-27T14:35:24.00Z"),
            Instant.parse("2020-11-27T14:35:24.00Z"));
*/
}
