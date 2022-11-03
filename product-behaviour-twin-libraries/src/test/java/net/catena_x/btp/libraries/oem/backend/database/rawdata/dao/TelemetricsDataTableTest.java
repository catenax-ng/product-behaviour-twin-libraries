package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelemetricsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;
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

@DataJpaTest
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {TelemetricsDataTable.class})
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
        "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata.model"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelemetricsDataTableTest {
    @Autowired
    private TelemetricsDataTable telemetricsDataTable;

    @Test
    void injectedComponentsAreNotNull() {
        Assert.assertNotNull(telemetricsDataTable);
    }

    @Test
    void uploadandQueryTelemetricsData() throws OemDatabaseException, InterruptedException  {

        telemetricsDataTable.uploadTelemetricsData(generateTelematricsTestData("veh1"));
        Thread.sleep(1);

        telemetricsDataTable.uploadTelemetricsData(generateTelematricsTestData("veh2"));
        telemetricsDataTable.uploadTelemetricsData(generateTelematricsTestData("veh1"));

        telemetricsDataTable.getEntityManager().clear();
        List<TelemetricsData> data = telemetricsDataTable.getAll();
        Assert.assertEquals(data.size(), 3);

        List<TelemetricsData> veh2Data = telemetricsDataTable.getByVehicleId("veh2");
        Assert.assertEquals(veh2Data.size(), 1);

        List<TelemetricsData> updatedSinceVeh2Data = telemetricsDataTable.getUpdatedSince(
                veh2Data.get(0).getCreationTimestamp());
        Assert.assertEquals(updatedSinceVeh2Data.size(), 2);
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
}