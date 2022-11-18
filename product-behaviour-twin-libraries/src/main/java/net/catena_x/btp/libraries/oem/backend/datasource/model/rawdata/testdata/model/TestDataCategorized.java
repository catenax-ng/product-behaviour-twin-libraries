package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model;

import lombok.Getter;
import net.catena_x.btp.libraries.bamm.common.BammStatus;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.CLSClass;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.LoadSpectrumType;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.DigitalTwinCategorizer;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.DigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

@Component
public class TestDataCategorized {
    @Autowired DigitalTwinCategorizer digitalTwinCategorizer;

    @Getter private HashMap<String, DigitalTwin> digitalTwinsVehicles = null;
    @Getter private HashMap<String, DigitalTwin> digitalTwinsGearboxes = null;

    private boolean initialized = false;

    public boolean isInitialized() {
        return initialized;
    }

    public void initFromTestData(@NotNull final TestData testData) throws DataProviderException {
        initMaps(testData.getDigitalTwins().size());
        fillMaps(testData);

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

    private void fillMaps(@NotNull final TestData testData) throws DataProviderException {
        for (DigitalTwin digitalTwin : testData.getDigitalTwins() ) {
            switch (digitalTwinCategorizer.categorize(digitalTwin)) {
                case VEHICLE: {
                    //FA: Append missing adaption values (missing in testdata file).
                    appendRandomAdaptionValues(digitalTwin);

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

        if(!isNullOrEmpty(digitalTwin.getClassifiedLoadSpectra())) {
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

    private void reduceLoadSpectra(@Nullable final DigitalTwin digitalTwin) {
        System.out.println("!!!Reducing load spectrum size, not for productive use!!!");

        final List<ClassifiedLoadSpectrum> loadSpectra = digitalTwin.getClassifiedLoadSpectra();

        if(loadSpectra == null) {
            return;
        }

        for (ClassifiedLoadSpectrum loadSpectrum : loadSpectra) {
            reduceSize(loadSpectrum);
        }
    }

    private void reduceSize(@NotNull ClassifiedLoadSpectrum loadSpectrum) {
        final int count = loadSpectrum.getBody().getCounts().getCountsList().length;

        if(loadSpectrum.getMetadata().getComponentDescription() == LoadSpectrumType.GEAR_SET) {
            loadSpectrum.getMetadata().setComponentDescription(LoadSpectrumType.CLUTCH);
        }

        if(count <= 100) {
            return;
        }

        loadSpectrum.getBody().getCounts().setCountsList(
                Arrays.copyOfRange(loadSpectrum.getBody().getCounts().getCountsList(), 0, 100));

        for (CLSClass clsClass : loadSpectrum.getBody().getClasses()) {
            clsClass.setClassList(Arrays.copyOfRange(clsClass.getClassList(), 0, 100));
        }
    }

    protected <T> boolean isNullOrEmpty(@Nullable final Collection<T> collection) {
        if(collection == null) {
            return true;
        }
        return collection.isEmpty();
    }
}
