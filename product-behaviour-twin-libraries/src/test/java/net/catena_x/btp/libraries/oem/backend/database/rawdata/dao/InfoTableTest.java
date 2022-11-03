package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {InfoTable.class})
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
        "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata.model"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InfoTableTest {
    @Autowired private InfoTable infoTable;

    @Test
    void injectedComponentsAreNotNull(){
        Assert.assertNotNull(infoTable);
    }

    @Test
    void checkCurrentDatabaseTimestamp() throws OemDatabaseException, InterruptedException {
        insertTestData();
        long duration_ms = queryDatabaseTimestampsDuration();
        assertTrue(duration_ms >= 100 && duration_ms <= 5000);
    }

    @Test
    void insertAndReadInfoItem() throws OemDatabaseException, Exception {
        checkDataversionNotPresent();
        infoTable.setInfoItem(InfoItem.InfoKey.dataversion, "TestVersion");

        infoTable.getEntityManager().clear();
        InfoItem infoItem = infoTable.getInfoItem(InfoItem.InfoKey.dataversion);

        assertTrue(infoItem.getKey() == InfoItem.InfoKey.dataversion && infoItem.getValue() == "TestVersion" );
        assertTrue(infoTable.getInfoValue(InfoItem.InfoKey.dataversion) == "TestVersion" );
    }

    private void insertTestData() throws OemDatabaseException {
        infoTable.setInfoItem(InfoItem.InfoKey.dataversion, "DV_0.0.99");
        infoTable.setInfoItem(InfoItem.InfoKey.adaptionvalueinfo, "{}");
        infoTable.setInfoItem(InfoItem.InfoKey.collectiveinfo, "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
    }

    private long queryDatabaseTimestampsDuration() throws OemDatabaseException, InterruptedException {
        Instant currentTimestamp1 = infoTable.getCurrentDatabaseTimestamp();
        Thread.sleep(200);
        Instant currentTimestamp2 = infoTable.getCurrentDatabaseTimestamp();

        return Duration.between(currentTimestamp1, currentTimestamp2).toMillis();
    }

    private void checkDataversionNotPresent() throws Exception {
        assertThrows(OemDatabaseException.class, () -> {
            InfoItem item = infoTable.getInfoItem(InfoItem.InfoKey.dataversion);
        });
    }
}
