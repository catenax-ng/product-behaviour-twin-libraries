package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.AdaptionValues;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputAdaptionValues;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;
import net.catena_x.btp.libraries.oem.backend.datasource.updater.OemDataUpdaterApplication;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles(profiles = "dataupdater")
@ContextConfiguration(classes = {OemDataUpdaterApplication.class})
@EnableAutoConfiguration
@TestPropertySource(locations = {"classpath:test-rawdatadb.properties"})
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
        "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata.model"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdaptionValuesTableTest {
    @Autowired
    private AdaptionValuesTable adaptionValuesTable;

    @Test
    void uploadAdaptionValues() throws OemDatabaseException {
        ArrayList<double[]> valueList = new ArrayList<double[]>();
        valueList.add(new double[]{0.1, 0.2, 0.3});
        valueList.add(new double[]{1.1, 1.2});

        InputAdaptionValues newValues = new InputAdaptionValues(new VehicleState("veh1",
                Instant.parse("2022-10-27T14:35:24.00Z"), 12345.6f, 766254352), valueList);

        adaptionValuesTable.uploadAdaptionValues(newValues);

        List<AdaptionValues> adaptionValues = adaptionValuesTable.getAdaptionValues();

        assertEquals(adaptionValues.size(), 1);
    }
}