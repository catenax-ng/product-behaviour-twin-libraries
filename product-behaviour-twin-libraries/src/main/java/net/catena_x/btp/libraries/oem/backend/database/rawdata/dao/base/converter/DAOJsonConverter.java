package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.catena_x.btp.libraries.util.TimeStampDeserializer;
import net.catena_x.btp.libraries.util.TimeStampSerializer;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Instant;

public class DAOJsonConverter<T_DTO> extends DAOConverter<String, T_DTO> {
    final private ObjectReader objectReader;
    final private ObjectWriter objectWriter;

    public DAOJsonConverter(final TypeReference type) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule timestampSerializerModule = new SimpleModule();
        timestampSerializerModule.addSerializer(Instant.class, new TimeStampSerializer());
        objectMapper.registerModule(timestampSerializerModule);

        SimpleModule timestampDeserializerModule = new SimpleModule();
        timestampDeserializerModule.addDeserializer(Instant.class, new TimeStampDeserializer());
        objectMapper.registerModule(timestampDeserializerModule);

        objectReader = objectMapper.readerFor(type);
        objectWriter = objectMapper.writerFor(type);
    }

    protected T_DTO toDTOSourceExists(@NotNull String source) {
        try {
            return objectReader.readValue(source);
        } catch (final IOException e) {
            return null;
        }
    }

    protected String toDAOSourceExists(@NotNull T_DTO source) {
        try {
            return objectWriter.writeValueAsString(source);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }
}
