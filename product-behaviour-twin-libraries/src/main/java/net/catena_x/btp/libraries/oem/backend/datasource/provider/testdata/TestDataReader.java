package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class TestDataReader {
    private ObjectMapper objectMapper = null;

    public TestData loadFromFile(@NotNull final Path filename) throws DataProviderException {
        intObjectManager();

        try {
            return objectMapper.readValue(new File(filename.toString()), TestData.class);
        } catch (IOException exception) {
            throw new DataProviderException("Error while opening testdata file!", exception);
        }
    }

    private void intObjectManager() {
        if(objectMapper != null) {
            return;
        }

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
}
