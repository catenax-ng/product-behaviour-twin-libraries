package net.catena_x.btp.libraries.util.database.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public class DAOJsonConverter<T_DTO> extends DAOConverter<String, T_DTO> {
    final private ObjectReader objectReader;
    final private ObjectWriter objectWriter;

    public DAOJsonConverter(final TypeReference type) {
        final ObjectMapper objectMapper = ObjectMapperFactoryBtp.createObjectMapper();
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
