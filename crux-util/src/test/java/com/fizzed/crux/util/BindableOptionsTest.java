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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class BindableOptionsTest {

    static public class Config {
        
        static private final BindableOptions<Config> OPTIONS = new BindableOptions<Config>()
            .bindString("first_name", Config::setFirstName)
            .bindString("last_name", Config::setLastName)
            .bindInteger("port", Config::setPort)
            .bindType("uri", Config::setUri, (s) -> URI.create(s));
        
        private String firstName;
        private String lastName;
        private Integer port;
        private URI uri;

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

        public void setParameter(String key, String value) {
            OPTIONS.process(this, key, value);
        }
        
    }
    
    @Test
    public void bind() {
        Config config = new Config();
        
        config.setParameter("first_name", "Joe");
        config.setParameter("port", "80");
        config.setParameter("uri", "http://localhost");
        
        assertThat(config.getFirstName(), is("Joe"));
        assertThat(config.getPort(), is(80));
        assertThat(config.getUri(), is(URI.create("http://localhost")));
    }
    
}
