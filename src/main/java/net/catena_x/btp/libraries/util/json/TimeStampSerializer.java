package net.catena_x.btp.libraries.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;

public class TimeStampSerializer extends StdSerializer<Instant> {

    public TimeStampSerializer(Class<Instant> t) {
        super(t);
    }

    public TimeStampSerializer() {
        this(null);
    }

    @Override
    public void serialize(Instant value, JsonGenerator generator, SerializerProvider serializerProvider)
            throws IOException {
        generator.writeString(value.toString());
    }
}