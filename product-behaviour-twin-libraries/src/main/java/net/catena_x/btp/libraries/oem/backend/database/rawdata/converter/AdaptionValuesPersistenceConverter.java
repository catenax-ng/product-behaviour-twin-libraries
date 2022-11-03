package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AdaptionValuesPersistenceConverter implements AttributeConverter<List<double[]>, String> {
    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<double[]> adaptionValues) {
        try {
            return objectMapper.writeValueAsString(adaptionValues);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public List<double[]> convertToEntityAttribute(String adaptionValuesJSON) {

        double[][] adaptionValues = null;
        try {
            return objectMapper.readValue(adaptionValuesJSON, ArrayList.class);
        } catch (final IOException e) {
            return null;
        }
    }
}
