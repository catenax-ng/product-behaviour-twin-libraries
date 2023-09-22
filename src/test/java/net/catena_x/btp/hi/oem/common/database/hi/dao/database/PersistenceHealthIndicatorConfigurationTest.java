package net.catena_x.btp.hi.oem.common.database.hi.dao.database;

import net.catena_x.btp.hi.oem.common.database.hi.config.PersistenceHealthIndicatorConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {PersistenceHealthIndicatorConfiguration.class})
@TestPropertySource(locations = {"classpath:test-healthindicatordb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.hi.oem.common.database.hi.dao.database"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenceHealthIndicatorConfigurationTest {

    @Test
    void injectedComponentsAreNotNull() {
    }
}
