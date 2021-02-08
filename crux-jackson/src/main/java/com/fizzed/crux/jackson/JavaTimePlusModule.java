package com.fizzed.crux.jackson;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class JavaTimePlusModule {
    
    static public final ZoneId TZ_UTC = ZoneId.of("Z");
    
    static public JavaTimeModule build() {
        return build(true);
    }
    
    static public JavaTimeModule build(
            boolean strictDeserializing) {
        
        return build(strictDeserializing, ChronoUnit.MILLIS);
    }
    
    static public JavaTimeModule build(
            boolean strictDeserializing,
            ChronoUnit truncateTo) {
        
        final JavaTimeModule module = new JavaTimeModule();
        
        module.addSerializer(Instant.class, new JavaInstantSerializer());
        module.addDeserializer(Instant.class, new JavaInstantDeserializer(strictDeserializing, truncateTo));
        
        module.addSerializer(ZonedDateTime.class, new JavaZonedDateTimeSerializer());
        module.addDeserializer(ZonedDateTime.class, new JavaZonedDateTimeDeserializer(strictDeserializing, TZ_UTC, truncateTo));
        
        return module;
    }
    
}