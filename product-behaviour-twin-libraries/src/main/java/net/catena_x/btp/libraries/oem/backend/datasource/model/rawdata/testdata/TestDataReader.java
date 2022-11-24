package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.json.TimeStampDeserializer;
import net.catena_x.btp.libraries.util.json.TimeStampSerializer;
import javax.validation.constraints.NotNull;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;

@Component
public class TestDataReader {
    private ObjectMapper objectMapper = null;

    public TestData loadFromFile(@NotNull final Path filename) throws DataProviderException {
        intObjectManager();

        return loadFromFiles(filename, null, null, null);
    }

    public TestData loadFromFiles(@NotNull final Path filename, @Nullable final Path clutchSpectrumGreen,
                                  @Nullable final Path clutchSpectrumYellow, @Nullable final Path clutchSpectrumRed)
            throws DataProviderException {

        intObjectManager();

        try {
            final TestData testData = objectMapper.readValue(new File(filename.toString()), TestData.class);
            extendTestData(testData, clutchSpectrumGreen, clutchSpectrumYellow, clutchSpectrumRed);
            return testData;

        } catch (IOException exception) {
            throw new DataProviderException("Error while opening testdata file(s)!", exception);
        }
    }

    private ClassifiedLoadSpectrum loadClutcLoadSpectrumIfPresent(@Nullable final Path clutchSpectrum)
            throws IOException {

        if(clutchSpectrum == null) {
            return null;
        }

        return objectMapper.readValue(new File(clutchSpectrum.toString()), ClassifiedLoadSpectrum.class);
    }

    private void intObjectManager() {
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

    private void extendTestData(@NotNull TestData testData, @Nullable final Path clutchSpectrumGreen,
                                @Nullable final Path clutchSpectrumYellow, @Nullable final Path clutchSpectrumRed)
            throws IOException {

        testData.setClutchLoadSpectrumGreen(loadClutcLoadSpectrumIfPresent(clutchSpectrumGreen));
        testData.setClutchLoadSpectrumYellow(loadClutcLoadSpectrumIfPresent(clutchSpectrumYellow));
        testData.setClutchLoadSpectrumRed(loadClutcLoadSpectrumIfPresent(clutchSpectrumRed));
    }
}
