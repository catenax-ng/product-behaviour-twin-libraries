package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.LoadSpectrumType;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.bamm.util.DigitalTwinConverter;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class TestDataExporter {
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;
    @Autowired DigitalTwinConverter digitalTwinConverter;

    public void export(@NotNull final TestDataCategorized testDataCategorized, @NotNull final Path filename,
                       @NotNull final boolean useOldBammVersion,
                       @NotNull final boolean onlyfirstLoadSpectrumPerType,
                       @NotNull final boolean exportDamageAndRul) throws DataProviderException {
        if(DataHelper.isNullOrEmpty(testDataCategorized.getDigitalTwinsVehicles())) {
            return;
        }

        final TestDataCategorized testDataCategorizedCopy = getCopy(testDataCategorized);

        final Collection<DigitalTwin> vehicleTwins = testDataCategorizedCopy.getDigitalTwinsVehicles().values();
        final TestData testDataExport = new TestData();
        testDataExport.setDigitalTwins(new ArrayList<>(vehicleTwins.size()));

        for (final DigitalTwin vehicleTwin: vehicleTwins) {
            testDataExport.getDigitalTwins().add(vehicleTwin);
            testDataExport.getDigitalTwins().add(
                    testDataCategorizedCopy.getGearboxTwinFromVehicleTwinMustExists(vehicleTwin));
        }

        try {
            export(testDataExport, filename.toString(), useOldBammVersion,
                    onlyfirstLoadSpectrumPerType, exportDamageAndRul);
        } catch (final IOException exception) {
            throw new DataProviderException(exception);
        }
    }

    public void exportLimited(@NotNull final TestDataCategorized testDataCategorized,
                              @NotNull final Path baseFilename,
                              @NotNull final String fileextension,
                              @NotNull final int maxVehicleCountPerFile,
                              @NotNull final boolean useOldBammVersion,
                              @NotNull final boolean onlyFirstLoadSpectrumPerType,
                              @NotNull final boolean exportDamageAndRul) throws DataProviderException {
        if(DataHelper.isNullOrEmpty(testDataCategorized.getDigitalTwinsVehicles())) {
            return;
        }

        final TestDataCategorized testDataCategorizedCopy = getCopy(testDataCategorized);

        try {
            final Collection<DigitalTwin> vehicleTwins = testDataCategorizedCopy.getDigitalTwinsVehicles().values();
            final int fileCount = ((vehicleTwins.size() - 1) / maxVehicleCountPerFile) + 1;

            final TestData testDataExport = new TestData();
            testDataExport.setDigitalTwins(new ArrayList<>(maxVehicleCountPerFile));

            int count = 0;
            int fileIndex = 1;
            for (final DigitalTwin vehicleTwin: vehicleTwins) {
                testDataExport.getDigitalTwins().add(vehicleTwin);
                testDataExport.getDigitalTwins().add(
                        testDataCategorizedCopy.getGearboxTwinFromVehicleTwinMustExists(vehicleTwin));

                ++count;
                if(count >= maxVehicleCountPerFile) {
                    writeAndResetTestData(buildExportFilename(baseFilename, fileextension, fileIndex, fileCount),
                            maxVehicleCountPerFile, testDataExport, useOldBammVersion,
                            onlyFirstLoadSpectrumPerType, exportDamageAndRul);

                    count = 0;
                    ++fileIndex;
                }
            }

            if(count > 0) {
                writeAndResetTestData(buildExportFilename(baseFilename, fileextension, fileIndex, fileCount),
                        maxVehicleCountPerFile, testDataExport, useOldBammVersion,
                        onlyFirstLoadSpectrumPerType, exportDamageAndRul);
            }
        } catch (final IOException exception) {
            throw new DataProviderException(exception);
        }
    }

    private void writeAndResetTestData(@NotNull final String filename, @NotNull final int maxVehicleCountPerFile,
                                       @NotNull final TestData testDataExport,
                                       @NotNull final boolean useOldBammVersion,
                                       @NotNull final boolean onlyFirstLoadSpectrumPerType,
                                       @NotNull final boolean exportDamageAndRul)
            throws IOException, DataProviderException {
        export(testDataExport, filename, useOldBammVersion, onlyFirstLoadSpectrumPerType, exportDamageAndRul);
        testDataExport.setDigitalTwins(new ArrayList<>(maxVehicleCountPerFile));
    }

    private String buildExportFilename(@NotNull final Path baseFilename, @NotNull final String fileextension,
                                       @NotNull final int fileIndex, @NotNull final int fileCount) {
        return baseFilename.toString() + "_" + String.valueOf(fileIndex)
                + "_of_" + String.valueOf(fileCount) + "." + fileextension;
    }

    private void export(@NotNull final TestData testData, @NotNull final String filename,
                        @NotNull final boolean useOldBammVersion,
                        @NotNull final boolean onlyFirstLoadSpectrumPerType,
                        @NotNull final boolean exportDamageAndRul) throws IOException, DataProviderException {
        if(onlyFirstLoadSpectrumPerType) {
            removeUnusedLoadSpectra(testData);
        }

        if(!exportDamageAndRul) {
            removeDamageAndRuLAspects(testData);
        }

        convertBammVersion(testData, useOldBammVersion);

        final File destinationFile = new File(filename);
        if(useOldBammVersion) {
            exportOldBamm(destinationFile, testData);
        }
        else {
            exportNewBamm(destinationFile, testData);
        }
    }

    private void exportNewBamm(final File destinationFile, @NotNull final TestData testData) throws IOException {
        objectMapper.writeValue(destinationFile, testData);
    }

    private void exportOldBamm(final File destinationFile, @NotNull final TestData testData) throws IOException {
        final String testDataAsString =
                objectMapper.writeValueAsString(testData).replace("Spectrum", "Collective");

        final FileWriter writer = new FileWriter(destinationFile);
        writer.write(testDataAsString);
        writer.flush();
        writer.close();
    }

    private void removeUnusedLoadSpectra(@NotNull final TestData testData) throws DataProviderException {
        for (final DigitalTwin digitalTwin : testData.getDigitalTwins()) {
            removeUnusedLoadSpectra(digitalTwin);
        }
    }

    private void removeUnusedLoadSpectra(@NotNull final DigitalTwin digitalTwin) throws DataProviderException {
        final List<ClassifiedLoadSpectrum> loadSpectra = digitalTwin.getClassifiedLoadSpectra();

        if(loadSpectra == null) {
            return;
        }

        int pos = 1;
        while(pos < loadSpectra.size()) {
            final LoadSpectrumType type = getLoadSpectrumType(loadSpectra.get(pos));
            for (int i = 0; i < pos; i++) {
                if(getLoadSpectrumType(loadSpectra.get(i)) == type) {
                    loadSpectra.remove(pos);
                    /* Resume next turn with same position (after for loop, ++pos is called). */
                    --pos;
                    break;
                }
            }

            ++pos;
        }
    }

    private void removeDamageAndRuLAspects(@NotNull final TestData testData) throws DataProviderException {
        for (final DigitalTwin digitalTwin : testData.getDigitalTwins()) {
            removeDamageAndRuLAspects(digitalTwin);
        }
    }

    private void removeDamageAndRuLAspects(@NotNull final DigitalTwin digitalTwin) throws DataProviderException {
        digitalTwin.setDamages(null);
        digitalTwin.setRemainingUsefulLifes(null);
    }

    private void convertBammVersion(@NotNull final TestData testData,
                                    @NotNull final boolean useOldBammVersion) throws DataProviderException {
        try {
            for (final DigitalTwin digitalTwin : testData.getDigitalTwins()) {
                if (useOldBammVersion) {
                    digitalTwinConverter.convertToOldBAMM(digitalTwin);
                } else {
                    digitalTwinConverter.convertToNewBAMM(digitalTwin);
                }

                removeDamageAndRuLAspects(digitalTwin);
            }
        } catch(final BtpException exception) {
            throw new DataProviderException(exception);
        }
    }

    private LoadSpectrumType getLoadSpectrumType(@NotNull final ClassifiedLoadSpectrum loadSpectrum)
            throws DataProviderException {
        assertMetadata(loadSpectrum);
        return loadSpectrum.getMetadata().getComponentDescription();
    }

    private void assertMetadata(@NotNull final ClassifiedLoadSpectrum loadSpectrum) throws DataProviderException {
        if(loadSpectrum.getMetadata() == null) {
            throw new DataProviderException("Metadata in load spectrum is null!");
        }
    }

    private TestData getCopy(@NotNull final TestData testData) throws DataProviderException {
        if(testData == null) {
                return null;
        }

        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(testData), TestData.class);
        } catch(final JsonProcessingException exception) {
            throw new DataProviderException(exception);
        }
    }

    private TestDataCategorized getCopy(@NotNull final TestDataCategorized testData) throws DataProviderException {
        if(testData == null) {
            return null;
        }

        try {
            final TestDataCategorized testDataCategorized =
                    objectMapper.readValue(objectMapper.writeValueAsString(testData), TestDataCategorized.class);

            testDataCategorized.initAutowiredFrom(testData);

            return testDataCategorized;
        } catch(final JsonProcessingException exception) {
            throw new DataProviderException(exception);
        }
    }
}
