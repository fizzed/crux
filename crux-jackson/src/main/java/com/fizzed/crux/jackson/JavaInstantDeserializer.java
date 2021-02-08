package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.MILLIS;

public class JavaInstantDeserializer extends JsonDeserializer<Instant> {

    private final boolean strict;
    private final ChronoUnit truncatedTo;
    
    public JavaInstantDeserializer() {
        this(true, MILLIS);
    }
    
    public JavaInstantDeserializer(
            boolean strict,
            ChronoUnit truncatedTo) {
        
        this.strict = strict;
        this.truncatedTo = truncatedTo;
    }

    @Override
    public Instant deserialize(
            JsonParser parser,
            DeserializationContext context) throws IOException, JsonProcessingException {
        
        String s = parser.getText();
        
        if (s == null) {
            return null;
        }
        
        final Instant raw;
        final Instant truncated;
        try {
            raw = Instant.parse(s);
            
            // truncate it now...
            truncated = raw.truncatedTo(this.truncatedTo);
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