package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fizzed.crux.util.*;

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

        //
        // com.fizzed.crux.util.ByteSize
        //

        this.addDeserializer(ByteSize.class, new StdDeserializer<ByteSize>(ByteSize.class) {
            @Override
            public ByteSize deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                final String value = jp.getValueAsString();
                if (value == null || value.isEmpty()) {
                    return null;
                }
                return ByteSize.parse(value);
            }
        });

        this.addSerializer(ByteSize.class, new StdSerializer<ByteSize>(ByteSize.class) {
            @Override
            public void serialize(ByteSize value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    jgen.writeNull();
                } else {
                    jgen.writeString(value.toString());
                }
            }
        });

        //
        // com.fizzed.crux.util.MutableInteger
        //

        this.addDeserializer(MutableInteger.class, new StdDeserializer<MutableInteger>(MutableInteger.class) {
            @Override
            public MutableInteger deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                final int value = jp.getIntValue();
                return new MutableInteger(value);
            }
        });

        this.addSerializer(MutableInteger.class, new StdSerializer<MutableInteger>(MutableInteger.class) {
            @Override
            public void serialize(MutableInteger value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    jgen.writeNull();
                } else {
                    jgen.writeNumber(value.value());
                }
            }
        });

        //
        // com.fizzed.crux.util.MutableLong
        //

        this.addDeserializer(MutableLong.class, new StdDeserializer<MutableLong>(MutableLong.class) {
            @Override
            public MutableLong deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                final long value = jp.getLongValue();
                return new MutableLong(value);
            }
        });

        this.addSerializer(MutableLong.class, new StdSerializer<MutableLong>(MutableLong.class) {
            @Override
            public void serialize(MutableLong value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    jgen.writeNull();
                } else {
                    jgen.writeNumber(value.value());
                }
            }
        });


        //
        // com.fizzed.crux.util.MutableDouble
        //

        this.addDeserializer(MutableDouble.class, new StdDeserializer<MutableDouble>(MutableDouble.class) {
            @Override
            public MutableDouble deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                final double value = jp.getDoubleValue();
                return new MutableDouble(value);
            }
        });

        this.addSerializer(MutableDouble.class, new StdSerializer<MutableDouble>(MutableDouble.class) {
            @Override
            public void serialize(MutableDouble value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    jgen.writeNull();
                } else {
                    jgen.writeNumber(value.value());
                }
            }
        });
    }

}