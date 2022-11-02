package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.OemDataUpdaterApplication;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {OemDataUpdaterApplication.class})
@EnableAutoConfiguration
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
        "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata.model"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InfoTableTest {
    @Autowired private InfoTable infoTable;

    @BeforeEach
    void setUp() throws OemDatabaseException {
        infoTable.setInfoItem(InfoItem.InfoKey.dataversion, "DV_0.0.99");
        infoTable.setInfoItem(InfoItem.InfoKey.adaptionvalueinfo, "{}");
        infoTable.setInfoItem(InfoItem.InfoKey.collectiveinfo, "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
    }

    @Test
    void checkCurrentDatabaseTimestamp() throws OemDatabaseException, InterruptedException {
        Instant currentTimestamp1 = infoTable.getCurrentDatabaseTimestamp();
        Thread.sleep(200);
        Instant currentTimestamp2 = infoTable.getCurrentDatabaseTimestamp();

        long duration_ms = Duration.between(currentTimestamp1, currentTimestamp2).toMillis();

        Assert.assertTrue(String.format("Duration %d between database queries is invalid!", duration_ms ),
                duration_ms >= 100 && duration_ms <= 5000);
    }
}
