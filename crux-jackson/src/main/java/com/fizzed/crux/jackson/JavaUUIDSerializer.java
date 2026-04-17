package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.UUID;

public class JavaUUIDSerializer extends JsonSerializer<UUID> implements ContextualSerializer {

    private final JavaUUIDStyle style;

    public JavaUUIDSerializer() {
        this(JavaUUIDStyle.DEFAULT);
    }

    public JavaUUIDSerializer(JavaUUIDStyle style) {
        this.style = style;
    }


    @Override
    public void serialize(UUID value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        final String uuidStr = value.toString();

        switch (this.style) {
            case STRIPPED:
                gen.writeString(uuidStr.replace("-", ""));
                break;
            case DEFAULT:
            default:
                gen.writeString(uuidStr);
                break;
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property != null) {
            JavaUUIDFormat format = property.getAnnotation(JavaUUIDFormat.class);
            if (format != null) {
                // Return a new instance configured with the specific enum value
                return new JavaUUIDSerializer(format.value());
            }
        }
        return this; // Use default if no annotation is present
    }

}