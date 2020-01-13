package com.fizzed.crux.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Objects;

/**
 * Module that helps deserialize enums accepting any case, while letting you
 * control how they are serialized (upper, lower, or retain).
 * 
 * @author jjlauer
 */
public class EnumStrategyModule extends SimpleModule {

    static public enum SerializeStrategy {
        UPPER_CASE,
        LOWER_CASE,
        RETAIN_CASE
    }
    
    static public enum DeserializeStrategy {
        UPPER_CASE,
        LOWER_CASE,
        IGNORE_CASE
    }
    
    private final SerializeStrategy serializeStrategy;
    private final DeserializeStrategy deserializeStrategy;

    public EnumStrategyModule() {
        this(SerializeStrategy.LOWER_CASE, DeserializeStrategy.IGNORE_CASE);
    }

    public EnumStrategyModule(SerializeStrategy serializeStrategy, DeserializeStrategy deserializeStrategy) {
        Objects.requireNonNull(serializeStrategy, "serialize strategy was null");
        Objects.requireNonNull(deserializeStrategy, "deserialize strategy was null");
        
        this.serializeStrategy = serializeStrategy;
        this.deserializeStrategy = deserializeStrategy;

        this.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<Enum> modifyEnumDeserializer(
                    DeserializationConfig config,
                    final JavaType type,
                    BeanDescription beanDesc,
                    final JsonDeserializer<?> deserializer) {
                
                return new JsonDeserializer<Enum>() {
                    @Override
                    public Enum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        final String value = jp.getValueAsString();
                        
                        if (value == null || value.isEmpty()) {
                            return null;
                        }
                        
                        Class<? extends Enum> rawClass = (Class<Enum<?>>) type.getRawClass();
                        
                        boolean ignoreUnknown = false;
                        JsonIgnoreProperties ignoreProps = rawClass.getAnnotation(JsonIgnoreProperties.class);
                        if (ignoreProps != null) {
                            ignoreUnknown = ignoreProps.ignoreUnknown();
                        }
                        
                        switch (EnumStrategyModule.this.deserializeStrategy) {
                            case IGNORE_CASE:
                                for (Enum en : rawClass.getEnumConstants()) {
                                    if (en.name().equalsIgnoreCase(value)) {
                                        return en;
                                    }
                                }
                                break;
                            case UPPER_CASE:
                                for (Enum en : rawClass.getEnumConstants()) {
                                    if (en.name().toUpperCase().equals(value)) {
                                        return en;
                                    }
                                }
                                break;
                            case LOWER_CASE:
                                for (Enum en : rawClass.getEnumConstants()) {
                                    if (en.name().toLowerCase().equals(value)) {
                                        return en;
                                    }
                                }
                                break;
                        }
                        
                        if (!ignoreUnknown) {
                            throw new UnrecognizedPropertyException(
                                jp, "No enum constant " + rawClass.getCanonicalName() + "." + value,
                                JsonLocation.NA, rawClass, "value", null);
                        }
                        
                        // otherwise, ignore and return null
                        return null;
                    }
                };
            }
        });

        this.addSerializer(Enum.class,
                new StdSerializer<Enum>(Enum.class) {
                    
            @Override
            public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                switch (EnumStrategyModule.this.serializeStrategy) {
                    case LOWER_CASE:
                        jgen.writeString(value.name().toLowerCase());
                        break;
                    case UPPER_CASE:
                        jgen.writeString(value.name().toUpperCase());
                        break;
                    case RETAIN_CASE:
                        jgen.writeString(value.name());
                        break;
                }
            }
        });
    }

}