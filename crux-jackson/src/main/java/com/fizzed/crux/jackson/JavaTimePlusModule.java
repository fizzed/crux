package com.fizzed.crux.jackson;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.time.ZonedDateTime;

public class JavaTimePlusModule {
    
    static public JavaTimeModule build() {
        final JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(Instant.class, new InstantWithMsSerializer());
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeWithMsSerializer());
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeUtcDeserializer());
        return module;
    }
    
}