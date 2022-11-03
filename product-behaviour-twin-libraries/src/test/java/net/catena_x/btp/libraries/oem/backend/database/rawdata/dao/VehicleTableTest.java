package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelemetricsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.junit.Assert;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void injectedComponentsAreNotNull() {
        Assert.assertNotNull(vehicleTable);
        Assert.assertNotNull(telemetricsDataTable);
    }

    @Test
    void registerVehicles() throws OemDatabaseException {
        register3TestVehicles();

        List<Vehicle> vehicles = vehicleTable.getAll();

        Assert.assertEquals(vehicles.size(), 3);
        Assert.assertNotNull(vehicles.get(0).getUpdateTimestamp());

        assertThrows(OemDatabaseException.class, () -> {
            vehicleTable.registerVehicle(new VehicleInfo("veh1", "van4", "gear4",
                    Instant.parse("2022-10-27T14:35:24.00Z")));
        });

        assertThrows(OemDatabaseException.class, () -> {
            vehicleTable.registerVehicle(new VehicleInfo("veh4", "van1", "gear4",
                    Instant.parse("2019-10-27T14:35:24.00Z")));
        });

        vehicleTable.registerVehicle(new VehicleInfo("veh4", "van4", "gear4",
                Instant.parse("2019-10-27T14:35:24.00Z")));
    }

    @Test
    void checkTelematicsRelation() throws OemDatabaseException {
        register1TestVehicles();

        vehicleTable.appendTelematicsDataById("veh1", generateTelematricsTestData("veh1"));
        List<TelemetricsData> telemetricsData1 = telemetricsDataTable.getAll();
        Assert.assertEquals(telemetricsData1.size(), 1);

        //Not working!!
        Vehicle veh_van = vehicleTable.findByVanMustExist("van1");

        vehicleTable.appendTelematicsDataByVan("van1", generateTelematricsTestData("veh1"));

        List<TelemetricsData> telemetricsData2 = telemetricsDataTable.getAllOrderByStorageTimestamp();
        Assert.assertEquals(telemetricsData2.size(), 2);

        /* Check relation */



    }

    @Test
    void getVehiclesUpdatedSince() {
    }

    @Test
    void getVehiclesByProductionDate() {
    /*
    List<Vehicle> vehicles = vehicleTable.getVehiclesProducedBetween(Instant.parse("2020-12-27T14:35:24.00Z"),
            Instant.parse("2020-11-27T14:35:24.00Z"));
*/
    }

    private InputTelemetricsData generateTelematricsTestData(String vehicleId) {
        return new InputTelemetricsData( helperGenerateState(vehicleId),
                helperGenerateLoadCollectives(), helperGenerateAdaptionValues() );
    }

    private VehicleState helperGenerateState(String vehicleId) {
        return new VehicleState(vehicleId, Instant.now(),
                12345.6f, 12345678 );
    }

    private List<String> helperGenerateLoadCollectives() {
        List<String> list = new ArrayList<>();
        list.add("{ \"TEST_COLLECTIVE_JSON\": \"TEST\" }");
        return list;
    }

    private List<double[]> helperGenerateAdaptionValues() {
        List<double[]> list = new ArrayList<>();
        list.add(new double[]{0.4, 0.7, 1.8});
        return list;
    }

    private void register1TestVehicles() throws OemDatabaseException {
        vehicleTable.registerVehicle(new VehicleInfo("veh1", "van1", "gear1",
                Instant.parse("2020-10-27T14:35:24.00Z")));
    }

    private void register3TestVehicles() throws OemDatabaseException {
        vehicleTable.registerVehicle(new VehicleInfo("veh1", "van1", "gear1",
                Instant.parse("2020-10-27T14:35:24.00Z")));
        vehicleTable.registerVehicle(new VehicleInfo("veh2", "van2", "gear2",
                Instant.parse("2019-10-27T14:35:24.00Z")));
        vehicleTable.registerVehicle(new VehicleInfo("veh3", "van3", "gear3",
                Instant.parse("2021-10-27T14:35:24.00Z")));
    }
}
