package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.OemDataUpdaterApplication;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.OemDataUpdaterApplicationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;


import javax.persistence.EntityManager;
import javax.validation.constraints.AssertTrue;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {OemDataUpdaterApplication.class})
@EnableAutoConfiguration
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
                               "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata.model"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleTableTest {

    @Autowired private EntityManager entityManager;
    @Autowired private VehicleTable vehicleTable;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(entityManager).isNotNull();
        assertThat(vehicleTable).isNotNull();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registerVehicle() throws OemDatabaseException {
        VehicleInfo newVehilce = new VehicleInfo("veh1", "van1",
                Instant.parse("2020-10-27T14:35:24.00Z"));
        vehicleTable.registerVehicle(newVehilce);

        List<Vehicle> vehicles = vehicleTable.getVehicles();

        assertEquals(vehicles.size(), 1);
    }

    private void Assert() {
    }

    @Test
    void getVehiclesUpdatedSince() {
    }

/*
    List<Vehicle> vehicles = vehicleTable.getVehiclesProducedBetween(Instant.parse("2020-12-27T14:35:24.00Z"),
            Instant.parse("2020-11-27T14:35:24.00Z"));
*/
}
