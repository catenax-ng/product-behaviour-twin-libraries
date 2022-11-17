package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util;

import net.catena_x.btp.libraries.bamm.custom.serialparttypization.SerialPartTypization;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTPartTypeInformation;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class DigitalTwinCategorizer extends DataLoader {
    public DigitalTwinType categorize(final DigitalTwin digitalTwin) throws DataProviderException {
        String nameAtManufacturer = getNameAtManufacturer(digitalTwin);

        if (nameAtManufacturer == null) {
            return DigitalTwinType.UNKNOWN;
        }

        return categorizeFromNameAtManufacturer(nameAtManufacturer);
    }

    private DigitalTwinType categorizeFromNameAtManufacturer(@NotNull final String nameAtManufacturer) {
        switch(nameAtManufacturer) {
            case "Vehicle Combustion": {
                return DigitalTwinType.VEHICLE;
            }
            case "Differential Gear": {
                return DigitalTwinType.GEARBOX;
            }
            default: {
                return DigitalTwinType.UNKNOWN;
            }
        }
    }

    private String getNameAtManufacturer(@NotNull final DigitalTwin digitalTwin) throws DataProviderException {
        // Breaking the law of demeter but want to avoid getting business logic within pure data classes.
        // The data classes are standardized so there won't be frequent changes.
        return getNameAtManufacturerFromSerialPartTypization(getFirstAndOnlyItemAllowNull(
                                                             digitalTwin.getSerialPartTypizations()));
    }

    private String getNameAtManufacturerFromSerialPartTypization(
            @Nullable final SerialPartTypization serialPartTypization) {
        if (serialPartTypization == null) {
            return null;
        }

        return getNameAtManufacturerFromPartTypeInformation(serialPartTypization.getPartTypeInformation());
    }

    private String getNameAtManufacturerFromPartTypeInformation(
            @Nullable final SPTPartTypeInformation partTypeInformation) {
        if(partTypeInformation == null) {
            return null;
        }

        return partTypeInformation.getNameAtManufacturer();
    }
}
