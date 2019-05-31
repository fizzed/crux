package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fizzed.crux.util.SecureCode;
import com.fizzed.crux.util.TimeDuration;
import com.fizzed.crux.util.TimeUUID;
import java.io.IOException;

public class CruxUtilModule extends SimpleModule {

    public CruxUtilModule() {

        //
        // com.fizzed.crux.util.SecureCode
        //
        
        this.addDeserializer(SecureCode.class, new StdDeserializer<SecureCode>(SecureCode.class) {
            @Override
            public SecureCode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                final String value = jp.getValueAsString();
                if (value == null || value.isEmpty()) {
                    return null;
                }
                return new SecureCode(value);
            }
        });

        this.addSerializer(SecureCode.class, new StdSerializer<SecureCode>(SecureCode.class) {
            @Override
            public void serialize(SecureCode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    jgen.writeNull();
                } else {
                    jgen.writeString(value.toString());
                }
            }
        });
        
        //
        // com.fizzed.crux.util.TimeUUID
        //
        
        this.addDeserializer(TimeUUID.class, new StdDeserializer<TimeUUID>(TimeUUID.class) {
            @Override
            public TimeUUID deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                final String value = jp.getValueAsString();
                if (value == null || value.isEmpty()) {
                    return null;
                }
                return TimeUUID.fromString(value);
            }
        });

        this.addSerializer(TimeUUID.class, new StdSerializer<TimeUUID>(TimeUUID.class) {
            @Override
            public void serialize(TimeUUID value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    jgen.writeNull();
                } else {
                    jgen.writeString(value.toString());
                }
            }
        });
        
        //
        // com.fizzed.crux.util.TimeDuration
        //
        
        this.addDeserializer(TimeDuration.class, new StdDeserializer<TimeDuration>(TimeDuration.class) {
            @Override
            public TimeDuration deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                final String value = jp.getValueAsString();
                if (value == null || value.isEmpty()) {
                    return null;
                }
                System.out.println("'"+value+"'");
                return TimeDuration.parse(value);
            }
        });

        this.addSerializer(TimeDuration.class, new StdSerializer<TimeDuration>(TimeDuration.class) {
            @Override
            public void serialize(TimeDuration value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    jgen.writeNull();
                } else {
                    jgen.writeString(value.toString());
                }
            }
        });
    }

}