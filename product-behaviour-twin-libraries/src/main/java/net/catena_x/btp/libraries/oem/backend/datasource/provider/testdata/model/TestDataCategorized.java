package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.model;

import lombok.Getter;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.DigitalTwinCategorizer;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.DigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TestDataCategorized {
    @Autowired DigitalTwinCategorizer digitalTwinCategorizer;

    @Getter private HashMap<String, DigitalTwin> digitalTwinsVehicles = null;
    @Getter private HashMap<String, DigitalTwin> digitalTwinsGearboxes = null;

    public void initFromTestData(@NotNull final TestData testData) throws DataProviderException {
        initMaps(testData.getDigitalTwins().size());
        fillMaps(testData);
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

    private void fillMaps(@NotNull final TestData testData) throws DataProviderException {
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

    private void initMaps(final int intiSize) {
        digitalTwinsVehicles = new HashMap<>(intiSize);
        digitalTwinsGearboxes = new HashMap<>(intiSize);
    }
}
