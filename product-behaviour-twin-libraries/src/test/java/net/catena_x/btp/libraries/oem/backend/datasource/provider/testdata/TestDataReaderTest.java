package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata;

import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.CatenaXIdToDigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.DigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.VehilceDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.UncheckedDataProviderException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;

@SpringBootTest
@ActiveProfiles(profiles = "dataprovider")
@ContextConfiguration(classes = {TestDataReader.class})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.provider",
        "net.catena_x.btp.libraries.oem.backend.datasource.model"})
class TestDataReaderTest {
    @Autowired TestDataReader testDataReader;
    @Autowired VehilceDataLoader vehilceDataLoader;

    @Test
    void injectedComponentsAreNotNull(){
        Assertions.assertNotNull(testDataReader);
    }

    @Test
    void testLoadFromFile() throws IOException, DataProviderException {
        testDataReader.loadFromFile(
                Path.of("C:\\pc\\CatenaX\\Catena-X_Vehicle_Health_App\\DigitalTwins_CX_RuL_Testdata_v0.0.2.json"));

        final HashMap<String, DigitalTwin> vehicles = testDataReader.getDigitalTwinsVehicles();

        vehicles.entrySet().stream().forEachOrdered((vehicleEntry) -> {
            final DigitalTwin vehicle = vehicleEntry.getValue();

            CatenaXIdToDigitalTwinType idToType = (String catenaXId)-> {
                return testDataReader.catenaXIdToType(catenaXId);
            };

            try {
                String catenaXId = vehilceDataLoader.getCatenaXId(vehicle);
                String van = vehilceDataLoader.getVan(vehicle);
                String gearboxId = vehilceDataLoader.getGearboxID(vehicle, idToType);
                Instant productionDate = vehilceDataLoader.getProductionDate(vehicle);

                registerVehicle(new VehicleInfo(catenaXId, van, gearboxId, productionDate));
            } catch (DataProviderException exception) {
                throw new UncheckedDataProviderException(exception);
            }
        });
    }

    private void registerVehicle(@NotNull final VehicleInfo vehicleInfo) {
        // TODO: registerVehicle at DataUpdater-Endpoint

    }

    /*
     * TODO: APP:
     *
     * Transfer Test-Code to APP-Code.
     *
     * <Run per Endpoint>
     * for each vehicle <stream().foreach()>:
     *   Update load collectives and adaption values
     * (av must be generated, loadcollectives factorized)
     *
     * Init-Endpoint required.
     *
     */

}
