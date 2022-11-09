package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncTable;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.TelematicsData;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;
import org.junit.jupiter.api.Assertions;
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

@DataJpaTest(properties = {"rawdatadb.hibernate.hbm2ddl.auto=create-drop"})
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {InfoTable.class})
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
        "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelematicsDataTableTest {
    @Autowired private TelematicsDataTable telematicsDataTable;
    @Autowired private SyncTable syncTable;

    @BeforeEach
    void setupTest() throws OemDatabaseException {
        telematicsDataTable.deleteAllNewTransaction();
        syncTable.clearNewTransaction();
        syncTable.initDefaultNewTransaction();
    }

    @Test
    void injectedComponentsAreNotNull() {
        Assertions.assertNotNull(telematicsDataTable);
        Assertions.assertNotNull(syncTable);
    }

    @Test
    void uploadandQueryTelematicsData() throws OemDatabaseException, InterruptedException  {

        telematicsDataTable.uploadTelematicsDataGetIdNewTransaction(generateTelematricsTestData("veh1"));
        Thread.sleep(100);

        telematicsDataTable.uploadTelematicsDataGetIdNewTransaction(generateTelematricsTestData("veh2"));
        Thread.sleep(100);

        telematicsDataTable.uploadTelematicsDataGetIdNewTransaction(generateTelematricsTestData("veh1"));

        List<TelematicsData> data = telematicsDataTable.getAllNewTransaction();
        Assertions.assertEquals(data.size(), 3);

        List<TelematicsData> veh2Data = telematicsDataTable.getByVehicleIdNewTransaction("veh2");
        Assertions.assertEquals(veh2Data.size(), 1);

        List<TelematicsData> updatedSinceVeh2Data = telematicsDataTable.getUpdatedSinceNewTransaction(
                veh2Data.get(0).getStorageTimestamp());
        Assertions.assertEquals(updatedSinceVeh2Data.size(), 2);
    }

    private InputTelematicsData generateTelematricsTestData(String vehicleId) {
        return new InputTelematicsData( helperGenerateState(vehicleId),
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