package net.catena_x.btp.libraries.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class TimeStampDeserializer extends StdDeserializer<Instant> {

    public TimeStampDeserializer() {
        this(null);
    }

    public TimeStampDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        final String value = jsonParser.getValueAsString();

        try {
            return Instant.parse(value);
        } catch (Exception exception) {}

        try {
            return Instant.parse(value + "Z");
        } catch (Exception exception) {}

        try {
            return Instant.from( DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(value));
        } catch (Exception exception) {
            return null;
        }
    }
}
