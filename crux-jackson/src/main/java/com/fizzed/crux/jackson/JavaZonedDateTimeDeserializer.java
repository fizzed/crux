package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class JavaZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    private final JavaTimeFormat format;
    private final boolean strict;
    private final ZoneId zoneId;
    private final ChronoUnit truncatedTo;
    
    public JavaZonedDateTimeDeserializer() {
        this(JavaTimeFormat.ISO_8601, true, JavaTimeModuleBuilder.TZ_UTC, ChronoUnit.MILLIS);
    }

    public JavaZonedDateTimeDeserializer(boolean strict, ZoneId zoneId, ChronoUnit truncatedTo) {
        this(JavaTimeFormat.ISO_8601, strict, zoneId, truncatedTo);
    }

    public JavaZonedDateTimeDeserializer(JavaTimeFormat format, boolean strict, ZoneId zoneId, ChronoUnit truncatedTo) {
        this.format = format;
        this.strict = strict;
        this.zoneId = zoneId;
        this.truncatedTo = truncatedTo;
    }

    @Override
    public ZonedDateTime deserialize(
            JsonParser parser,
            DeserializationContext context) throws IOException, JsonProcessingException {
        
        String s = parser.getText();
        
        if (s == null || s.isEmpty()) {
            return null;
        }
        
        final ZonedDateTime raw;
        final ZonedDateTime zoned;
        try {
            switch (format) {
                case EPOCH_MILLIS: {
                    long v = Long.parseLong(s);
                    raw = Instant.ofEpochMilli(v).atZone(this.zoneId);
                    break;
                }
                case EPOCH_SECS: {
                    long v = Long.parseLong(s);
                    raw = Instant.ofEpochSecond(v).atZone(this.zoneId);
                    break;
                }
                case ISO_8601:
                    raw = ZonedDateTime.parse(s);
                    break;
                default:
                    throw new IOException("Unsupported JavaTimeFormat (was support not added?): " + this.format);
            }

            zoned = raw.withZoneSameInstant(this.zoneId);
        }
        catch (Exception e) {
            throw new IOException("Unable to deserialize '" + s + "' into a datetime: " + e.getMessage());
        }

        final ZonedDateTime truncated = zoned.truncatedTo(this.truncatedTo);

        // if they don't match and in strict mode, then we've got an issue
        if (strict && !raw.equals(truncated)) {
            throw new IOException("Unable to deserialize '" + s + "' into a datetime (invalid precision)");
        }

        return truncated;
    }
    
}