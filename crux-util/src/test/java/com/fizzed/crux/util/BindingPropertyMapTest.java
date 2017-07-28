/*
 * Copyright 2017 Fizzed, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.crux.util;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class BindingPropertyMapTest {

    static public class Config implements BindingPropertySupport<Config> {
        
        static private final BindingPropertyMap<Config> HANDLERS = new BindingPropertyMap<Config>()
            .bindString("first_name", Config::setFirstName)
            .bindString("last_name", Config::setLastName)
            .bindInteger("port", Config::setPort)
            .bindType(URI.class, "uri", Config::setUri, (s) -> URI.create(s));
        
        private String firstName;
        private String lastName;
        private Integer port;
        private URI uri;

        public Config() {
        }

        @Override
        public BindingPropertyMap<Config> getPropertyMap() {
            return HANDLERS;
        }
        
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }
        
    }
    
    @Test
    public void bind() {
        Config config = new Config();
        
        config.setProperty("first_name", "Joe");
        config.setProperty("port", 80);
        config.setProperty("uri", "http://localhost");
        
        assertThat(config.getFirstName(), is("Joe"));
        assertThat(config.getPort(), is(80));
        assertThat(config.getUri(), is(URI.create("http://localhost")));
        
        Map<String,String> values = new HashMap<>();
        values.put("first_name", "Dude");
        values.put("last_name", "Lauer");
        
        config.setProperties(values)
            .setProperty("port", "81");
        
        assertThat(config.getFirstName(), is("Dude"));
        assertThat(config.getLastName(), is("Lauer"));
        assertThat(config.getPort(), is(81));
        
        assertThat(config.getPropertyMap().getKeys(), hasItem("first_name"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void setInvalidParameter() {
        Config config = new Config();
        
        config.setProperty("blah", "Joe");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void setInvalidValue() {
        Config config = new Config();
        
        config.setProperty("port", "Joe");
    }
    
    @Test
    public void bindNonMatchingTypes() throws Exception {
        Config config = new Config();
        
        config.setProperty("first_name", "Joe");
        config.setProperty("port", 80L);
        config.setProperty("uri", new URL("https://localhost"));
        
        // match the actual properties...
        assertThat(config.getFirstName(), is("Joe"));
        assertThat(config.getPort(), is(80));
        assertThat(config.getUri(), is(URI.create("https://localhost")));
        
        Map<String,Object> values = new HashMap<>();
        values.put("first_name", "Dude");
        values.put("last_name", "Lauer");
        
        config.setProperties(values)
            .setProperty("port", "81");
        
        assertThat(config.getFirstName(), is("Dude"));
        assertThat(config.getLastName(), is("Lauer"));
        assertThat(config.getPort(), is(81));
        
        assertThat(config.getPropertyMap().getKeys(), hasItem("first_name"));
    }
    
}
