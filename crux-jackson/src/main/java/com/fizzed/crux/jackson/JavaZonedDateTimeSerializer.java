package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import static com.fizzed.crux.jackson.JavaTimePlusModule.TZ_UTC;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JavaZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

    private final DateTimeFormatter formatter;
    
    public JavaZonedDateTimeSerializer() {
        this.formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .withZone(TZ_UTC);
    }

    @Override
    public void serialize(
            final ZonedDateTime zonedDateTime,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        
        final String s = formatter.format(zonedDateTime);
        jsonGenerator.writeString(s);
    }
    
}