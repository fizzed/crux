package com.fizzed.crux.jackson;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.temporal.ChronoUnit;

public class JavaTimePlusModule {

    /**
     * @deprecated Use JavaTimePlusModuleBuilder instead
     */
    @Deprecated
    static public JavaTimeModule build() {
        return new JavaTimeModuleBuilder().build();
    }

    /**
     * @deprecated Use JavaTimePlusModuleBuilder instead
     */
    @Deprecated
    static public JavaTimeModule build(boolean strictDeserializing) {
        return new JavaTimeModuleBuilder()
            .setStrictDeserializing(strictDeserializing)
            .build();
    }

    /**
     * @deprecated Use JavaTimePlusModuleBuilder instead
     */
    @Deprecated
    static public JavaTimeModule build(boolean strictDeserializing, ChronoUnit truncateTo) {
        return new JavaTimeModuleBuilder()
            .setStrictDeserializing(strictDeserializing)
            .setTruncateTo(truncateTo)
            .build();
    }
    
}