package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.DigitalTwinCategorizer;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.DigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import java.util.HashMap;

@Component
public class TestDataReader {
    @Autowired DigitalTwinCategorizer digitalTwinCategorizer;

    @Getter private HashMap<String, DigitalTwin> digitalTwinsVehicles = null;
    @Getter private HashMap<String, DigitalTwin> digitalTwinsGearboxes = null;

    private ObjectMapper objectMapper = null;

    public void loadFromFile(@NotNull final Path filename) throws IOException, DataProviderException {

        final TestData testData = readFromFile(filename);

        initMaps(testData.getDigitalTwins().size());
        fillMaps(testData);
    }

    private void fillMaps(final TestData testData) throws DataProviderException {
        for (DigitalTwin digitalTwin : testData.getDigitalTwins() ) {
            switch (digitalTwinCategorizer.categorize(digitalTwin)) {
                case VEHICLE: {
                    digitalTwinsVehicles.put(digitalTwin.getCatenaXId(), digitalTwin);
                    break;
                }

                case GEARBOX: {
                    digitalTwinsGearboxes.put(digitalTwin.getCatenaXId(), digitalTwin);
                    break;
                }

                case UNKNOWN:
                default: {
                    throw new DataProviderException("Neither vehicle nor gearbox twin!");
                }
            }
        }
    }

    public DigitalTwinType catenaXIdToType(@NotNull final String catenaXId) {
        if(digitalTwinsVehicles.isEmpty()){
            return DigitalTwinType.UNKNOWN;
        }

        if(digitalTwinsVehicles.containsKey(catenaXId)) {
            return DigitalTwinType.VEHICLE;
        }
        else if(digitalTwinsGearboxes.containsKey(catenaXId)) {
            return DigitalTwinType.GEARBOX;
        }

        return DigitalTwinType.UNKNOWN;
    }


    private void initMaps(final int intiSize) {
        digitalTwinsVehicles = new HashMap<>(intiSize);
        digitalTwinsGearboxes = new HashMap<>(intiSize);
    }

    private TestData readFromFile(@NotNull final Path filename) throws IOException {
        final File inputFile = new File(filename.toString());
        intObjectManager();
        return objectMapper.readValue(inputFile, TestData.class);
    }

    private void intObjectManager() {
        if(objectMapper != null) {
            return;
        }

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
}
