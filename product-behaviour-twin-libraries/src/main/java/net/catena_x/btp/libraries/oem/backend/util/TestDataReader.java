package net.catena_x.btp.libraries.oem.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TestDataReader {
    public TestData loadFromFile(@NotNull Path filename) throws IOException {
        File inputFile = new File(filename.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.readValue(inputFile, TestData.class);
    }

    /*public Map<String, Object> loadFromFile(@NotNull Path filename) throws IOException {
        // https://stackoverflow.com/questions/443499/convert-json-to-map
        File inputFile = new File(filename.toString());
        return new ObjectMapper().readValue(inputFile, HashMap.class);
    }*/
}
