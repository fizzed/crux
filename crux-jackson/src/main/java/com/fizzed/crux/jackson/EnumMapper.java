package com.fizzed.crux.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class EnumMapper {

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
    
    
    private final EnumSerializeStrategy serializeStrategy;
    private final EnumDeserializeStrategy deserializeStrategy;
    private EnumGlobalUnknownHandler globalUnknownEnumHandler;
    
    public EnumMapper() {
        this(EnumSerializeStrategy.LOWER_CASE, EnumDeserializeStrategy.IGNORE_CASE);
    }
    
    public EnumMapper(
            EnumSerializeStrategy serializeStrategy,
            EnumDeserializeStrategy deserializeStrategy) {
        
        Objects.requireNonNull(serializeStrategy, "serialize strategy was null");
        Objects.requireNonNull(deserializeStrategy, "deserialize strategy was null");
        
        this.serializeStrategy = serializeStrategy;
        this.deserializeStrategy = deserializeStrategy;
    }

    public EnumGlobalUnknownHandler getGlobalUnknownEnumHandler() {
        return globalUnknownEnumHandler;
    }

    public EnumMapper setGlobalUnknownEnumHandler(
            EnumGlobalUnknownHandler globalUnknownEnumHandler) {
        
        this.globalUnknownEnumHandler = globalUnknownEnumHandler;
        return this;
    }

    public String serialize(Enum value) throws IOException {
        switch (EnumMapper.this.serializeStrategy) {
            case LOWER_CASE:
                return value.name().toLowerCase();
            case UPPER_CASE:
                return value.name().toUpperCase();
            case RETAIN_CASE:
                return value.name();
            default:
                throw new IllegalArgumentException("Unsupported serialize strategy " + this.serializeStrategy);
        }
    }
    
    public <T extends Enum> T deserialize(String value, Class<T> rawClass) throws IOException {

        if (value == null || value.isEmpty()) {
            return null;
        }

        // does it have an unknown enum handler?
        final EnumInfo enumInfo = computeEnumInfo(rawClass);

        // is there a creator method?
        if (enumInfo.getCreateEnumMethod() != null) {
            try {
                return (T)enumInfo.getCreateEnumMethod().invoke(null, value);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }   
        }

        switch (EnumMapper.this.deserializeStrategy) {
            case IGNORE_CASE:
                for (T en : rawClass.getEnumConstants()) {
                    if (en.name().equalsIgnoreCase(value)) {
                        return en;
                    }
                }
                break;
            case UPPER_CASE:
                for (T en : rawClass.getEnumConstants()) {
                    if (en.name().toUpperCase().equals(value)) {
                        return en;
                    }
                }
                break;
            case LOWER_CASE:
                for (T en : rawClass.getEnumConstants()) {
                    if (en.name().toLowerCase().equals(value)) {
                        return en;
                    }
                }
                break;
        }

        // if there is an unknown enum handler on the enum itself, call that first
        if (enumInfo.getUnknownEnumMethod() != null) {
            try {
                return (T)enumInfo.getUnknownEnumMethod().invoke(null, value);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }

        // global handler?
        if (EnumMapper.this.globalUnknownEnumHandler != null) {
            EnumMapper.this.globalUnknownEnumHandler.onUnknownEnum(rawClass, value);
        }

        // if the enum is flagged as ignoring unknown, then do nothing
        if (enumInfo.isIgnoreUnknown()) {
            return null;
        }

        // default is to mimic jackson's behavior and throw an unknown property exception
        throw new IOException("No enum constant " + rawClass.getCanonicalName() + "." + value);
//        throw new UnrecognizedPropertyException(
//            jp, "No enum constant " + rawClass.getCanonicalName() + "." + value,
//            JsonLocation.NA, rawClass, "value", null);
    }
    
}