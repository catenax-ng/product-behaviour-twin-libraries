package net.catena_x.btp.libraries.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
        } catch (final Exception exception) {}

        try {
            return Instant.parse(value + "Z");
        } catch (final Exception exception) {}

        try {
            final LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date.atStartOfDay(ZoneId.of("UTC")).toInstant();
        } catch (final Exception exception) {
            return null;
        }
    }
}
