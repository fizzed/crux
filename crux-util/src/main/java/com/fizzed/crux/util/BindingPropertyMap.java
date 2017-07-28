package com.fizzed.crux.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BindingPropertyMap<A> {

    public class Property<A,T> {

        private final Class<T> type;
        private final BiConsumer<A,T> setter;
        private final Function<String,T> converter;

        public Property(Class<T> type, BiConsumer<A,T> setter, Function<String,T> converter) {
            this.type = type;
            this.setter = setter;
            this.converter = converter;
        }
        
        public void set(A instance, Object value) {
            T target;
            
            if (value == null || type.isInstance(value)) {
                target = (T)value;
            } else {
                String s = Objects.toString(value, null);
                try {
                    target = this.converter.apply(s);
                } catch (Throwable t) {
                    throw new IllegalArgumentException("Unable to convert '" + s + "' to " + this.type.getCanonicalName());
                }
            }
            
            this.setter.accept(instance, target);
        }
    }
    
    static public final Function<String,String> STRING_CONVERTER = (s) -> {
        return s;
    };
    static public final Function<String,Integer> INTEGER_CONVERTER = (s) -> {
        return s != null ? Integer.valueOf(s) : null;
    };
    static public final Function<String,Long> LONG_CONVERTER = (s) -> {
        return s != null ? Long.valueOf(s) : null;
    };
    static public final Function<String,Path> PATH_CONVERTER = (s) -> {
        return s != null ? Paths.get(s) : null;
    };
    static public final Function<String,File> FILE_CONVERTER = (s) -> {
        return s != null ? new File(s) : null;
    };
    static public final Function<String,URI> URI_CONVERTER = (s) -> {
        return s != null ? URI.create(s) : null;
    };
    static public final Function<String,URL> URL_CONVERTER = (s) -> {
        if (s == null) {
            return null;
        }
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    };
    
    private final Map<String,Property<A,?>> map;

    public BindingPropertyMap() {
        this.map = new LinkedHashMap<>();      // insertion order important
    }
    
    public Set<String> getKeys() {
        return this.map.keySet();
    }
    
    public BindingPropertyMap<A> bindString(String key, BiConsumer<A,String> setter) {
        this.map.put(key, new Property<>(String.class, setter, STRING_CONVERTER));
        return this;
    }
    
    public BindingPropertyMap<A> bindInteger(String key, BiConsumer<A,Integer> setter) {
        this.map.put(key, new Property<>(Integer.class, setter, INTEGER_CONVERTER));
        return this;
    }
    
    public BindingPropertyMap<A> bindLong(String key, BiConsumer<A,Long> setter) {
        this.map.put(key, new Property<>(Long.class, setter, LONG_CONVERTER));
        return this;
    }
    
    public BindingPropertyMap<A> bindPath(String key, BiConsumer<A,Path> setter) {
        this.map.put(key, new Property<>(Path.class, setter, PATH_CONVERTER));
        return this;
    }
    
    public BindingPropertyMap<A> bindFile(String key, BiConsumer<A,File> setter) {
        this.map.put(key, new Property<>(File.class, setter, FILE_CONVERTER));
        return this;
    }
    
    public BindingPropertyMap<A> bindURI(String key, BiConsumer<A,URI> setter) {
        this.map.put(key, new Property<>(URI.class, setter, URI_CONVERTER));
        return this;
    }
    
    public BindingPropertyMap<A> bindURL(String key, BiConsumer<A,URL> setter) {
        this.map.put(key, new Property<>(URL.class, setter, URL_CONVERTER));
        return this;
    }
    
    public <T> BindingPropertyMap<A> bindType(String key, BiConsumer<A,T> setter, Class<T> type, Function<String,T> converter) {
        this.map.put(key, new Property<>(type, setter, converter));
        return this;
    }
    
    public void set(A instance, String key, Object value) {
        Objects.requireNonNull(instance, "instance was null");
        Objects.requireNonNull(key, "key was null");
        
        Property<A,Object> property = (Property<A,Object>)this.map.get(key);
        
        if (property == null) {
            throw new IllegalArgumentException("Property '" + key + "' is not recognized for "
                + instance.getClass().getCanonicalName()
                + " (available are " + this.map.keySet() + ")");
        }
        
        try {
            property.set(instance, value);
        } catch (IllegalArgumentException e) {
            // unwrap conversion exception, add message, but also return its cause
            throw new IllegalArgumentException("Property '" + key + "' could not be converted. " + e.getMessage(), e.getCause());
        }
    }
    
    public void setAll(A instance, Map<String,?> values) {
        if (values == null) {
            return;
        }
        values.forEach((key, value) -> this.set(instance, key, value));
    }
    
}