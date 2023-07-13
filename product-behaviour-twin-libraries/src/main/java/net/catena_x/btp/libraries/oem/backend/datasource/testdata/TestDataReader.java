package net.catena_x.btp.libraries.oem.backend.datasource.testdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.bamm.common.BammMeasurementUnit;
import net.catena_x.btp.libraries.bamm.common.BammQuantity;
import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.AssemblyPartRelationship;
import net.catena_x.btp.libraries.bamm.custom.assemblypartrelationship.items.APRChildPart;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.SerialPartTypization;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTLocalIdentifier;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTManufacturingInformation;
import net.catena_x.btp.libraries.bamm.custom.serialparttypization.items.SPTPartTypeInformation;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.rul.DefaultRuLInputDAO;
import net.catena_x.btp.libraries.bamm.rul.DefaultRuLNotificationToSupplierContentDAO;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.RuLTestdataInputElement;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestdataConfig;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TestDataReader {
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;

    private final Random rand = new Random();
    private final Logger logger = LoggerFactory.getLogger(TestDataReader.class);

    public TestData loadFromFile(@NotNull final Path filename) throws DataProviderException {
        return loadFromFiles(filename, null, null, null);
    }

    public TestData loadFromFiles(@NotNull final Path filename, @Nullable final Path clutchSpectrumGreen,
                                  @Nullable final Path clutchSpectrumYellow, @Nullable final Path clutchSpectrumRed)
            throws DataProviderException {

        try {
            final TestData testData = objectMapper.readValue(new File(filename.toString()), TestData.class);
            extendTestData(testData, clutchSpectrumGreen, clutchSpectrumYellow, clutchSpectrumRed);
            return testData;

        } catch (final IOException exception) {
            throw new DataProviderException("Error while opening testdata file(s)!", exception);
        }
    }

    public TestData loadFromJson(@NotNull final String testDataJson) throws DataProviderException {
        try {
            return objectMapper.readValue(testDataJson, TestData.class);
        } catch (final IOException exception) {
            throw new DataProviderException("Error while reading testdata from json string!", exception);
        }
    }

    public void appendFromJson(@NotNull final TestData existingTestdata,
                               @NotNull final String testDataJson) throws DataProviderException {
        try {
            append(existingTestdata, loadFromJson(testDataJson));
        } catch (final IOException exception) {
            throw new DataProviderException("Error while reading testdata from json string!", exception);
        }
    }

    public TestData loadFromConfigFile(@NotNull final Path filename) throws DataProviderException {
        try {
            final TestdataConfig config = objectMapper.readValue(new File(filename.toString()), TestdataConfig.class);
            return loadFromConfig(config);
        } catch (final IOException exception) {
            throw new DataProviderException("Error while reading testdata from config file!", exception);
        }
    }

    public TestData loadFromConfig(@NotNull final TestdataConfig testDataConfig) throws DataProviderException {
        try {
            TestData testData = new TestData();
            testData.setDigitalTwins(new ArrayList<>());

            for (final RuLTestdataInputElement testdataInputElement: testDataConfig.rulTestDataFiles()) {
                logger.info("Reading file \"" + testdataInputElement.filename() + "\"");

                logger.info(String.format("New relation: VIN: %s, VAN: %s, CX-Id: %s, gearbox-Id: %s",
                        testdataInputElement.vehicle().vin(),
                        testdataInputElement.vehicle().van(),
                        testdataInputElement.vehicle().catenaxId(),
                        testdataInputElement.gearbox().catenaxId()));

                final String data = replacePlaceholders(Files.readString(testdataInputElement.filename()));
                NotificationDAO<DefaultRuLNotificationToSupplierContentDAO> testDataElement = objectMapper.readValue(
                        data, new TypeReference<NotificationDAO<DefaultRuLNotificationToSupplierContentDAO>>() {});

                final DigitalTwin gearboxTwin = new DigitalTwin();
                gearboxTwin.setCatenaXId(testdataInputElement.gearbox().catenaxId());
                addSerialPartTypizationGearbox(gearboxTwin, testdataInputElement);
                addAssemblyPartRelationshipGearbox(gearboxTwin, testdataInputElement);

                final DigitalTwin vehicleTwin = new DigitalTwin();
                vehicleTwin.setCatenaXId(testdataInputElement.vehicle().catenaxId());
                addSerialPartTypizationVehicle(vehicleTwin, testdataInputElement);
                addAssemblyPartRelationshipVehicle(vehicleTwin, testdataInputElement);

                final ArrayList<ClassifiedLoadSpectrum> loadSpectra = new ArrayList<>();

                final DefaultRuLInputDAO input = testDataElement.getContent().getEndurancePredictorInputs().get(0);
                if(input.getClassifiedLoadSpectrumGearSet() != null) {
                    input.getClassifiedLoadSpectrumGearSet().setTargetComponentID(gearboxTwin.getCatenaXId());
                    loadSpectra.add(input.getClassifiedLoadSpectrumGearSet());

                    logger.info(String.format("Load spectrum GearSet: %s",
                            loadSpectrumToString(input.getClassifiedLoadSpectrumGearSet())));
                }

                if(input.getClassifiedLoadSpectrumGearOil() != null) {
                    input.getClassifiedLoadSpectrumGearOil().setTargetComponentID(gearboxTwin.getCatenaXId());
                    loadSpectra.add(input.getClassifiedLoadSpectrumGearOil());

                    logger.info(String.format("Load spectrum GearOil: %s",
                            loadSpectrumToString(input.getClassifiedLoadSpectrumGearOil())));
                }

                vehicleTwin.setClassifiedLoadSpectra(loadSpectra);

                testData.getDigitalTwins().add(vehicleTwin);
                testData.getDigitalTwins().add(gearboxTwin);
            }
            return testData;
        } catch (final IOException exception) {
            throw new DataProviderException("Error while reading testdata from config file!", exception);
        }
    }

    private String loadSpectrumToString(@NotNull final ClassifiedLoadSpectrum profile) {
        final double sum = Arrays.stream(profile.getBody().getCounts().getCountsList()).sum();
        try {
            return String.format("Sum: %.3f, Status:\n%s", sum,
                objectMapper.writeValueAsString(profile.getMetadata().getStatus()));
        } catch(final JsonProcessingException exception) {
            return String.format("Sum: %.3f, Status not readable.", sum);
        }
    }

    private void addSerialPartTypizationVehicle(@NotNull final DigitalTwin vehicleTwin,
                                                @NotNull final RuLTestdataInputElement testdataInputElement) {
        final SerialPartTypization serialPartTypization = new SerialPartTypization();
        serialPartTypization.setCatenaXId(vehicleTwin.getCatenaXId());

        final String manufacturerPartId = generateVehicleManufacturerPartId();

        List<SPTLocalIdentifier> localIdentifiers = new ArrayList<>();
        localIdentifiers.add(new SPTLocalIdentifier("manufacturerId", testdataInputElement.vehicle().manufacturerId()));
        localIdentifiers.add(new SPTLocalIdentifier("manufacturerPartId", manufacturerPartId));
        localIdentifiers.add(new SPTLocalIdentifier("partInstanceId", testdataInputElement.vehicle().van()));
        localIdentifiers.add(new SPTLocalIdentifier("van", testdataInputElement.vehicle().van()));
        serialPartTypization.setLocalIdentifiers(localIdentifiers);

        serialPartTypization.setManufacturingInformation(
                new SPTManufacturingInformation(testdataInputElement.vehicle().manufacturingDate(), "DEU"));

        serialPartTypization.setPartTypeInformation(new SPTPartTypeInformation(
                                                          "product",
                                                                    manufacturerPartId,
                                                    "Vehicle Combustion",
                                                                    null, null));

        final ArrayList<SerialPartTypization> serialPartTypizations = new ArrayList<>();
        serialPartTypizations.add(serialPartTypization);
        vehicleTwin.setSerialPartTypizations(serialPartTypizations);
    }

    private void addSerialPartTypizationGearbox(@NotNull final DigitalTwin gearboxTwin,
                                                @NotNull final RuLTestdataInputElement testdataInputElement) {
        final SerialPartTypization serialPartTypization = new SerialPartTypization();
        serialPartTypization.setCatenaXId(gearboxTwin.getCatenaXId());

        final String manufacturerPartId = generateGearboxManufacturerPartId();

        List<SPTLocalIdentifier> localIdentifiers = new ArrayList<>();
        localIdentifiers.add(new SPTLocalIdentifier("manufacturerId", testdataInputElement.gearbox().manufacturerId()));
        localIdentifiers.add(new SPTLocalIdentifier("manufacturerPartId", manufacturerPartId));
        localIdentifiers.add(new SPTLocalIdentifier("partInstanceId", generateGearboxPartInstanceId()));
        serialPartTypization.setLocalIdentifiers(localIdentifiers);

        serialPartTypization.setManufacturingInformation(
                new SPTManufacturingInformation(testdataInputElement.gearbox().manufacturingDate(), "DEU"));

        serialPartTypization.setPartTypeInformation(new SPTPartTypeInformation(
                "component",
                manufacturerPartId, "Differential Gear",
                manufacturerPartId, "Differential Gear"));

        final ArrayList<SerialPartTypization> serialPartTypizations = new ArrayList<>();
        serialPartTypizations.add(serialPartTypization);
        gearboxTwin.setSerialPartTypizations(serialPartTypizations);
    }

    private void addAssemblyPartRelationshipVehicle(@NotNull final DigitalTwin vehicleTwin,
                                                    @NotNull final RuLTestdataInputElement testdataInputElement) {
        final ArrayList<AssemblyPartRelationship> assemblyPartRelationships = new ArrayList<>();

        final AssemblyPartRelationship vehicleAssemblyPartRelationship = new AssemblyPartRelationship();
        vehicleAssemblyPartRelationship.setCatenaXId(testdataInputElement.vehicle().catenaxId());

        final List<APRChildPart> childParts = new ArrayList<>();
        final APRChildPart childPart = new APRChildPart();
        childPart.setChildCatenaXId(testdataInputElement.gearbox().catenaxId());
        childPart.setQuantity(new BammQuantity(1, new BammMeasurementUnit(
                "urn:bamm:io.openmanufacturing:meta-model:1.0.0#curie", "unit:piece")));
        childPart.setLifecycleContext("AsBuilt");
        childPart.setAssembledOn(testdataInputElement.gearbox().assembledOn());
        childPart.setLastModifiedOn(testdataInputElement.gearbox().assembledOn());
        childParts.add(childPart);

        vehicleAssemblyPartRelationship.setChildParts(childParts);
        assemblyPartRelationships.add(vehicleAssemblyPartRelationship);
        vehicleTwin.setAssemblyPartRelationships(assemblyPartRelationships);
    }

    private void addAssemblyPartRelationshipGearbox(@NotNull final DigitalTwin gearboxTwin,
                                                    @NotNull final RuLTestdataInputElement testdataInputElement) {
        final ArrayList<AssemblyPartRelationship> assemblyPartRelationships = new ArrayList<>();
        final AssemblyPartRelationship gearboxAssemblyPartRelationship = new AssemblyPartRelationship();
        gearboxAssemblyPartRelationship.setCatenaXId(testdataInputElement.gearbox().catenaxId());
        assemblyPartRelationships.add(gearboxAssemblyPartRelationship);
        gearboxTwin.setAssemblyPartRelationships(assemblyPartRelationships);
    }

    private String generateVehicleManufacturerPartId() {
        return RandomStringUtils.randomAlphabetic(2).toUpperCase() + "-"
                + String.format("%02d", rand.nextInt(100));
    }

    private String generateGearboxManufacturerPartId() {
        return RandomStringUtils.randomAlphabetic(2).toUpperCase() + "-"
                + String.format("%02d", rand.nextInt(100));
    }

    private String generateGearboxPartInstanceId() {
        return "NO" + "-" + String.format("%08d", rand.nextLong(100000000L))
                + String.format("%08d", rand.nextLong(100000000L))
                + String.format("%08d", rand.nextLong(100000000L));
    }

    public void appendFromConfig(@NotNull final TestData existingTestdata,
                                 @NotNull final TestdataConfig testDataConfig) throws DataProviderException {
        try {
            append(existingTestdata, loadFromConfig(testDataConfig));
        } catch (final IOException exception) {
            throw new DataProviderException("Error while reading testdata from config!", exception);
        }
    }

    private ClassifiedLoadSpectrum loadClutcLoadSpectrumIfPresent(@Nullable final Path clutchSpectrum)
            throws IOException {

        if(clutchSpectrum == null) {
            return null;
        }

        return objectMapper.readValue(new File(clutchSpectrum.toString()), ClassifiedLoadSpectrum.class);
    }

    private void extendTestData(@NotNull TestData testData, @Nullable final Path clutchSpectrumGreen,
                                @Nullable final Path clutchSpectrumYellow, @Nullable final Path clutchSpectrumRed)
            throws IOException {

        testData.setClutchLoadSpectrumGreen(loadClutcLoadSpectrumIfPresent(clutchSpectrumGreen));
        testData.setClutchLoadSpectrumYellow(loadClutcLoadSpectrumIfPresent(clutchSpectrumYellow));
        testData.setClutchLoadSpectrumRed(loadClutcLoadSpectrumIfPresent(clutchSpectrumRed));
    }

    private void append(@NotNull final TestData existingTestdata, @NotNull final TestData testDataToAppend)
            throws IOException {

        HashMap<String, DigitalTwin> digitalTwins = Stream.concat(
                        existingTestdata.getDigitalTwins().stream(),
                        testDataToAppend.getDigitalTwins().stream())
                .collect(Collectors.toMap(DigitalTwin::getCatenaXId,
                        Function.identity(), (prev, next) -> next, HashMap::new));

        existingTestdata.setDigitalTwins(new ArrayList<>(digitalTwins.values()));
    }

    private String replacePlaceholders(final String originalString) {
        return originalString.replace("{timeStamp}", "2023-03-28T00:00:00.00Z")
                .replace("{genericTargetDate}", "2023-03-28T00:00:00.00Z");
    }
}
