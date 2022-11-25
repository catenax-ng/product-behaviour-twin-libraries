package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model;

import lombok.Getter;
import net.catena_x.btp.libraries.bamm.common.BammStatus;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.CLSClass;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.LoadSpectrumType;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.CatenaXIdToDigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.DigitalTwinCategorizer;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.DigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.VehicleDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import javax.validation.constraints.NotNull;

import net.catena_x.btp.libraries.util.hleper.ContentChecker;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

@Component
public class TestDataCategorized {
    @Autowired private DigitalTwinCategorizer digitalTwinCategorizer;
    @Autowired private VehicleDataLoader vehicleDataLoader;

    @Getter private HashMap<String, DigitalTwin> digitalTwinsVehicles = null;
    @Getter private HashMap<String, DigitalTwin> digitalTwinsGearboxes = null;

    private boolean initialized = false;

    public boolean isInitialized() {
        return initialized;
    }

    public void reset() {
        initialized = false;
        digitalTwinsVehicles = null;
        digitalTwinsGearboxes = null;
    }

    public void initFromTestData(@NotNull final TestData testData) throws DataProviderException {
        initMaps(testData.getDigitalTwins().size());
        fillMaps(testData);

        postProcessLoadCollectiveTargetComponentIds();

        initialized = true;
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

    public DigitalTwin getGearboxTwinFromVehicleTwin(@NotNull final DigitalTwin vehicleTwin) {
        try {
            final CatenaXIdToDigitalTwinType idToType =
                    (@NotNull final String catenaXId) -> this.catenaXIdToType(catenaXId);

            final String gearboxId = vehicleDataLoader.getGearboxID(vehicleTwin, idToType);
            if (gearboxId == null) {
                return null;
            }

            return this.getDigitalTwinsGearboxes().get(gearboxId);
        } catch (DataProviderException exception) {
            return null;
        }
    }

    public DigitalTwin getGearboxTwinFromVehicleTwinMustExists(@NotNull final DigitalTwin vehicleTwin)
            throws DataProviderException {

        final DigitalTwin gearboxTwin = getGearboxTwinFromVehicleTwin(vehicleTwin);

        if(gearboxTwin == null) {
            throw new DataProviderException("No gearbox twin found for vehicle twin "
                    + vehicleTwin.getCatenaXId() + "!");
        }

        return gearboxTwin;
    }

    private void fillMaps(@NotNull final TestData testData) throws DataProviderException {
        int loadSpectrumVariant = 0;
        for (final DigitalTwin digitalTwin : testData.getDigitalTwins() ) {
            switch (digitalTwinCategorizer.categorize(digitalTwin)) {
                case VEHICLE: {
                    //FA: Append missing adaption values (missing in testdata file).
                    appendRandomAdaptionValues(digitalTwin);

                    //FA Append missing clutch load spectrum.
                    appendMissingClutchLoadSpectrum(digitalTwin, testData, loadSpectrumVariant);
                    loadSpectrumVariant = (loadSpectrumVariant + 1) % 3;

                    //FA: Reduce data size of load spectra to fit in database varchar type.
                    reduceLoadSpectra(digitalTwin);

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

    private void appendRandomAdaptionValues(@Nullable final DigitalTwin digitalTwin) {
        AdaptionValues adaptionValues = new AdaptionValues();

        BammStatus status = null;

        if(!ContentChecker.isNullOrEmpty(digitalTwin.getClassifiedLoadSpectra())) {
            if(digitalTwin.getClassifiedLoadSpectra().get(0).getMetadata() != null) {
                status = digitalTwin.getClassifiedLoadSpectra().get(0).getMetadata().getStatus();
            }
        }

        if(status == null) {
            status = new BammStatus();
            status.setMileage(123456L);
            status.setDate(Instant.now());
            status.setOperatingTime("12.3456");
            status.setRouteDescription("Illegal status!");
        }

        if(status.getDate() == null) {
            status.setDate(Instant.now().minus(Duration.ofHours(3L)));
        }

        adaptionValues.setStatus(status);

        adaptionValues.setValues(new double[]{0.5, 16554.6, 234.3,323.0});

        List<AdaptionValues> adaptionValueList = new ArrayList<>(1);
        adaptionValueList.add(adaptionValues);

        digitalTwin.setAdaptionValues(adaptionValueList);
    }

    private void appendMissingClutchLoadSpectrum(@NotNull final DigitalTwin digitalTwin,
                                                 @NotNull final TestData testData, int loadSpectrumVariant)
                throws DataProviderException {

        ensureLoadspectraList(digitalTwin);

        if (!loadSpectraContains(digitalTwin.getClassifiedLoadSpectra(), LoadSpectrumType.CLUTCH)) {
            System.out.println("!!!Adding load spectrum of type clutch, not for productive use!!!");


            final ClassifiedLoadSpectrum loadSpectrum = switch (loadSpectrumVariant) {
                            case 1 -> testData.getClutchLoadSpectrumYellow();
                            case 2 -> testData.getClutchLoadSpectrumRed();
                            default -> testData.getClutchLoadSpectrumGreen();
                        };

            if(loadSpectrum != null) {
                digitalTwin.getClassifiedLoadSpectra().add(loadSpectrum);
            }
        }
    }

    private static void ensureLoadspectraList(DigitalTwin digitalTwin) {
        if(digitalTwin.getClassifiedLoadSpectra() == null) {
            digitalTwin.setClassifiedLoadSpectra(new ArrayList<>());
        }
    }

    private boolean loadSpectraContains(@NotNull final List<ClassifiedLoadSpectrum> loadSpectra,
                                        @NotNull final LoadSpectrumType loadSpectrumType) throws DataProviderException {

        for (final ClassifiedLoadSpectrum loadSpectrum : loadSpectra) {
            assertComponentDescription(loadSpectrum);
            if(loadSpectrum.getMetadata().getComponentDescription() == loadSpectrumType) {
                return true;
            }
        }

        return false;
    }

    private void assertComponentDescription(@NotNull final ClassifiedLoadSpectrum loadSpectrum)
            throws DataProviderException {
        if(loadSpectrum == null) {
            throw new DataProviderException("Load spectrum not present!");
        }

        if(loadSpectrum.getMetadata() == null) {
            throw new DataProviderException("Metadata not present!");
        }

        if(loadSpectrum.getMetadata().getComponentDescription() == null) {
            throw new DataProviderException("Component description not present!");
        }
    }

    private void reduceLoadSpectra(@Nullable final DigitalTwin digitalTwin) {
        /*
        System.out.println("!!!Reducing load spectrum size, not for productive use!!!");

        final List<ClassifiedLoadSpectrum> loadSpectra = digitalTwin.getClassifiedLoadSpectra();

        if(loadSpectra == null) {
            return;
        }

        for (final ClassifiedLoadSpectrum loadSpectrum : loadSpectra) {
            reduceSize(loadSpectrum);
        }
        */
    }

    private void reduceSize(@NotNull ClassifiedLoadSpectrum loadSpectrum) {
        final int count = loadSpectrum.getBody().getCounts().getCountsList().length;

        if(count <= 100) {
            return;
        }

        loadSpectrum.getBody().getCounts().setCountsList(
                Arrays.copyOfRange(loadSpectrum.getBody().getCounts().getCountsList(), 0, 100));

        for (CLSClass clsClass : loadSpectrum.getBody().getClasses()) {
            clsClass.setClassList(Arrays.copyOfRange(clsClass.getClassList(), 0, 100));
        }
    }

    private void postProcessLoadCollectiveTargetComponentIds() {
        digitalTwinsVehicles.forEach((vehicleId, vehicleTwin)->{
            ensureLoadspectraList(vehicleTwin);

            final CatenaXIdToDigitalTwinType idToType =
                    (@NotNull final String catenaXId) -> this.catenaXIdToType(catenaXId);

            String gearboxId = null;
            try {
                gearboxId = vehicleDataLoader.getGearboxID(vehicleTwin, idToType);
            } catch (DataProviderException exception) {
                //ignore
                return;
           }

           for (final ClassifiedLoadSpectrum loadSpectrum : vehicleTwin.getClassifiedLoadSpectra()) {
                try {
                    assertComponentDescription(loadSpectrum);
                } catch (DataProviderException exception) {
                    //ignore
                    continue;
                }

                if(loadSpectrum.getMetadata().getComponentDescription() == LoadSpectrumType.CLUTCH) {
                    loadSpectrum.setTargetComponentID(gearboxId);
                }
            }
        });
    }
}
