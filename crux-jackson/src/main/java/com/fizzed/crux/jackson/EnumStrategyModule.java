package com.fizzed.crux.jackson;

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

//    private final EnumSerializeStrategy serializeStrategy;
//    private final EnumDeserializeStrategy deserializeStrategy;
//    private EnumGlobalUnknownHandler globalUnknownEnumHandler;
    private EnumMapper mapper;
    
    public EnumStrategyModule() {
        this(EnumSerializeStrategy.LOWER_CASE, EnumDeserializeStrategy.IGNORE_CASE);
    }
    
    public EnumStrategyModule(
            EnumSerializeStrategy serializeStrategy,
            EnumDeserializeStrategy deserializeStrategy) {
        
        Objects.requireNonNull(serializeStrategy, "serialize strategy was null");
        Objects.requireNonNull(deserializeStrategy, "deserialize strategy was null");
        
        this.mapper = new EnumMapper(serializeStrategy, deserializeStrategy);

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
                        
                        final Class<? extends Enum> rawClass = (Class<Enum<?>>) type.getRawClass();
                        
                        try {
                            return mapper.deserialize(value, rawClass);
                        }
                        catch (IOException e) {
                            throw new UnrecognizedPropertyException(
                                jp, "No enum constant " + rawClass.getCanonicalName() + "." + value,
                                JsonLocation.NA, rawClass, "value", null);
                        }
                    }
                };
            }
        });

        this.addSerializer(Enum.class,
                new StdSerializer<Enum>(Enum.class) {
                    
            @Override
            public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                
                final String s = mapper.serialize(value);
                
                jgen.writeString(s);
            }
            
        });
    }

    public EnumMapper getMapper() {
        return mapper;
    }

    public EnumStrategyModule setGlobalUnknownEnumHandler(
            EnumGlobalUnknownHandler globalUnknownEnumHandler) {
        
        this.mapper.setGlobalUnknownEnumHandler(globalUnknownEnumHandler);
        return this;
    }

}