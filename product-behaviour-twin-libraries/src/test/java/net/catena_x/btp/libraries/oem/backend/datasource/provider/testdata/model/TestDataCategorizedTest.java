package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.model;

import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.TestDataReader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.DigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Path;

@SpringBootTest
@ActiveProfiles(profiles = "dataprovider")
@ContextConfiguration(classes = {TestDataReader.class})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.provider",
        "net.catena_x.btp.libraries.oem.backend.datasource.model",
        "net.catena_x.btp.libraries.bamm.testdata"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = {"classpath:test-dataprovider.properties"})
class TestDataCategorizedTest {
    @Autowired TestDataReader testDataReader;
    @Autowired TestDataCategorized testDataCategorized;

    @Value("${services.dataprovider.test.testdata.file}")
    private String testDataFile;

    TestData testData = null;

    @BeforeAll
    void init() throws DataProviderException {
        testData = testDataReader.loadFromFile(
                Path.of(testDataFile));

        Assertions.assertEquals(testData.getDigitalTwins().size(), 1000);

        testDataCategorized.initFromTestData(testData);
    }

    @Test
    void injectedComponentsAreNotNull() {
        Assertions.assertNotNull(testDataReader);
        Assertions.assertNotNull(testDataCategorized);
    }

    @Test
    void initFromTestData() throws DataProviderException {
        Assertions.assertNotNull(testDataCategorized.getDigitalTwinsVehicles());
        Assertions.assertNotNull(testDataCategorized.getDigitalTwinsGearboxes());
    }

    @Test
    void catenaXIdToType() throws DataProviderException {
        testDataCategorized.getDigitalTwinsVehicles().forEach((id, twin) -> {
            Assertions.assertEquals(testDataCategorized.catenaXIdToType(id), DigitalTwinType.VEHICLE);
        });

        testDataCategorized.getDigitalTwinsGearboxes().forEach((id, twin) -> {
            Assertions.assertEquals(testDataCategorized.catenaXIdToType(id), DigitalTwinType.GEARBOX);
        });
    }

    @Test
    void getDigitalTwinsVehicles() throws DataProviderException {
        Assertions.assertEquals(testDataCategorized.getDigitalTwinsVehicles().size(), 500);
    }

    @Test
    void getDigitalTwinsGearboxes() throws DataProviderException {
        Assertions.assertEquals(testDataCategorized.getDigitalTwinsVehicles().size(), 500);
    }
}