package net.catena_x.btp.sedc.apps.oem.backend.controller;

import net.catena_x.btp.libraries.edc.api.EdcApi;
import net.catena_x.btp.sedc.apps.oem.backend.OemDataCollectorApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@AutoConfigureTestEntityManager
@SpringBootTest
@ActiveProfiles("oemdatacollector")
@ContextConfiguration(classes = {OemDataCollectorApplication.class})
class OemDataCollectorTestControllerTest {
    @Autowired EdcApi edcApi;
    @Autowired
    OemDataCollectorController controller;

    @Test
    void catalog() {
        controller.catalog();
    }

    @Test
    void test() {
        controller.test();
    }
}