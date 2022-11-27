package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TestDataReader {
    @Autowired private ObjectMapper objectMapper;

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
            append(existingTestdata, objectMapper.readValue(testDataJson, TestData.class));
        } catch (final IOException exception) {
            throw new DataProviderException("Error while reading testdata from json string!", exception);
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
}
