package com.fizzed.crux.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BindableOptions<A> {

    public class Handler<A,Object> {

        private final BiConsumer<A,Object> consumer;
        private final Function<String,Object> converter;

        public Handler(BiConsumer<A,Object> consumer, Function<String,Object> converter) {
            this.consumer = consumer;
            this.converter = converter;
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
    
    private final Map<String,Handler<A,?>> handlers;

    public BindableOptions() {
        this.handlers = new LinkedHashMap<>();      // insertion order important
    }
    
    public BindableOptions<A> bindString(String key, BiConsumer<A,String> consumer) {
        this.handlers.put(key, new Handler<>(consumer, STRING_CONVERTER));
        return this;
    }
    
    public BindableOptions<A> bindInteger(String key, BiConsumer<A,Integer> consumer) {
        this.handlers.put(key, new Handler<>(consumer, INTEGER_CONVERTER));
        return this;
    }
    
    public BindableOptions<A> bindLong(String key, BiConsumer<A,Long> consumer) {
        this.handlers.put(key, new Handler<>(consumer, LONG_CONVERTER));
        return this;
    }
    
    public <T> BindableOptions<A> bindType(String key, BiConsumer<A,T> consumer, Function<String,T> converter) {
        this.handlers.put(key, new Handler<>(consumer, converter));
        return this;
    }
    
    public void process(A instance, String key, String value) {
        Handler<A,Object> handler = (Handler<A,Object>)this.handlers.get(key);
        
        if (handler == null) {
            throw new IllegalArgumentException("Option " + key + " is not supported");
        }
        
        Object converted = handler.converter.apply(value);
        
        handler.consumer.accept(instance, converted);
    }
    
}