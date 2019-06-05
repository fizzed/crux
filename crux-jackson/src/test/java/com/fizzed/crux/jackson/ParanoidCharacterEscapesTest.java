package com.fizzed.crux.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class ParanoidCharacterEscapesTest {
 
    @Test
    public void escapes() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        
        json = objectMapper.writeValueAsString("</script>");
        
        assertThat(json, is("\"</script>\""));
        
        objectMapper.getFactory().setCharacterEscapes(ParanoidCharacterEscapes.INSTANCE);
        
        json= objectMapper.writeValueAsString("</script>");
        
        assertThat(json, is("\"<\\/script>\""));
    }
    
    @Test
    public void deserialize() throws JsonProcessingException, IOException {
        // verify either format is still acceptable for deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getFactory().setCharacterEscapes(ParanoidCharacterEscapes.INSTANCE);
        
        String value;
        
        value = objectMapper.readValue("\"<\\/script>\"", String.class);
        
        assertThat(value, is("</script>"));
        
        value = objectMapper.readValue("\"</script>\"", String.class);
        
        assertThat(value, is("</script>"));
    }
    
}