package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@DataJpaTest
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {PersistenceRawdataConfiguration.class})
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenceRawdataConfigurationTest {

    @Autowired private LocalContainerEntityManagerFactoryBean healthindicatorEntityManager;
    @Autowired private DataSource healthindicatorDataSource;
    @Autowired private PlatformTransactionManager healthindicatorTransactionManager;

    @Test
    void injectedComponentsAreNotNull() {
        Assertions.assertNotNull(healthindicatorEntityManager);
        Assertions.assertNotNull(healthindicatorDataSource);
        Assertions.assertNotNull(healthindicatorTransactionManager);
    }
}