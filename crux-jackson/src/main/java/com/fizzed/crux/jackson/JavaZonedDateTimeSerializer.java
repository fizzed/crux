package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class JavaZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

    static private final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(JavaTimeModuleBuilder.TZ_UTC);

    private final JavaTimeFormat format;
    
    public JavaZonedDateTimeSerializer() {
        this(JavaTimeFormat.ISO_8601);
    }

    public JavaZonedDateTimeSerializer(JavaTimeFormat format) {
        Objects.requireNonNull(format, "format cannot be null");
        this.format = format;
    }

    @Override
    public void serialize(final ZonedDateTime zonedDateTime, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        switch (this.format) {
            case EPOCH_MILLIS:
                jsonGenerator.writeNumber(zonedDateTime.toInstant().toEpochMilli());
                break;
            case EPOCH_SECS:
                jsonGenerator.writeNumber(zonedDateTime.toEpochSecond());
                break;
            case ISO_8601:
                final String s = ISO_8601_FORMATTER.format(zonedDateTime);
                jsonGenerator.writeString(s);
                break;
            default:
                throw new IOException("Unsupported JavaTimeFormat (was support not added?): " + this.format);
        }
    }
    
}