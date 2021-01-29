package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeUtcDeserializer extends JsonDeserializer<ZonedDateTime> {

    private final ZoneId utcZone;
    
    public ZonedDateTimeUtcDeserializer() {
        this.utcZone = ZoneId.of("Z");
    }

    @Override
    public ZonedDateTime deserialize(
            JsonParser parser,
            DeserializationContext context) throws IOException, JsonProcessingException {
        
        String s = parser.getText();
        
        if (s == null) {
            return null;
        }
        
        try {
            return ZonedDateTime.parse(s).withZoneSameInstant(utcZone);
        }
        catch (Exception e) {
            throw new IOException("Unable to deserialize '" + s + "' into a datetime");
        }
    }
    
}