package com.fizzed.crux.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Module that helps deserialize enums accepting any case, while letting you
 * control how they are serialized (upper, lower, or retain).
 * 
 * @author jjlauer
 */
public class EnumStrategyModule extends SimpleModule {

    final private EnumMapper mapper;
    
    public EnumStrategyModule() {
        this(EnumSerializeStrategy.LOWER_CASE, EnumDeserializeStrategy.IGNORE_CASE);
    }
    
    public EnumStrategyModule(
            EnumSerializeStrategy serializeStrategy,
            EnumDeserializeStrategy deserializeStrategy) {

        Objects.requireNonNull(serializeStrategy, "serialize strategy was null");
        Objects.requireNonNull(deserializeStrategy, "deserialize strategy was null");
        
        this.mapper = new EnumMapper(serializeStrategy, deserializeStrategy);
    }

    @Override
    public void setupModule(final SetupContext context) {
        context.addDeserializers(new PermissiveEnumDeserializers(this.mapper));

        context.addSerializers(new Serializers.Base() {
            @Override
            public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
                if (type.isEnumType()) {
                    return new StdSerializer<Enum>(Enum.class) {
                        @Override
                        public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                            final String s = mapper.serialize(value);
                            jgen.writeString(s);
                        }
                    };
                }
                return null;
            }
        });

        context.addKeySerializers(new Serializers.Base() {
            @Override
            public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
                if (type.isEnumType()) {
                    return new StdSerializer<Enum>(Enum.class) {
                        @Override
                        public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                            final String s = mapper.serialize(value);
                            jgen.writeFieldName(s);
                        }
                    };
                }
                return null;
            }
        });
    }

    public EnumMapper getMapper() {
        return mapper;
    }

    public EnumStrategyModule setNullOnUnknown(
            boolean nullOnUnknown) {
        
        this.mapper.setNullOnUnknown(nullOnUnknown);
        return this;
    }
    
    public EnumStrategyModule setGlobalUnknownEnumHandler(
            EnumGlobalUnknownHandler globalUnknownEnumHandler) {
        
        this.mapper.setGlobalUnknownEnumHandler(globalUnknownEnumHandler);
        return this;
    }

    // Technique learned from https://github.com/dropwizard/dropwizard/tree/master/dropwizard-jackson/src/main/java/io/dropwizard/jackson
    static private class PermissiveEnumDeserializer extends StdScalarDeserializer<Enum<?>> {
        private static final long serialVersionUID = 1L;

        private final EnumMapper mapper;

        @SuppressWarnings("unchecked")
        protected PermissiveEnumDeserializer(EnumMapper mapper, Class<Enum<?>> clazz) {
            super(clazz);
            this.mapper = mapper;
        }

        @Override
        public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            final String value = jp.getText();
            final Class rawClass = (Class)this.handledType();
            try {
                return (Enum<?>)this.mapper.deserialize(value, rawClass);
            }
            catch (IOException e) {
                throw new UnrecognizedPropertyException(
                    jp, "No enum constant " + this.handledType().getCanonicalName() + "." + value,
                    JsonLocation.NA, rawClass, "value", null);
            }
        }

        @Override
        public boolean isCachable() {
            // Should cache enum deserializers similar to com.fasterxml.jackson.databind.deser.std.EnumDeserializer
            return true;
        }
    }

    static private class PermissiveEnumDeserializers extends Deserializers.Base {

        private final EnumMapper mapper;

        public PermissiveEnumDeserializers(EnumMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        @SuppressWarnings("unchecked")
        public JsonDeserializer<?> findEnumDeserializer(Class<?> type,
                                                        DeserializationConfig config,
                                                        BeanDescription desc) throws JsonMappingException {
            // If the user configured to use `toString` method to deserialize enums
            if (config.hasDeserializationFeatures(DeserializationFeature.READ_ENUMS_USING_TO_STRING.getMask()) ||
                    config.hasDeserializationFeatures(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL.getMask()) ||
                    // The presence of @JsonEnumDefaultValue will cause a fallback to the default, however lets short circuit here
                    config.hasDeserializationFeatures(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE.getMask())) {
                return null;
            }

            // If there is a JsonCreator annotation we should use that instead of the PermissiveEnumDeserializer
            final Collection<AnnotatedMethod> factoryMethods = desc.getFactoryMethods();
            if (factoryMethods != null) {
                for (AnnotatedMethod am : factoryMethods) {
                    if (am.hasAnnotation(JsonCreator.class)) {
                        return null;
                    }
                }
            }

            // If any enum choice is annotated with an annotation from jackson, defer to
            // Jackson to do the deserialization
            for (Field field : type.getFields()) {
                for (Annotation annotation : field.getAnnotations()) {
                    final String packageName = annotation.annotationType().getPackage().getName();
                    if (packageName.equals("com.fasterxml.jackson.annotation")) {
                        return null;
                    }
                }
            }

            return new PermissiveEnumDeserializer(mapper, (Class<Enum<?>>)type);
        }
    }

}