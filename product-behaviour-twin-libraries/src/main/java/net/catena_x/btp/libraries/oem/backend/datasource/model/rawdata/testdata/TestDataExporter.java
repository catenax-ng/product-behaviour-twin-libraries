package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.CatenaXIdToDigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.VehicleDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.hleper.ContentChecker;
import net.catena_x.btp.libraries.util.json.TimeStampDeserializer;
import net.catena_x.btp.libraries.util.json.TimeStampSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
public class TestDataExporter {
    @Autowired VehicleDataLoader vehicleDataLoader;

    private ObjectMapper objectMapper = null;

    public void export(@NotNull final TestData testData, @NotNull final Path filename) throws DataProviderException {
        intObjectManager();

        try {
            objectMapper.writeValue(new File(filename.toString()), testData);
        } catch (IOException exception) {
            throw new DataProviderException(exception);
        }
    }

    public void exportLimited(@NotNull final TestDataCategorized testDataCategorized,
                              @NotNull final Path baseFilename,
                              @NotNull final String fileextension,
                              @NotNull final int maxVehicleCountPerFile) throws DataProviderException {
        intObjectManager();

        if(ContentChecker.isNullOrEmpty(testDataCategorized.getDigitalTwinsVehicles())) {
            return;
        }

        try {
            final Collection<DigitalTwin> vehicleTwins = testDataCategorized.getDigitalTwinsVehicles().values();
            final int fileCount = ((vehicleTwins.size() - 1) / maxVehicleCountPerFile) + 1;

            final TestData testDataExport = new TestData();
            testDataExport.setDigitalTwins(new ArrayList<>(maxVehicleCountPerFile));

            int count = 0;
            int fileIndex = 1;
            for (final DigitalTwin vehicleTwin: vehicleTwins) {
                testDataExport.getDigitalTwins().add(vehicleTwin);
                testDataExport.getDigitalTwins().add(
                        testDataCategorized.getGearboxTwinFromVehicleTwinMustExists(vehicleTwin));

                ++count;
                if(count >= maxVehicleCountPerFile) {
                    writeAndResetTestData(buildExportFilename(baseFilename, fileextension, fileIndex, fileCount),
                            maxVehicleCountPerFile, testDataExport);

                    count = 0;
                    ++fileIndex;
                }
            }

            if(count > 0) {
                writeAndResetTestData(buildExportFilename(baseFilename, fileextension, fileIndex, fileCount),
                        maxVehicleCountPerFile, testDataExport);
            }
        } catch (IOException exception) {
            throw new DataProviderException(exception);
        }
    }

    private void writeAndResetTestData(@NotNull final String filename, @NotNull final int maxVehicleCountPerFile,
                                       @NotNull final TestData testDataExport) throws IOException {
        objectMapper.writeValue(new File(filename), testDataExport);
        testDataExport.setDigitalTwins(new ArrayList<>(maxVehicleCountPerFile));
    }

    private String buildExportFilename(@NotNull final Path baseFilename, @NotNull final String fileextension,
                                       @NotNull final int fileIndex, @NotNull final int fileCount) {
        return baseFilename.toString() + "_" + String.valueOf(fileIndex)
                + "_of_" + String.valueOf(fileCount) + "." + fileextension;
    }

    private synchronized void intObjectManager() {
        if(objectMapper != null) {
            return;
        }

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule timestampSerializerModule = new SimpleModule();
        timestampSerializerModule.addSerializer(Instant.class, new TimeStampSerializer());
        objectMapper.registerModule(timestampSerializerModule);

        SimpleModule timestampDeserializerModule = new SimpleModule();
        timestampDeserializerModule.addDeserializer(Instant.class, new TimeStampDeserializer());
        objectMapper.registerModule(timestampDeserializerModule);
    }

}
