package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import static com.fizzed.crux.jackson.JavaTimePlusModule.TZ_UTC;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class JavaInstantSerializer extends JsonSerializer<Instant> {

    private final DateTimeFormatter formatter;
    
    public JavaInstantSerializer() {
        this.formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .withZone(TZ_UTC);
    }

    @Override
    public void serialize(
            final Instant instant,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        
        final String s = formatter.format(instant);
        jsonGenerator.writeString(s);
    }
    
}