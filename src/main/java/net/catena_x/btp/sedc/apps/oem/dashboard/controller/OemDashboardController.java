package net.catena_x.btp.sedc.apps.oem.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.apps.oem.dashboard.api.PeakLoadRingBufferElementApiDAO;
import net.catena_x.btp.sedc.apps.oem.dashboard.api.PeakLoadRingBufferResultApiDAO;
import net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer.PeakLoadRingBufferElement;
import net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer.PeakLoadRingBufferTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class OemDashboardController {
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;
    @Autowired private PeakLoadRingBufferTable ringBufferTable;
    @Autowired private ApiHelper apiHelper;
    @Autowired PeakLoadRingBufferTable peakLoadRingBufferTable;

    private final Logger logger = LoggerFactory.getLogger(OemDashboardController.class);

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeakLoadRingBufferResultApiDAO> getCurrent() {
        final PeakLoadRingBufferResultApiDAO result = new PeakLoadRingBufferResultApiDAO();

        try {
            final List<PeakLoadRingBufferElement> list = peakLoadRingBufferTable.getAllSortByTimestampNewTransaction();
            result.setRingbuffer(new ArrayList<>());

            if(list != null) {
                for (final PeakLoadRingBufferElement element : list) {
                    final PeakLoadRingBufferElementApiDAO elementDao = new PeakLoadRingBufferElementApiDAO();
                    elementDao.setId(element.getId());
                    elementDao.setTimestamp(element.getTimestamp());
                    elementDao.setRawValues(element.getRawValues());
                    elementDao.setResult(element.getResult());
                    result.getRingbuffer().add(elementDao);
                }
            }
        } catch (final Exception exception) {
            result.setRingbuffer(null);
            result.setErrorMessage(exception.getMessage());
        }

        return apiHelper.ok(result);
    }
}