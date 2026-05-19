package com.fizzed.crux.jackson;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class JavaTimeModuleBuilder {

    static public final ZoneId TZ_UTC = ZoneId.of("Z");

    private JavaTimeFormat format;
    private boolean strictDeserializing;
    private ChronoUnit truncateTo;
    private ZoneId zone;

    public JavaTimeModuleBuilder() {
        this.format = JavaTimeFormat.ISO_8601;
        this.strictDeserializing = true;
        this.truncateTo = ChronoUnit.MILLIS;
        this.zone = TZ_UTC;
    }

    public JavaTimeModuleBuilder setFormat(JavaTimeFormat format) {
        this.format = format;
        return this;
    }

    public JavaTimeModuleBuilder setStrictDeserializing(boolean strictDeserializing) {
        this.strictDeserializing = strictDeserializing;
        return this;
    }

    public JavaTimeModuleBuilder setTruncateTo(ChronoUnit truncateTo) {
        this.truncateTo = truncateTo;
        return this;
    }

    public JavaTimeModuleBuilder setZone(ZoneId zone) {
        this.zone = zone;
        return this;
    }

    public JavaTimeModule build() {
        final JavaTimeModule module = new JavaTimeModule();

        module.addSerializer(Instant.class, new JavaInstantSerializer(this.format));
        module.addDeserializer(Instant.class, new JavaInstantDeserializer(this.format, this.strictDeserializing, this.truncateTo));

        module.addSerializer(ZonedDateTime.class, new JavaZonedDateTimeSerializer(this.format));
        module.addDeserializer(ZonedDateTime.class, new JavaZonedDateTimeDeserializer(this.format, this.strictDeserializing, this.zone, this.truncateTo));

        return module;
    }
    
}