package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeWithMsSerializer extends JsonSerializer<ZonedDateTime> {

    private final DateTimeFormatter formatter;
    
    public ZonedDateTimeWithMsSerializer() {
        this.formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .withZone(ZoneId.of("UTC"));
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