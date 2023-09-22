package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.testdata.TestDataReader;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@ContextConfiguration(classes = {TestdataConfig.class})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.provider",
        "net.catena_x.btp.libraries.oem.backend.datasource.model",
        "net.catena_x.btp.libraries.oem.backend.datasource.testdata",
        "net.catena_x.btp.libraries.bamm.testdata"})
@TestPropertySource(locations = {"classpath:test-dataprovider.properties"})
class TestDataConfigTest {
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;
    @Autowired TestDataReader testDataReader;

    @Test
    void rulTestDataFiles() throws Exception {
        final TestData testdata = testDataReader.loadFromConfigFile(
                Path.of("C:\\pc\\CatenaX\\RuL-KIT\\Testdaten\\EP_E2E\\input\\TestDataConfig.json"));


    }
}