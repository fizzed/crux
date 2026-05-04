package com.fizzed.crux.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import java.io.IOException;

public class CaseInsensitiveTypeIdResolver extends TypeIdResolverBase {

    protected JavaType baseType;

    @Override
    public void init(JavaType baseType) {
        this.baseType = baseType;
    }

    @Override
    public String idFromValue(Object value) {
        return idFromValueAndType(value, value.getClass());
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        // For serialization, we use the simple name or a standard mapping.
        // If you want serialization to also be forced uppercase:
        return suggestedType.getSimpleName().toUpperCase();
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        // 1. Normalize the incoming JSON 'type' value to uppercase
        String normalizedId = (id != null) ? id.toUpperCase() : null;

        // 2. Use Jackson's built-in subtype resolution logic.
        // This looks at the @JsonSubTypes on the 'baseType' we stored in init().
        return context.resolveSubType(baseType, normalizedId);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.NAME;
    }

}