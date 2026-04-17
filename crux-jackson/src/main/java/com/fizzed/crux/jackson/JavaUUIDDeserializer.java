package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.util.UUID;

public class JavaUUIDDeserializer extends JsonDeserializer<UUID> implements ContextualDeserializer {

    private final JavaUUIDStyle style;

    public JavaUUIDDeserializer() {
        this(JavaUUIDStyle.DEFAULT);
    }

    public JavaUUIDDeserializer(JavaUUIDStyle style) {
        this.style = style;
    }

    @Override
    public UUID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String value = p.getValueAsString();

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        // Re-insert hyphens to satisfy the standard UUID format
        final String uuidStr;

        if (style == JavaUUIDStyle.DEFAULT) {
            uuidStr = value;
        } else if (style == JavaUUIDStyle.STRIPPED) {
            if (value.length() != 32) {
                throw new IllegalArgumentException("MD5 hex string must be exactly 32 characters long");
            }

            uuidStr = value.substring(0, 8) + "-" +
                value.substring(8, 12) + "-" +
                value.substring(12, 16) + "-" +
                value.substring(16, 20) + "-" +
                value.substring(20, 32);
        } else {
            throw new IllegalArgumentException("Unknown UUID style: " + style);
        }

        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            return (UUID) ctxt.handleWeirdStringValue(UUID.class, value, "Invalid UUID format");
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        if (property != null) {
            final JavaUUIDFormat format = property.getAnnotation(JavaUUIDFormat.class);
            if (format != null) {
                // Return a version of the deserializer tailored to this field's annotation
                return new JavaUUIDDeserializer(format.value());
            }
        }
        return this; // Return default if no annotation is found
    }

}