package com.fizzed.crux.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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

    static private class EnumInfo {
        
        private final boolean ignoreUnknown;
        private final Method unknownEnumMethod;
        private final Method createEnumMethod;

        public EnumInfo(boolean ignoreUnknown, Method unknownEnumMethod, Method createEnumMethod) {
            this.ignoreUnknown = ignoreUnknown;
            this.unknownEnumMethod = unknownEnumMethod;
            this.createEnumMethod = createEnumMethod;
        }

        public boolean isIgnoreUnknown() {
            return ignoreUnknown;
        }

        public Method getUnknownEnumMethod() {
            return unknownEnumMethod;
        }

        public Method getCreateEnumMethod() {
            return createEnumMethod;
        }

    }
    
    static private final ConcurrentHashMap<Class<? extends Enum>,EnumInfo> ENUM_INFOS = new ConcurrentHashMap<>();
    
    static private EnumInfo computeEnumInfo(
            Class<? extends Enum> rawClass) {
        
        return ENUM_INFOS.computeIfAbsent(rawClass, t -> {
            boolean ignoreUnknown = false;
            JsonIgnoreProperties ignoreProps = rawClass.getAnnotation(JsonIgnoreProperties.class);
            if (ignoreProps != null) {
                ignoreUnknown = ignoreProps.ignoreUnknown();
            }

            Method unknownEnumMethod = null;
            
            for (Method m : rawClass.getMethods()) {
                JsonUnknownEnum v = (JsonUnknownEnum)m.getAnnotation(JsonUnknownEnum.class);
                if (v != null) {
                    System.out.println(m.toGenericString());
                    if (!Modifier.isStatic(m.getModifiers())) {
                        throw new IllegalArgumentException("OnUnknownEnum method must be static");
                    }
                    if (m.getParameterCount() != 1) {
                        throw new IllegalArgumentException("OnUnknownEnum method must have exactly 1 parameter");
                    }
                    if (!m.getParameterTypes()[0].equals(String.class)) {
                        throw new IllegalArgumentException("OnUnknownEnum method first parameter must be a java.lang.String");
                    }
                    if (!m.getReturnType().equals(rawClass)) {
                        throw new IllegalArgumentException("OnUnknownEnum method return type must be " + rawClass.getCanonicalName());
                    }
                    unknownEnumMethod = m;
                    break;
                }
            }

            Method createEnumMethod = null;
            
            for (Method m : rawClass.getMethods()) {
                JsonCreator v = (JsonCreator)m.getAnnotation(JsonCreator.class);
                if (v != null) {
                    if (!Modifier.isStatic(m.getModifiers())) {
                        throw new IllegalArgumentException("JsonCreator method must be static");
                    }
                    if (m.getParameterCount() != 1) {
                        throw new IllegalArgumentException("JsonCreator method must have exactly 1 parameter");
                    }
                    if (!m.getParameterTypes()[0].equals(String.class)) {
                        throw new IllegalArgumentException("JsonCreator method first parameter must be a java.lang.String");
                    }
                    if (!m.getReturnType().equals(rawClass)) {
                        throw new IllegalArgumentException("JsonCreator method return type must be " + rawClass.getCanonicalName());
                    }
                    createEnumMethod = m;
                    break;
                }
            }
            
            return new EnumInfo(ignoreUnknown, unknownEnumMethod, createEnumMethod);
        });
    }
    
    private final SerializeStrategy serializeStrategy;
    private final DeserializeStrategy deserializeStrategy;

    public EnumStrategyModule() {
        this(SerializeStrategy.LOWER_CASE, DeserializeStrategy.IGNORE_CASE);
    }
    
    public EnumStrategyModule(
            SerializeStrategy serializeStrategy,
            DeserializeStrategy deserializeStrategy) {
        
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
                        
                        final Class<? extends Enum> rawClass = (Class<Enum<?>>) type.getRawClass();
                        
                        // does it have an unknown enum handler?
                        final EnumInfo enumInfo = computeEnumInfo(rawClass);
                        
                        // is there a creator method?
                        if (enumInfo.getCreateEnumMethod() != null) {
                            try {
                                return (Enum)enumInfo.getCreateEnumMethod().invoke(null, value);
                            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                                throw new IllegalArgumentException(e.getMessage(), e);
                            }   
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
                        
                        // if there is an unknown enum handler, call that first
                        if (enumInfo.getUnknownEnumMethod() != null) {
                            try {
                                return (Enum)enumInfo.getUnknownEnumMethod().invoke(null, value);
                            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                                throw new IllegalArgumentException(e.getMessage(), e);
                            }
                        }
                        
                        // if the enum is flagged as ignoring unknown, then do nothing
                        if (enumInfo.isIgnoreUnknown()) {
                            return null;
                        }
                        
                        // custom handler?
//                        if (EnumStrategyModule.this.unknownEnumHandler != null) {
//                            // call the global handler, return that default enum
//                            return EnumStrategyModule.this.unknownEnumHandler.onUnknownEnum(
//                                rawClass, value);
//                        }
                        
                        // default is to mimic jackson's behavior and throw an unknown property exception
                        throw new UnrecognizedPropertyException(
                            jp, "No enum constant " + rawClass.getCanonicalName() + "." + value,
                            JsonLocation.NA, rawClass, "value", null);
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