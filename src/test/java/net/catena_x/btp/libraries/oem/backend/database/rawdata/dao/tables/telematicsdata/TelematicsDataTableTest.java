package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import net.catena_x.btp.libraries.bamm.common.BammStatus;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.CLSMetaData;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.LoadSpectrumType;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.sync.SyncTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsData;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsDataTable;
import net.catena_x.btp.libraries.util.threads.Threads;
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
        "net.catena_x.btp.libraries.oem.backend.database.rawdata",
        "net.catena_x.btp.libraries.oem.backend.model"})
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
    void uploadandQueryTelematicsData() throws OemDatabaseException  {

        telematicsDataTable.uploadTelematicsDataGetIdNewTransaction(generateTelematricsTestData("veh1"));
        Threads.sleepWithoutExceptions(100);

        telematicsDataTable.uploadTelematicsDataGetIdNewTransaction(generateTelematricsTestData("veh2"));
         Threads.sleepWithoutExceptions(100);

        telematicsDataTable.uploadTelematicsDataGetIdNewTransaction(generateTelematricsTestData("veh1"));

        List<TelematicsData> data = telematicsDataTable.getAllNewTransaction();
        Assertions.assertEquals(3, data.size());

        List<TelematicsData> veh2Data = telematicsDataTable.getByVehicleIdNewTransaction("veh2");
        Assertions.assertEquals(1, veh2Data.size());

        List<TelematicsData> updatedSinceVeh2Data = telematicsDataTable.getUpdatedSinceNewTransaction(
                veh2Data.get(0).getStorageTimestamp());
        Assertions.assertEquals(2, updatedSinceVeh2Data.size());
    }

    private InputTelematicsData generateTelematricsTestData(String vehicleId) {
        return new InputTelematicsData(vehicleId,
                helperGenerateLoadSpectra(), helperGenerateAdaptionValues());
    }

    private List<ClassifiedLoadSpectrum> helperGenerateLoadSpectra() {
        List<ClassifiedLoadSpectrum> list = new ArrayList<>();

        BammStatus bammStatus = new BammStatus();
        // bammStatus.setRouteDescription("Default route");
        bammStatus.setOperatingHours(210.0f);
        bammStatus.setMileage(1233L);
        bammStatus.setDate(Instant.now());

        CLSMetaData metaData = new CLSMetaData();
        metaData.setComponentDescription(LoadSpectrumType.GEAR_SET);
        metaData.setStatus(bammStatus);

        ClassifiedLoadSpectrum loadSpectrum = new ClassifiedLoadSpectrum();
        loadSpectrum.setTargetComponentID("urn:2345");
        loadSpectrum.setMetadata(metaData);

        list.add(loadSpectrum);

        metaData.setComponentDescription(LoadSpectrumType.GEAR_OIL);
        list.add(loadSpectrum);

        return list;
    }

    private List<AdaptionValues> helperGenerateAdaptionValues() {
        BammStatus bammStatus = new BammStatus();
        // bammStatus.setRouteDescription("Default route");
        bammStatus.setOperatingHours(211.0f);
        bammStatus.setMileage(1233L);
        bammStatus.setDate(Instant.now());

        AdaptionValues adaptionValues = new AdaptionValues();
        adaptionValues.setValues(new double[]{ 0.3, 12.6, 3.0, 1.1 });
        adaptionValues.setStatus(bammStatus);

        List<AdaptionValues> list = new ArrayList<>();
        list.add(adaptionValues);

        return list;
    }
}