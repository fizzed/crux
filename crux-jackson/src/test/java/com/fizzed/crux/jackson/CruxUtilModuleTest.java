package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fizzed.crux.util.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class CruxUtilModuleTest {
    
    @Test
    public void secureCode() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new CruxUtilModule());
        
        String json;
        SecureCode sc;
        
        json = "\"01234w\"";
        sc = new SecureCode("01234w");
        
        assertThat(objectMapper.readValue(json, SecureCode.class), is(sc));
        assertThat(objectMapper.writeValueAsString(sc), is(json));
    }
    
    @Test
    public void timeUUID() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new CruxUtilModule());
        
        String json;
        TimeUUID td;
        
        json = "\"e5b75fb3-fca6-11e7-9f59-3138381d5321\"";
        td = TimeUUID.fromString("e5b75fb3-fca6-11e7-9f59-3138381d5321");
        
        assertThat(objectMapper.readValue(json, TimeUUID.class), is(td));
        assertThat(objectMapper.writeValueAsString(td), is(json));
    }
    
    @Test
    public void timeDuration() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new CruxUtilModule());
        
        String json;
        TimeDuration td;
        
        json = "\"512ms\"";
        td = new TimeDuration(512L, TimeUnit.MILLISECONDS);
        
        assertThat(objectMapper.readValue(json, TimeDuration.class), is(td));
        assertThat(objectMapper.writeValueAsString(td), is(json));
    }

    @Test
    public void mutableInteger() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new CruxUtilModule());

        String json;
        MutableInteger mi;

        json = "15";
        mi = new MutableInteger(15);

        assertThat(objectMapper.readValue(json, MutableInteger.class), is(mi));
        assertThat(objectMapper.writeValueAsString(mi), is(json));

        json = "null";
        mi = null;

        assertThat(objectMapper.readValue(json, MutableInteger.class), is(nullValue()));
        assertThat(objectMapper.writeValueAsString(mi), is(json));
    }

    @Test
    public void mutableLong() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new CruxUtilModule());

        String json;
        MutableLong mi;

        json = "15";
        mi = new MutableLong(15);

        assertThat(objectMapper.readValue(json, MutableLong.class), is(mi));
        assertThat(objectMapper.writeValueAsString(mi), is(json));

        json = "null";
        mi = null;

        assertThat(objectMapper.readValue(json, MutableLong.class), is(nullValue()));
        assertThat(objectMapper.writeValueAsString(mi), is(json));
    }

    @Test
    public void mutableDouble() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new CruxUtilModule());

        String json;
        MutableDouble mi;

        json = "15.2";
        mi = new MutableDouble(15.2);

        assertThat(objectMapper.readValue(json, MutableDouble.class), is(mi));
        assertThat(objectMapper.writeValueAsString(mi), is(json));

        json = "null";
        mi = null;

        assertThat(objectMapper.readValue(json, MutableDouble.class), is(nullValue()));
        assertThat(objectMapper.writeValueAsString(mi), is(json));
    }

}