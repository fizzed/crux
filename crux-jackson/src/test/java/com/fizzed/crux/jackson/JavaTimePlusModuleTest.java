package com.fizzed.crux.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

public class JavaTimePlusModuleTest {
 
    @Test
    public void serializeInstant() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(JavaTimePlusModule.build());
        
        final Instant i1 = Instant.parse("2021-01-01T00:00:00.000Z");
        final Instant i2 = Instant.parse("2021-01-01T00:00:00.471Z");
        final Instant i3 = Instant.parse("2021-01-01T00:00:00.999Z");
        final Instant i4 = Instant.parse("2021-01-01T00:00:00.999999Z");
        final Instant i5 = Instant.parse("2021-01-01T00:00:00.123456789Z");
        final Instant i6 = Instant.parse("2021-01-01T00:00:00Z");

        // this is how jdk would serialize it
        assertThat(i1.toString(), is("2021-01-01T00:00:00Z"));
        assertThat(objectMapper.writeValueAsString(i1), is("\"2021-01-01T00:00:00.000Z\""));
        assertThat(objectMapper.writeValueAsString(i2), is("\"2021-01-01T00:00:00.471Z\""));
        assertThat(objectMapper.writeValueAsString(i3), is("\"2021-01-01T00:00:00.999Z\""));
        assertThat(i4.getNano(), is(999999000));        // did java parse the high resolution?
        assertThat(objectMapper.writeValueAsString(i4), is("\"2021-01-01T00:00:00.999Z\""));
        assertThat(i5.getNano(), is(123456789));        // did java parse the high resolution?
        assertThat(objectMapper.writeValueAsString(i5), is("\"2021-01-01T00:00:00.123Z\""));
        assertThat(objectMapper.writeValueAsString(i6), is("\"2021-01-01T00:00:00.000Z\""));
    }
 
    @Test
    public void deserializeInstant() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(JavaTimePlusModule.build(false));
        
        final Instant i1 = Instant.parse("2021-01-01T00:00:00.000Z");
        final Instant i2 = Instant.parse("2021-01-01T00:00:00.471Z");
        final Instant i3 = Instant.parse("2021-01-01T00:00:00.999Z");
        
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.000Z\"", Instant.class), is(i1));
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00Z\"", Instant.class), is(i1));
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.471Z\"", Instant.class), is(i2));
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.999Z\"", Instant.class), is(i3));
        // the instant is very flexible on parsing super high resolution on milliseconds...
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.999999Z\"", Instant.class), is(i3));
    }
 
    @Test
    public void deserializeInstantStrictly() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(JavaTimePlusModule.build(true));
        
        final Instant i1 = Instant.parse("2021-01-01T00:00:00.999Z");
        
        try {
            objectMapper.readValue("\"2021-01-01T00:00:00.999999Z\"", Instant.class);
            fail();
        }
        catch (IOException e) {
            // expected
        }

        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.999Z\"", Instant.class), is(i1));
    }
    
    @Test
    public void serializeZonedDateTime() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(JavaTimePlusModule.build());
        
        final ZonedDateTime i1 = ZonedDateTime.parse("2021-01-01T00:00:00.000Z");
        final ZonedDateTime i2 = ZonedDateTime.parse("2021-01-01T00:00:00.471Z");
        final ZonedDateTime i3 = ZonedDateTime.parse("2021-01-01T00:00:00.999Z");
        final ZonedDateTime i4 = ZonedDateTime.parse("2021-01-01T00:00:00.999999Z");
        final ZonedDateTime i5 = ZonedDateTime.parse("2021-01-01T00:00:00.123456789Z");
        final ZonedDateTime i6 = ZonedDateTime.parse("2021-01-01T00:00:00Z");
        
        assertThat(objectMapper.writeValueAsString(i1), is("\"2021-01-01T00:00:00.000Z\""));
        assertThat(objectMapper.writeValueAsString(i2), is("\"2021-01-01T00:00:00.471Z\""));
        assertThat(objectMapper.writeValueAsString(i3), is("\"2021-01-01T00:00:00.999Z\""));
        assertThat(i4.getNano(), is(999999000));        // did java parse the high resolution?
        assertThat(objectMapper.writeValueAsString(i4), is("\"2021-01-01T00:00:00.999Z\""));
        assertThat(i5.getNano(), is(123456789));        // did java parse the high resolution?
        assertThat(objectMapper.writeValueAsString(i5), is("\"2021-01-01T00:00:00.123Z\""));
        assertThat(objectMapper.writeValueAsString(i6), is("\"2021-01-01T00:00:00.000Z\""));
    }
 
    @Test
    public void deserializeZonedDateTime() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(JavaTimePlusModule.build(false));
        
        final ZonedDateTime i1 = ZonedDateTime.parse("2021-01-01T00:00:00.000Z");
        final ZonedDateTime i2 = ZonedDateTime.parse("2021-01-01T00:00:00.471Z");
        final ZonedDateTime i3 = ZonedDateTime.parse("2021-01-01T00:00:00.999Z");
        final ZonedDateTime i4 = ZonedDateTime.parse("2021-01-01T00:00:00Z");
        
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.000Z\"", ZonedDateTime.class), is(i1));
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00Z\"", ZonedDateTime.class), is(i1));
        assertThat(objectMapper.readValue("\"2021-01-01T00:00Z\"", ZonedDateTime.class), is(i1));
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.471Z\"", ZonedDateTime.class), is(i2));
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.999Z\"", ZonedDateTime.class), is(i3));
        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.999999Z\"", ZonedDateTime.class), is(i3));
    }
    
    @Test
    public void deserializeZonedDateTimeStrictly() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(JavaTimePlusModule.build(true));
        
        final ZonedDateTime i1 = ZonedDateTime.parse("2021-01-01T00:00:00.999Z");
        
        try {
            objectMapper.readValue("\"2021-01-01T00:00:00.999999Z\"", ZonedDateTime.class);
            fail();
        }
        catch (IOException e) {
            // expected
        }

        assertThat(objectMapper.readValue("\"2021-01-01T00:00:00.999Z\"", ZonedDateTime.class), is(i1));
    }

    static public class Widget {

        private Instant i1;
        private ZonedDateTime zdt1;

        public Instant getI1() {
            return i1;
        }

        public Widget setI1(Instant i1) {
            this.i1 = i1;
            return this;
        }

        public ZonedDateTime getZdt1() {
            return zdt1;
        }

        public Widget setZdt1(ZonedDateTime zdt1) {
            this.zdt1 = zdt1;
            return this;
        }

    }

    @Test
    public void serializeObject() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.registerModule(JavaTimePlusModule.build(false));

        Widget widget1 = new Widget();
        widget1.setI1(Instant.parse("2021-01-01T01:02:03Z"));
        widget1.setZdt1(ZonedDateTime.parse("2021-01-01T01:02:03Z"));

        assertThat(objectMapper.writeValueAsString(widget1), is("{\"i1\":\"2021-01-01T01:02:03.000Z\",\"zdt1\":\"2021-01-01T01:02:03.000Z\"}"));

        Widget widget2 = new Widget();
        widget2.setI1(Instant.parse("2021-01-01T01:02:03.123Z"));
        widget2.setZdt1(ZonedDateTime.parse("2021-01-01T01:02:03.123Z"));

        assertThat(objectMapper.writeValueAsString(widget2), is("{\"i1\":\"2021-01-01T01:02:03.123Z\",\"zdt1\":\"2021-01-01T01:02:03.123Z\"}"));

        Widget widget3 = new Widget();
        widget3.setI1(Instant.ofEpochMilli(1688160824000L));
        widget3.setZdt1(ZonedDateTime.parse("2021-01-01T01:02:03.123Z"));

        assertThat(objectMapper.writeValueAsString(widget3), is("{\"i1\":\"2023-06-30T21:33:44.000Z\",\"zdt1\":\"2021-01-01T01:02:03.123Z\"}"));
    }

}