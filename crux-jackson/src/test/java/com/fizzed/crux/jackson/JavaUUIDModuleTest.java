package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JavaUUIDModuleTest {
 
    @Test
    public void serializeDefaultStyle() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaUUIDModule());
        
        final UUID uuid1 = UUID.fromString("185F18D6-DEBC-49dd-9f62-a8bf2831a868");

        assertThat(objectMapper.writeValueAsString(uuid1), is("\"185f18d6-debc-49dd-9f62-a8bf2831a868\""));
    }

    @Test
    public void serializeStrippedStyle() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaUUIDModule(JavaUUIDStyle.STRIPPED));

        final UUID uuid1 = UUID.fromString("185f18d6-debc-49dd-9f62-a8bf2831a868");

        assertThat(objectMapper.writeValueAsString(uuid1), is("\"185f18d6debc49dd9f62a8bf2831a868\""));
    }

    @Test
    public void deserializeDefaultStyle() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaUUIDModule());

        final UUID uuid1 = UUID.fromString("185F18D6-DEBC-49dd-9f62-a8bf2831a868");

        assertThat(objectMapper.readValue("\"185f18d6-debc-49dd-9f62-a8bf2831a868\"", UUID.class), is(uuid1));
    }

    @Test
    public void deserializeStrippedStyle() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaUUIDModule(JavaUUIDStyle.STRIPPED));

        final UUID uuid1 = UUID.fromString("185F18D6-DEBC-49dd-9f62-a8bf2831a868");

        // fails if not 32 chars long
        try {
            assertThat(objectMapper.readValue("\"185f18d6-debc-49dd-9f62-a8bf2831a868\"", UUID.class), is(uuid1));
        } catch (IllegalArgumentException e) {
            // expected
        }

        assertThat(objectMapper.readValue("\"185f18d6debc49dd9f62a8bf2831a868\"", UUID.class), is(uuid1));
        assertThat(objectMapper.readValue("\"185f18D6deBC49dd9f62a8bf2831a868\"", UUID.class), is(uuid1));
    }

    static public class Widget {
        @JavaUUIDFormat(JavaUUIDStyle.STRIPPED)
        private UUID uuid;

        public UUID getUuid() {
            return uuid;
        }

        public Widget setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }
    }

    @Test
    public void serializeContextualStyle() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaUUIDModule(JavaUUIDStyle.DEFAULT));

        final Widget w = new Widget()
            .setUuid(UUID.fromString("185f18d6-debc-49dd-9f62-a8bf2831a868"));

        assertThat(objectMapper.writeValueAsString(w), is("{\"uuid\":\"185f18d6debc49dd9f62a8bf2831a868\"}"));
    }

    @Test
    public void deserializeContextualStyle() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaUUIDModule(JavaUUIDStyle.DEFAULT));

        final Widget w = objectMapper.readValue("{\"uuid\":\"185f18d6debc49dd9f62a8bf2831a868\"}", Widget.class);

        assertThat(w.getUuid(), is(UUID.fromString("185f18d6-debc-49dd-9f62-a8bf2831a868")));
    }

}