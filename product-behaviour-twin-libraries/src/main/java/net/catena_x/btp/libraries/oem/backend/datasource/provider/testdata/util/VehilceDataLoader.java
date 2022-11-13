package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util;

import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.AssemblyPartRelationship;
import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.items.APRChildPart;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.SerialPartTypization;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTManufacturingInformation;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class VehilceDataLoader extends DataLoader {

    public String getCatenaXId(@NotNull final DigitalTwin vehicleTwin) {
        return vehicleTwin.getCatenaXId();
    }

    public String getVan(@NotNull final DigitalTwin vehicleTwin) throws DataProviderException {
        return getLocalIdentifierOrNull(vehicleTwin, "van");
    }

    public String getGearboxID(@NotNull final DigitalTwin vehicleTwin,
                               @NotNull final CatenaXIdToDigitalTwinType idToType)
            throws DataProviderException {
        return getGearboxIDFromAssemblyPartRelationship(getAssemblyPartRelationshipsOrNull(vehicleTwin), idToType);
    }

    public Instant getProductionDate(@NotNull final DigitalTwin vehicleTwin) throws DataProviderException {
        return getManufacturingDateFromSerialPartTypization(getSerialPartTypizationOrNull(vehicleTwin));
    }

    private String getGearboxIDFromAssemblyPartRelationship(
            @Nullable final AssemblyPartRelationship assemblyPartRelationship,
            @NotNull final CatenaXIdToDigitalTwinType idToType) throws DataProviderException {

        if(assemblyPartRelationship == null) {
            return null;
        }

        return getGearboxIDFromChildParts(assemblyPartRelationship.getChildParts(), idToType);
    }

    private String getGearboxIDFromChildParts(
            @Nullable final List<APRChildPart> childParts,
            @NotNull final CatenaXIdToDigitalTwinType idToType) throws DataProviderException {

        if(isNullOrEmpty(childParts)) {
            return null;
        }

        for (APRChildPart childPart : childParts) {
            if(idToType.determine(childPart.getChildCatenaXId()) == DigitalTwinType.GEARBOX) {
                return childPart.getChildCatenaXId();
            }
        }

        return null;
    }

    private Instant getManufacturingDateFromSerialPartTypization(
            @Nullable final SerialPartTypization serialPartTypization) throws DataProviderException{
        if (serialPartTypization == null) {
            return null;
        }

        return getManufacturingDateFromManufacturingInformation(serialPartTypization.getManufacturingInformation());
    }

    private Instant getManufacturingDateFromManufacturingInformation(
            @Nullable final SPTManufacturingInformation manufacturingInformation) throws DataProviderException {

        if(manufacturingInformation == null) {
            return null;
        }

        try {
            return Instant.parse(manufacturingInformation.getDate());
        } catch (Exception exception) {
            throw new DataProviderException("Error while parsing date of production!", exception);
        }
    }

    private SerialPartTypization getSerialPartTypizationOrNull(@NotNull final DigitalTwin vehicleTwin)
            throws DataProviderException {
        return getFirstAndOnlyItemAllowNull(vehicleTwin.getSerialPartTypizations());
    }

    private AssemblyPartRelationship getAssemblyPartRelationshipsOrNull(@NotNull final DigitalTwin vehicleTwin)
            throws DataProviderException {
        return getFirstAndOnlyItemAllowNull(vehicleTwin.getAssemblyPartRelationships());
    }

    private String getLocalIdentifierOrNull(@NotNull final DigitalTwin vehicleTwin, @NotNull final String key)
            throws DataProviderException {

        SerialPartTypization serialPartTypization = getSerialPartTypizationOrNull(vehicleTwin);

        if(serialPartTypization == null) {
            return null;
        }

        return serialPartTypization.getLocelIdentifier(key);
    }
}
