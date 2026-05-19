package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class JavaInstantDeserializer extends JsonDeserializer<Instant> {

    private final JavaTimeFormat format;
    private final boolean strict;
    private final ChronoUnit truncatedTo;

    /**
     * Default constructor for the {@code JavaInstantDeserializer} class.
     * Initializes the deserializer with the default configuration, which uses:
     * <ul>
     * - {@code JavaTimeFormat.ISO_8601} as the time format.
     * - {@code true} for strict mode.
     * - {@code ChronoUnit.MILLIS} as the truncation unit.
     * </ul>
     *
     * This configuration ensures that ISO 8601 date-time strings are deserialized
     * to {@code Instant} objects with millisecond precision and strict validation
     * of input to prevent mismatches between raw and truncated precisions.
     */
    public JavaInstantDeserializer() {
        this(JavaTimeFormat.ISO_8601, true, ChronoUnit.MILLIS);
    }

    /**
     * Creates a new instance of the {@code JavaInstantDeserializer} class with the specified configuration.
     *
     * @param format specifies the time format to use when deserializing the input.
     *               Possible values are {@code JavaTimeFormat.ISO_8601}, {@code JavaTimeFormat.EPOCH_MILLIS},
     *               or {@code JavaTimeFormat.EPOCH_SECS}.
     * @param strict determines whether the deserialization process enforces strict validation.
     *               If {@code true}, deserialization will ensure that the parsed {@code Instant} matches
     *               the precision specified by {@code truncatedTo}, throwing an exception otherwise.
     * @param truncatedTo specifies the precision level for truncating the deserialized {@code Instant}.
     *                    Possible values are {@link ChronoUnit}, such as {@code MILLIS}, {@code SECONDS}, etc.
     */
    public JavaInstantDeserializer(JavaTimeFormat format, boolean strict, ChronoUnit truncatedTo) {
        Objects.requireNonNull(format, "format cannot be null");
        Objects.requireNonNull(truncatedTo, "truncatedTo cannot be null");
        this.format = format;
        this.strict = strict;
        this.truncatedTo = truncatedTo;
    }

    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        final Instant raw;
        try {
            switch (format) {
                case EPOCH_MILLIS: {
                    long v = parser.getLongValue();
                    raw = Instant.ofEpochMilli(v);
                    break;
                }
                case EPOCH_SECS: {
                    long v = parser.getLongValue();
                    raw = Instant.ofEpochSecond(v);
                    break;
                }
                case ISO_8601:
                    String s = parser.getText();
                    if (s == null || s.isEmpty()) {
                        return null;
                    }
                    raw = Instant.parse(s);
                    break;
                default:
                    throw new IOException("Unsupported JavaTimeFormat (was support not added?): " + this.format);
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Unable to deserialize '" + parser.getCurrentToken() + "' into a datetime: " + e.getMessage());
        }

        // truncate it now...
        final Instant truncated = raw.truncatedTo(this.truncatedTo);
            
        // if they don't match and in strict mode, then we've got an issue
        if (strict && !raw.equals(truncated)) {
            throw new IOException("Unable to deserialize '" + parser.getCurrentToken() + "' into a datetime (invalid precision)");
        }

        return truncated;
    }
    
}