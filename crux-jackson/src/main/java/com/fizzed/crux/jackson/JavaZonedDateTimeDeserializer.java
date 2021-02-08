package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import static com.fizzed.crux.jackson.JavaTimePlusModule.TZ_UTC;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.MILLIS;

public class JavaZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    private final boolean strict;
    private final ZoneId zoneId;
    private final ChronoUnit truncatedTo;
    
    public JavaZonedDateTimeDeserializer() {
        this(true, TZ_UTC, MILLIS);
    }

    public JavaZonedDateTimeDeserializer(
            boolean strict,
            ZoneId zoneId,
            ChronoUnit truncatedTo) {
        
        this.strict = strict;
        this.zoneId = zoneId;
        this.truncatedTo = truncatedTo;
    }

    @Override
    public ZonedDateTime deserialize(
            JsonParser parser,
            DeserializationContext context) throws IOException, JsonProcessingException {
        
        String s = parser.getText();
        
        if (s == null) {
            return null;
        }
        
        final ZonedDateTime raw;
        final ZonedDateTime zoned;
        final ZonedDateTime truncated;
        try {
            raw = ZonedDateTime.parse(s);
            
            zoned = raw.withZoneSameInstant(this.zoneId);

            truncated = zoned.truncatedTo(this.truncatedTo);
        }
        catch (Exception e) {
            throw new IOException("Unable to deserialize '" + s + "' into a datetime: " + e.getMessage());
        }
            
        // if they don't match and in strict mode, then we've got an issue
        if (strict && !raw.equals(truncated)) {
            throw new IOException("Unable to deserialize '" + s + "' into a datetime (invalid precision)");
        }

        return truncated;
    }
    
}