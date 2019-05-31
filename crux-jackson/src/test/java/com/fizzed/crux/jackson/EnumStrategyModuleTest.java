package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fizzed.crux.jackson.EnumStrategyModule.DeserializeStrategy;
import com.fizzed.crux.jackson.EnumStrategyModule.SerializeStrategy;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

public class EnumStrategyModuleTest {
    
    static public enum Animal {
        DOG,
        cat,
        Rabbit
    }
    
    @Test
    public void deserializeDefaultIgnoreCase() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new EnumStrategyModule());
        
        assertThat(objectMapper.readValue("\"dog\"", Animal.class), is(Animal.DOG));
        assertThat(objectMapper.readValue("\"CaT\"", Animal.class), is(Animal.cat));
        assertThat(objectMapper.readValue("\"RABBIT\"", Animal.class), is(Animal.Rabbit));
        
        try {
            objectMapper.readValue("\"bad\"", Animal.class);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void deserializeUpperCase() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new EnumStrategyModule(SerializeStrategy.UPPER_CASE, DeserializeStrategy.UPPER_CASE));
        
        assertThat(objectMapper.readValue("\"DOG\"", Animal.class), is(Animal.DOG));
        assertThat(objectMapper.readValue("\"CAT\"", Animal.class), is(Animal.cat));
        assertThat(objectMapper.readValue("\"RABBIT\"", Animal.class), is(Animal.Rabbit));
        
        try {
            objectMapper.readValue("\"rabbit\"", Animal.class);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void deserializeLowerCase() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new EnumStrategyModule(SerializeStrategy.UPPER_CASE, DeserializeStrategy.LOWER_CASE));
        
        assertThat(objectMapper.readValue("\"dog\"", Animal.class), is(Animal.DOG));
        assertThat(objectMapper.readValue("\"cat\"", Animal.class), is(Animal.cat));
        assertThat(objectMapper.readValue("\"rabbit\"", Animal.class), is(Animal.Rabbit));
        
        try {
            objectMapper.readValue("\"RABBIT\"", Animal.class);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void serializeDefaultLowerCase() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new EnumStrategyModule());
        
        assertThat(objectMapper.writeValueAsString(Animal.DOG), is("\"dog\""));
        assertThat(objectMapper.writeValueAsString(Animal.cat), is("\"cat\""));
        assertThat(objectMapper.writeValueAsString(Animal.Rabbit), is("\"rabbit\""));
    }
    
    @Test
    public void serializeUpperCase() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new EnumStrategyModule(SerializeStrategy.UPPER_CASE, DeserializeStrategy.IGNORE_CASE));
        
        assertThat(objectMapper.writeValueAsString(Animal.DOG), is("\"DOG\""));
        assertThat(objectMapper.writeValueAsString(Animal.cat), is("\"CAT\""));
        assertThat(objectMapper.writeValueAsString(Animal.Rabbit), is("\"RABBIT\""));
    }
    
    @Test
    public void serializeRetainCase() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new EnumStrategyModule(SerializeStrategy.RETAIN_CASE, DeserializeStrategy.IGNORE_CASE));
        
        assertThat(objectMapper.writeValueAsString(Animal.DOG), is("\"DOG\""));
        assertThat(objectMapper.writeValueAsString(Animal.cat), is("\"cat\""));
        assertThat(objectMapper.writeValueAsString(Animal.Rabbit), is("\"Rabbit\""));
    }
    
}