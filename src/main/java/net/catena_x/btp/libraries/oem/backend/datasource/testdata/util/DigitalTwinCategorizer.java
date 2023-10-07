package net.catena_x.btp.libraries.oem.backend.datasource.testdata.util;

import net.catena_x.btp.libraries.bamm.custom.serialparttypization.SerialPartTypization;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTPartTypeInformation;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class DigitalTwinCategorizer {
    public static DigitalTwinType categorize(@NotNull final DigitalTwin digitalTwin) throws DataProviderException {
        String nameAtManufacturer = getNameAtManufacturer(digitalTwin);

        if (nameAtManufacturer == null) {
            return DigitalTwinType.UNKNOWN;
        }

        return categorizeFromNameAtManufacturer(nameAtManufacturer);
    }

    private static DigitalTwinType categorizeFromNameAtManufacturer(@NotNull final String nameAtManufacturer) {
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

    private static String getNameAtManufacturer(@NotNull final DigitalTwin digitalTwin) throws DataProviderException {
        // Breaking the law of demeter but want to avoid getting business logic within pure data classes.
        // The data classes are standardized so there won't be frequent changes.
        try {
            return getNameAtManufacturerFromSerialPartTypization(DataHelper.getFirstAndOnlyItemAllowNull(
                    digitalTwin.getSerialPartTypizations()));
        } catch (final BtpException exception) {
            throw new DataProviderException(exception);
        }

    }

    private static String getNameAtManufacturerFromSerialPartTypization(
            @Nullable final SerialPartTypization serialPartTypization) {
        if (serialPartTypization == null) {
            return null;
        }

        return getNameAtManufacturerFromPartTypeInformation(serialPartTypization.getPartTypeInformation());
    }

    private static String getNameAtManufacturerFromPartTypeInformation(
            @Nullable final SPTPartTypeInformation partTypeInformation) {
        if(partTypeInformation == null) {
            return null;
        }

        return partTypeInformation.getNameAtManufacturer();
    }
}
