package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.UUID;

public class JavaUUIDModule extends SimpleModule {

    public JavaUUIDModule() {
        this(JavaUUIDStyle.DEFAULT);
    }

    public JavaUUIDModule(JavaUUIDStyle defaultStyle) {
        this.addSerializer(UUID.class, new JavaUUIDSerializer(defaultStyle));
        this.addDeserializer(UUID.class, new JavaUUIDDeserializer(defaultStyle));
    }

}