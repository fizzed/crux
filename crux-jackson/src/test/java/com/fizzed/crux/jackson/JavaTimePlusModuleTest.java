package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZonedDateTime;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
}