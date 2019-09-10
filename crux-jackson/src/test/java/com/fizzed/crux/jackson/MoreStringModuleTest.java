package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class MoreStringModuleTest {
 
    @Test
    public void emptyToNull() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MoreStringModule(false, true));
        
        String value;
        
        value = objectMapper.readValue("\"\"", String.class);
        
        assertThat(value, is(nullValue()));
        
        value = objectMapper.readValue("\"a\"", String.class);
        
        assertThat(value, is("a"));
        
        value = objectMapper.readValue("\" \"", String.class);
        
        assertThat(value, is(" "));
        
        value = objectMapper.readValue("null", String.class);
        
        assertThat(value, is(nullValue()));
    }
    
 
    @Test
    public void trim() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MoreStringModule(true, false));
        
        String value;
        
        value = objectMapper.readValue("\"\"", String.class);
        
        assertThat(value, is(""));
        
        value = objectMapper.readValue("\"a\"", String.class);
        
        assertThat(value, is("a"));
        
        value = objectMapper.readValue("\" \"", String.class);
        
        assertThat(value, is(""));
        
        value = objectMapper.readValue("null", String.class);
        
        assertThat(value, is(nullValue()));
    }
    
 
    @Test
    public void trimAndEmptyToNull() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MoreStringModule(true, true));
        
        String value;
        
        value = objectMapper.readValue("\"\"", String.class);
        
        assertThat(value, is(nullValue()));
        
        value = objectMapper.readValue("\"a\"", String.class);
        
        assertThat(value, is("a"));
        
        value = objectMapper.readValue("\" \"", String.class);
        
        assertThat(value, is(nullValue()));
        
        value = objectMapper.readValue("null", String.class);
        
        assertThat(value, is(nullValue()));
    }
    
}