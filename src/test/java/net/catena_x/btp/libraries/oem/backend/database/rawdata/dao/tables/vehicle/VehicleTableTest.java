package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle;

import net.catena_x.btp.libraries.bamm.common.BammStatus;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.CLSMetaData;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.LoadSpectrumType;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.sync.SyncTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.telematicsdata.TelematicsDataTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
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
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(properties = {"rawdatadb.hibernate.hbm2ddl.auto=create-drop"})
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {InfoTable.class})
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
        "net.catena_x.btp.libraries.oem.backend.database.rawdata",
        "net.catena_x.btp.libraries.oem.backend.model"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleTableTest {
    @Autowired private VehicleTable vehicleTable;
    @Autowired private TelematicsDataTable telematicsDataTable;
    @Autowired private SyncTable syncTable;

    @BeforeEach
    void setupTest() throws OemDatabaseException {
        vehicleTable.deleteAllNewTransaction();
        telematicsDataTable.deleteAllNewTransaction();
        syncTable.reInitDefaultNewTransaction();
    }

    @Test
    void injectedComponentsAreNotNull() {
        Assertions.assertNotNull(vehicleTable);
        Assertions.assertNotNull(telematicsDataTable);
    }

    @Test
    void registerVehicles() throws OemDatabaseException {
        register3TestVehicles();

        final List<Vehicle> vehicles = vehicleTable.getAllNewTransaction();

        Assertions.assertEquals(3, vehicles.size());
        Assertions.assertNotNull(vehicles.get(0).getUpdateTimestamp());

        assertThrows(OemDatabaseException.class, () -> vehicleTable.registerVehicleNewTransaction(
                new VehicleInfo("veh1", "van4", "gear4",
                Instant.parse("2022-10-27T14:35:24.00Z"))));

        assertThrows(OemDatabaseException.class, () -> vehicleTable.registerVehicleNewTransaction(
                new VehicleInfo("veh4", "van1", "gear4",
                Instant.parse("2019-10-27T14:35:24.00Z"))));

        vehicleTable.registerVehicleNewTransaction(new VehicleInfo("veh4", "van4", "gear4",
                Instant.parse("2019-10-27T14:35:24.00Z")));
    }

    @Test
    void checkTelematicsRelation() throws OemDatabaseException {
        register3TestVehicles();

        vehicleTable.appendTelematicsDataNewTransaction(generateTelematricsTestData("veh1"));
        Assertions.assertEquals(1, telematicsDataTable.getAllNewTransaction().size());

        vehicleTable.appendTelematicsDataNewTransaction(generateTelematricsTestData("veh1"));
        Assertions.assertEquals(2, telematicsDataTable.getSyncCounterSinceNewTransaction(0).size());

        Assertions.assertEquals(1, vehicleTable.getSyncCounterSinceWithTelematicsDataNewTransaction(2).size());
        Assertions.assertEquals(0, vehicleTable.getSyncCounterSinceNewTransaction(5).size());

        vehicleTable.appendTelematicsDataNewTransaction(generateTelematricsTestData("veh3"));
        Assertions.assertEquals(1, vehicleTable.getSyncCounterSinceWithTelematicsDataNewTransaction(6).size());

        final List<Vehicle> vehicles = vehicleTable.getSyncCounterSinceWithTelematicsDataNewTransaction(1);
        Assertions.assertEquals(2, vehicles.size());

        final Consumer<Vehicle> checkRelation = (vehicle) -> {
            Assertions.assertEquals(vehicle.getNewestTelematicsData().getSyncCounter() + 1,
                    vehicle.getSyncCounter());
        };

        checkRelation.accept(vehicles.get(0));
        checkRelation.accept(vehicles.get(0));
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

    private void register1TestVehicles() throws OemDatabaseException {
        vehicleTable.registerVehicleNewTransaction(new VehicleInfo("veh1", "van1", "gear1",
                Instant.parse("2020-10-27T14:35:24.00Z")));
    }

    private void register3TestVehicles() throws OemDatabaseException {
        vehicleTable.registerVehicleNewTransaction(new VehicleInfo("veh1", "van1", "gear1",
                Instant.parse("2020-10-27T14:35:24.00Z")));
        vehicleTable.registerVehicleNewTransaction(new VehicleInfo("veh2", "van2", "gear2",
                Instant.parse("2019-10-27T14:35:24.00Z")));
        vehicleTable.registerVehicleNewTransaction(new VehicleInfo("veh3", "van3", "gear3",
                Instant.parse("2021-10-27T14:35:24.00Z")));
    }
}
