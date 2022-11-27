package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util;

import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.AssemblyPartRelationship;
import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.items.APRChildPart;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.SerialPartTypization;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTManufacturingInformation;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Component
public class VehicleDataLoader {

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

        if(DataHelper.isNullOrEmpty(childParts)) {
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
            return manufacturingInformation.getDate();
        } catch (final Exception exception) {
            throw new DataProviderException("Error while parsing date of production!", exception);
        }
    }

    private SerialPartTypization getSerialPartTypizationOrNull(@NotNull final DigitalTwin vehicleTwin)
            throws DataProviderException {
        try {
            return DataHelper.getFirstAndOnlyItemAllowNull(vehicleTwin.getSerialPartTypizations());
        } catch (final BtpException exception) {
            throw new DataProviderException(exception);
        }
    }

    private AssemblyPartRelationship getAssemblyPartRelationshipsOrNull(@NotNull final DigitalTwin vehicleTwin)
            throws DataProviderException {
        try {
            return DataHelper.getFirstAndOnlyItemAllowNull(vehicleTwin.getAssemblyPartRelationships());
        } catch (final BtpException exception) {
            throw new DataProviderException(exception);
        }
    }

    private String getLocalIdentifierOrNull(@NotNull final DigitalTwin vehicleTwin, @NotNull final String key)
            throws DataProviderException {

        try {
            SerialPartTypization serialPartTypization = getSerialPartTypizationOrNull(vehicleTwin);

            if (serialPartTypization == null) {
                return null;
            }

            return serialPartTypization.getLocelIdentifier(key);
        } catch (final BtpException exception) {
            throw new DataProviderException(exception);
        }
    }
}
