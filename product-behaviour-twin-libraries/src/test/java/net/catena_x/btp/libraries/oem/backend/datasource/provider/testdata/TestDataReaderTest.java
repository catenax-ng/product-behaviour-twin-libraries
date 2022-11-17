package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata;

import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataReader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
@TestPropertySource(locations = {"classpath:test-dataprovider.properties"})
class TestDataReaderTest {
    @Autowired TestDataReader testDataReader;

    @Value("${services.dataprovider.test.testdata.file}")
    private String testDataFile;

    @Test
    void injectedComponentsAreNotNull() {
        Assertions.assertNotNull(testDataReader);
    }

    @Test
    void testLoadFromFile() throws DataProviderException {
        TestData testData = testDataReader.loadFromFile(Path.of(testDataFile));

        Assertions.assertEquals(1000, testData.getDigitalTwins().size());
    }
}
