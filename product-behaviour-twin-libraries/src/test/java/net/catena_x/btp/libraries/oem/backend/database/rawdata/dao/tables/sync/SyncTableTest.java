package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dto.Sync;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {"rawdatadb.hibernate.hbm2ddl.auto=create-drop"})
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {SyncTable.class})
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
        "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SyncTableTest {
    @Autowired private SyncTable syncTable;

    @Test
    void injectedComponentsAreNotNull(){
        assertNotNull(syncTable);
    }

    @Test
    void insertSetCurrent() throws Exception {
        syncTable.initDefaultNewTransaction();
        Sync prevSyncCounter = syncTable.getCurrentDefaultNewTransaction();
        Sync newSyncCounter = syncTable.setCurrentDefaultNewTransaction();
        Sync updatedSyncCounter = syncTable.getCurrentDefaultNewTransaction();

        assertTrue(prevSyncCounter.getSyncCounter() < newSyncCounter.getSyncCounter());
        assertTrue(prevSyncCounter.getQueryTimestamp().isBefore(newSyncCounter.getQueryTimestamp()));
        assertEquals(updatedSyncCounter.getSyncCounter(), newSyncCounter.getSyncCounter());
        assertEquals(updatedSyncCounter.getQueryTimestamp(), newSyncCounter.getQueryTimestamp());
    }
}