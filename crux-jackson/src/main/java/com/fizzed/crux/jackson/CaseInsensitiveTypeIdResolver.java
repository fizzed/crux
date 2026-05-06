package com.fizzed.crux.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CaseInsensitiveTypeIdResolver extends TypeIdResolverBase {

    private JavaType baseType;
    private final Map<String, JavaType> idToType = new HashMap<>();

    @Override
    public void init(JavaType baseType) {
        this.baseType = baseType;

        // Use the context/TypeFactory to look for @JsonSubTypes on the base class
        JsonSubTypes subTypes = baseType.getRawClass().getAnnotation(JsonSubTypes.class);
        if (subTypes != null) {
            for (JsonSubTypes.Type type : subTypes.value()) {
                // Map the name in UPPERCASE to the actual class
                JavaType javaType = TypeFactory.defaultInstance().constructType(type.value());
                idToType.put(type.name().toUpperCase(), javaType);
            }
        }
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        if (id == null) return null;

        // Normalize input to uppercase and look up in our pre-built map
        JavaType type = idToType.get(id.toUpperCase());

        if (type == null) {
            // Fallback: try to resolve via context if not in our @JsonSubTypes map
            return context.resolveSubType(baseType, id);
        }
        return type;
    }

    @Override
    public String idFromValue(Object value) {
        return idFromValueAndType(value, value.getClass());
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        // Find the name in our map that matches the class, or default to simple name
        return idToType.entrySet().stream()
            .filter(e -> e.getValue().getRawClass().equals(suggestedType))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(suggestedType.getSimpleName().toUpperCase());
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.NAME;
    }

}