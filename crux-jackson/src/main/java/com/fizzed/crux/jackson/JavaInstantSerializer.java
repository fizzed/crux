package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class JavaInstantSerializer extends JsonSerializer<Instant> {

    static private final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(JavaTimeModuleBuilder.TZ_UTC);

    private final JavaTimeFormat format;

    /**
     * Creates an instance of {@code JavaInstantSerializer} using the default {@link JavaTimeFormat#ISO_8601}.
     * The serializer will format {@link Instant} values into strings following the ISO 8601 standard.
     */
    public JavaInstantSerializer() {
        this(JavaTimeFormat.ISO_8601);
    }

    /**
     * Constructs a {@code JavaInstantSerializer} with the specified {@link JavaTimeFormat}.
     * This serializer will format {@link Instant} values based on the provided time format.
     *
     * @param format the {@link JavaTimeFormat} to use for formatting {@link Instant} values,
     *               determining how the serialized output will appear.
     */
    public JavaInstantSerializer(JavaTimeFormat format) {
        Objects.requireNonNull(format, "format cannot be null");
        this.format = format;
    }

    @Override
    public void serialize(final Instant instant, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        switch (this.format) {
            case EPOCH_MILLIS:
                jsonGenerator.writeNumber(instant.toEpochMilli());
                break;
            case EPOCH_SECS:
                jsonGenerator.writeNumber(instant.getEpochSecond());
                break;
            case ISO_8601:
                final String s = ISO_8601_FORMATTER.format(instant);
                jsonGenerator.writeString(s);
                break;
            default:
                throw new IOException("Unsupported JavaTimeFormat (was support not added?): " + this.format);
        }
    }
    
}