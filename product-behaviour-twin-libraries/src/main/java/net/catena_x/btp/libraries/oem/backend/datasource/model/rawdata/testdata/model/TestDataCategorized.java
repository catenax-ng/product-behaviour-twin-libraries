package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.catena_x.btp.libraries.bamm.common.BammStatus;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.LoadSpectrumType;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.CatenaXIdToDigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.DigitalTwinCategorizer;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.DigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.VehicleDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.UncheckedDataProviderException;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

@Component
public class TestDataCategorized {
    @Autowired private DigitalTwinCategorizer digitalTwinCategorizer;
    @Autowired private VehicleDataLoader vehicleDataLoader;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;

    @Getter private HashMap<String, DigitalTwin> digitalTwinsVehicles = null;
    @Getter private HashMap<String, DigitalTwin> digitalTwinsGearboxes = null;

    private boolean initialized = false;
    private Random randomGenerator = new Random();

    public boolean isInitialized() {
        return initialized;
    }

    public void reset() {
        initialized = false;
        digitalTwinsVehicles = null;
        digitalTwinsGearboxes = null;
    }

    public void initAutowiredFrom(@NotNull final TestDataCategorized testDataSource) {
        digitalTwinCategorizer = testDataSource.digitalTwinCategorizer;
        vehicleDataLoader = testDataSource.vehicleDataLoader;
    }

    public void initFromTestData(@NotNull final TestData testData) throws DataProviderException {
        initMaps(testData.getDigitalTwins().size());
        fillMaps(testData);

        postProcessLoadCollectiveTargetComponentIds();
        postProcessMoveRuLAndDamage();

        initialized = true;
    }

    public DigitalTwinType catenaXIdToType(@NotNull final String catenaXId) {
        if (digitalTwinsVehicles.isEmpty()) {
            return DigitalTwinType.UNKNOWN;
        }

        if (digitalTwinsVehicles.containsKey(catenaXId)) {
            return DigitalTwinType.VEHICLE;
        } else if (digitalTwinsGearboxes.containsKey(catenaXId)) {
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
        } catch (final BtpException exception) {
            return null;
        }
    }

    public DigitalTwin getGearboxTwinFromVehicleTwinMustExists(@NotNull final DigitalTwin vehicleTwin)
            throws DataProviderException {

        final DigitalTwin gearboxTwin = getGearboxTwinFromVehicleTwin(vehicleTwin);

        if (gearboxTwin == null) {
            throw new DataProviderException("No gearbox twin found for vehicle twin "
                    + vehicleTwin.getCatenaXId() + "!");
        }

        return gearboxTwin;
    }

    private void fillMaps(@NotNull final TestData testData) throws DataProviderException {
        for (final DigitalTwin digitalTwin : testData.getDigitalTwins()) {
            switch (digitalTwinCategorizer.categorize(digitalTwin)) {
                case VEHICLE: {
                    appendMissingData(digitalTwin, testData);
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

    private void appendMissingData(@NotNull final DigitalTwin digitalTwin, @NotNull final TestData testData)
            throws DataProviderException {
        final int variant = generateNextVariant();
        appendMissingClutchLoadSpectrum(digitalTwin, testData, variant);
        appendRandomAdaptionValues(digitalTwin, variant);
    }

    private int generateNextVariant() {
        final int modifier = randomGenerator.nextInt(200);

        if(modifier % 15 == 0) {
            return 2;
        }

        if(modifier % 6 == 0) {
            return 1;
        }

        return 0;
    }

    private void initMaps(final int intiSize) {
        digitalTwinsVehicles = new HashMap<>(intiSize);
        digitalTwinsGearboxes = new HashMap<>(intiSize);
    }

    private void appendRandomAdaptionValues(@Nullable final DigitalTwin digitalTwin, int variant) {
        if(randomGenerator.nextInt(100) >= 93) {
            /* Use different variant in 7 % of all cases. */
            variant = (variant + randomGenerator.nextInt(2) + 1) % 3;
        }

        AdaptionValues adaptionValues = new AdaptionValues();
        BammStatus status = null;

        if (!DataHelper.isNullOrEmpty(digitalTwin.getClassifiedLoadSpectra())) {
            if (digitalTwin.getClassifiedLoadSpectra().get(0).getMetadata() != null) {
                status = digitalTwin.getClassifiedLoadSpectra().get(0).getMetadata().getStatus();
            }
        }

        if (status == null) {
            status = new BammStatus();
            status.setMileage(123456L);
            status.setDate(Instant.now());
            status.setOperatingHours(12.3456f);
        }

        if (status.getDate() == null) {
            status.setDate(Instant.now().minus(Duration.ofHours(3L)));
        }

        adaptionValues.setStatus(status);

        adaptionValues.setValues(generateRandomAdaptionValues(variant));

        final List<AdaptionValues> adaptionValueList = new ArrayList<>(1);
        adaptionValueList.add(adaptionValues);

        digitalTwin.setAdaptionValues(adaptionValueList);
    }

    private double[] generateRandomAdaptionValues(@NotNull final int variant) {
        double[] adaptionValues = new double[4];
        final double[] maxValues = new double[]{100.0, 255.0, 100.0, 255.0};

        for (int i = 0; i < 4; i++) {
            adaptionValues[i] = generateNextAdaptionValue(maxValues[i], variant);
        }

        return adaptionValues;
    }

    private double generateNextAdaptionValue(@NotNull final double maxValue, @NotNull final int variant) {
        double relResult = 0.0;

        /* Allow some values outside the thresholds. */
        if(variant == 1) {
            relResult = 0.78 + randomGenerator.nextDouble(0.19);
        } else if(variant == 2) {
            relResult = 0.92 + min(randomGenerator.nextDouble(0.1), 1.0);
        } else {
            relResult = randomGenerator.nextDouble(1.0);
            relResult = relResult * relResult;
        }

        /* Randum signum. */
        if(randomGenerator.nextInt(100) >= 75) {
            relResult = -relResult;
        }

        return relResult * maxValue;
    }

    private void appendMissingClutchLoadSpectrum(@NotNull final DigitalTwin digitalTwin,
                                                 @NotNull final TestData testData, @NotNull int variant)
            throws DataProviderException {

        ensureLoadspectraList(digitalTwin);

        if (!loadSpectraContains(digitalTwin.getClassifiedLoadSpectra(), LoadSpectrumType.CLUTCH)) {
            System.out.println("!!!Adding load spectrum of type clutch, not for productive use!!!");

            final ClassifiedLoadSpectrum loadSpectrum = switch (variant) {
                case 1 -> generateModifiedLoadSpectrum(testData.getClutchLoadSpectrumYellow());
                case 2 -> generateModifiedLoadSpectrum(testData.getClutchLoadSpectrumRed());
                default -> generateModifiedLoadSpectrum(testData.getClutchLoadSpectrumGreen());
            };

            if (loadSpectrum != null) {
                digitalTwin.getClassifiedLoadSpectra().add(loadSpectrum);
            }
        }
    }

    private ClassifiedLoadSpectrum generateModifiedLoadSpectrum(@NotNull final ClassifiedLoadSpectrum loadSpectrum)
            throws DataProviderException {

        ClassifiedLoadSpectrum modifiedLoadSpectrum = null;

        try {
            modifiedLoadSpectrum = objectMapper.readValue(objectMapper.writeValueAsString(loadSpectrum),
                                                          ClassifiedLoadSpectrum.class);
        } catch (final JsonProcessingException exception){
            throw new DataProviderException(exception);
        }

        final double rel = 0.5 + randomGenerator.nextDouble(1.4);

        int count = modifiedLoadSpectrum.getBody().getCounts().getCountsList().length;
        for (int i = 0; i < count; i++) {
            modifiedLoadSpectrum.getBody().getCounts().getCountsList()[i] *= rel;
        }

        return modifiedLoadSpectrum;
    }

    private static void ensureLoadspectraList(DigitalTwin digitalTwin) {
        if (digitalTwin.getClassifiedLoadSpectra() == null) {
            digitalTwin.setClassifiedLoadSpectra(new ArrayList<>());
        }
    }

    private boolean loadSpectraContains(@NotNull final List<ClassifiedLoadSpectrum> loadSpectra,
                                        @NotNull final LoadSpectrumType loadSpectrumType) throws DataProviderException {

        for (final ClassifiedLoadSpectrum loadSpectrum : loadSpectra) {
            assertComponentDescription(loadSpectrum);
            if (loadSpectrum.getMetadata().getComponentDescription() == loadSpectrumType) {
                return true;
            }
        }

        return false;
    }

    private void assertComponentDescription(@NotNull final ClassifiedLoadSpectrum loadSpectrum)
            throws DataProviderException {
        if (loadSpectrum == null) {
            throw new DataProviderException("Load spectrum not present!");
        }

        if (loadSpectrum.getMetadata() == null) {
            throw new DataProviderException("Metadata not present!");
        }

        if (loadSpectrum.getMetadata().getComponentDescription() == null) {
            throw new DataProviderException("Component description not present!");
        }
    }

    private void postProcessLoadCollectiveTargetComponentIds() {
        digitalTwinsVehicles.forEach((vehicleId, vehicleTwin) -> {
            ensureLoadspectraList(vehicleTwin);

            final CatenaXIdToDigitalTwinType idToType =
                    (@NotNull final String catenaXId) -> this.catenaXIdToType(catenaXId);

            String gearboxId = null;
            try {
                gearboxId = vehicleDataLoader.getGearboxID(vehicleTwin, idToType);
            } catch (final BtpException exception) {
                //ignore
                return;
            }

            for (final ClassifiedLoadSpectrum loadSpectrum : vehicleTwin.getClassifiedLoadSpectra()) {
                try {
                    assertComponentDescription(loadSpectrum);
                } catch (final DataProviderException exception) {
                    //ignore
                    continue;
                }

                if (loadSpectrum.getMetadata().getComponentDescription() == LoadSpectrumType.CLUTCH) {
                    loadSpectrum.setTargetComponentID(gearboxId);
                }
            }
        });
    }

    private void postProcessMoveRuLAndDamage() throws DataProviderException {
        try {
            digitalTwinsVehicles.forEach((vehicleId, vehicleTwin) -> {
                final DigitalTwin gearboxTwin = getGearboxTwinFromVehicleTwin(vehicleTwin);

                if (gearboxTwin != null) {
                    try {
                        assertExistsMaxOnce(vehicleTwin.getDamages(), gearboxTwin.getDamages());
                        assertExistsMaxOnce(vehicleTwin.getRemainingUsefulLifes(), gearboxTwin.getRemainingUsefulLifes());

                        if (gearboxTwin.getDamages() == null) {
                            gearboxTwin.setDamages(vehicleTwin.getDamages());
                            vehicleTwin.setDamages(null);
                        }

                        if (gearboxTwin.getRemainingUsefulLifes() == null) {
                            gearboxTwin.setRemainingUsefulLifes(vehicleTwin.getRemainingUsefulLifes());
                            vehicleTwin.setRemainingUsefulLifes(null);
                        }
                    } catch (final DataProviderException exception) {
                        throw new UncheckedDataProviderException(exception);
                    }
                }
            });
        } catch (final UncheckedDataProviderException exception) {
            throw new DataProviderException(exception);
        }
    }

    private <T> void assertExistsMaxOnce(@Nullable final List<T> value1, @Nullable final List<T> value2)
            throws DataProviderException {

        if(value1 == null || value2 == null) {
            return;
        }

        throw new DataProviderException("Both values ars set, but different!");
    }
}