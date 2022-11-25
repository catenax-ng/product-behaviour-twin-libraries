package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.json.TimeStampDeserializer;
import net.catena_x.btp.libraries.util.json.TimeStampSerializer;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;

@Component
public class TestDataExporter {
    private ObjectMapper objectMapper = null;

    public void export(@NotNull final TestData testData, @NotNull final Path filename) throws DataProviderException {
        intObjectManager();

        try {
            objectMapper.writeValue(new File(filename.toString()), testData);
        } catch (IOException exception) {
            throw new DataProviderException(exception);
        }
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
