package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem;

import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
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

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {"rawdatadb.hibernate.hbm2ddl.auto=create-drop"})
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {InfoTable.class})
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
        "net.catena_x.btp.libraries.oem.backend.database.rawdata",
        "net.catena_x.btp.libraries.oem.backend.model"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InfoTableTest {
    @Autowired private InfoTable infoTable;

    @BeforeEach
    void setupTest() throws OemDatabaseException {
        infoTable.deleteAllNewTransaction();
    }

    @Test
    void injectedComponentsAreNotNull(){
        Assertions.assertNotNull(infoTable);
    }

    @Test
    void checkCurrentDatabaseTimestamp() throws OemDatabaseException, InterruptedException {
        insertTestData();
        long duration_ms = queryDatabaseTimestampsDuration();
        assertTrue(duration_ms >= 100 && duration_ms <= 5000);
    }

    @Test
    void insertAndReadInfoItem() throws Exception {
        checkDataversionNotPresent();
        infoTable.setInfoItemNewTransaction(InfoKey.DATAVERSION, "TestVersion");

        InfoItem infoItemDAO = infoTable.getInfoItemNewTransaction(InfoKey.DATAVERSION);

        assertTrue(infoItemDAO.getKey() == InfoKey.DATAVERSION
                && infoItemDAO.getValue().equals("TestVersion"));
        assertEquals("TestVersion", infoTable.getInfoValueNewTransaction(InfoKey.DATAVERSION));
    }

    private void insertTestData() throws OemDatabaseException {
        infoTable.setInfoItemNewTransaction(InfoKey.DATAVERSION, "DV_0.0.99");
        infoTable.setInfoItemNewTransaction(InfoKey.ADAPTIONVALUEINFO, "{}");
        infoTable.setInfoItemNewTransaction(InfoKey.COLLECTIVEINFO, "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
    }

    private long queryDatabaseTimestampsDuration() throws OemDatabaseException, InterruptedException {
        Instant currentTimestamp1 = infoTable.getCurrentDatabaseTimestampNewTransaction();
        Thread.sleep(200);
        Instant currentTimestamp2 = infoTable.getCurrentDatabaseTimestampNewTransaction();

        return Duration.between(currentTimestamp1, currentTimestamp2).toMillis();
    }

    private void checkDataversionNotPresent() throws Exception {
        InfoItem item = infoTable.getInfoItemNewTransaction(InfoKey.DATAVERSION);
        Assertions.assertNull(item);
    }
}
