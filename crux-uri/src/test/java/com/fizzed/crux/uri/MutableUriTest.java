/*
 * Copyright 2016 Fizzed, Inc.
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
package com.fizzed.crux.uri;

import java.util.Arrays;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class MutableUriTest {

    @Test
    public void parse() {
        MutableUri uri;
        
        uri = new MutableUri("http://localhost");
        
        assertThat(uri.getScheme(), is("http"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        
        uri = new MutableUri("http://localhost:8080?a=1&b=2");
        
        assertThat(uri.getScheme(), is("http"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQueryFirst("a"), is("1"));
        assertThat(uri.getQueryFirst("b"), is("2"));
        assertThat(uri.getFragment(), is(nullValue()));
        
        uri = new MutableUri("http://localhost:8080/?a=1&b=2");
        
        assertThat(uri.getScheme(), is("http"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is("/"));
        assertThat(uri.getQueryFirst("a"), is("1"));
        assertThat(uri.getQueryFirst("b"), is("2"));
        assertThat(uri.getFragment(), is(nullValue()));
        
        uri = new MutableUri("http://localhost:8080/?a=1&b=2#frag");
        
        assertThat(uri.getScheme(), is("http"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is("/"));
        assertThat(uri.getQueryFirst("a"), is("1"));
        assertThat(uri.getQueryFirst("b"), is("2"));
        assertThat(uri.getFragment(), is("frag"));
        
        uri = new MutableUri("http://user1@localhost:8080/this/is/a/path?a=1&b=2#frag");
        
        assertThat(uri.getScheme(), is("http"));
        assertThat(uri.getUserInfo(), is("user1"));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is("/this/is/a/path"));
        assertThat(uri.getQueryFirst("a"), is("1"));
        assertThat(uri.getQueryFirst("b"), is("2"));
        assertThat(uri.getFragment(), is("frag"));
        
        uri = new MutableUri("http://user1@localhost:8080/this/is/a/path?a=1&a=2&b=2&c#frag");
        
        assertThat(uri.getScheme(), is("http"));
        assertThat(uri.getUserInfo(), is("user1"));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is("/this/is/a/path"));
        assertThat(uri.getQueryFirst("a"), is("1"));
        assertThat(uri.getQueryAll("a"), is(Arrays.asList("1", "2")));
        assertThat(uri.getQueryFirst("b"), is("2"));
        assertThat(uri.getQuery(), hasKey("c"));
        assertThat(uri.getFragment(), is("frag"));
    }
    
    @Test
    public void stringify() {
        String uri;
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .toString();
        
        assertThat(uri, is("http://localhost"));
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .toString();
        
        assertThat(uri, is("http://localhost:8080"));
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .query("a", "1")
            .query("b", "2")
            .toString();
        
        assertThat(uri, is("http://localhost:8080?a=1&b=2"));
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .path("/")
            .query("a", "1")
            .query("b", "2")
            .toString();
        
        assertThat(uri, is("http://localhost:8080/?a=1&b=2"));
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .path("/")
            .query("a", "1")
            .query("b", "2")
            .fragment("frag")
            .toString();
        
        assertThat(uri, is("http://localhost:8080/?a=1&b=2#frag"));
        
        uri = new MutableUri()
            .scheme("http")
            .userInfo("user1")
            .host("localhost")
            .port(8080)
            .path("/")
            .query("a", "1")
            .query("b", "2")
            .fragment("frag")
            .toString();
        
        assertThat(uri, is("http://user1@localhost:8080/?a=1&b=2#frag"));
        
        uri = new MutableUri()
            .scheme("http")
            .userInfo("user1")
            .host("localhost")
            .port(8080)
            .path("/this/is/a/path")
            .query("a", "1")
            .query("b", "2")
            .fragment("frag")
            .toString();
        
        assertThat(uri, is("http://user1@localhost:8080/this/is/a/path?a=1&b=2#frag"));
        
        // multiple parameters
        uri = new MutableUri()
            .scheme("http")
            .userInfo("user1")
            .host("localhost")
            .port(8080)
            .path("/this/is/a/path")
            .query("a", "1")
            .query("a", "2")
            .query("b", "2")
            .query("c", null)
            .fragment("frag")
            .toString();
        
        assertThat(uri, is("http://user1@localhost:8080/this/is/a/path?a=1&a=2&b=2&c#frag"));
        
        // multiple parameters & requiring encoding
        uri = new MutableUri()
            .scheme("http")
            .userInfo("user@1")
            .host("localhost")
            .port(8080)
            .path("/this/is/a/path")
            .query("a", "@")
            .query("a", "2")
            .query("b", "2")
            .query("c", null)
            .fragment("fr@g")
            .toString();
        
        assertThat(uri, is("http://user%401@localhost:8080/this/is/a/path?a=%40&a=2&b=2&c#fr%40g"));
    }
    
    @Test
    public void defaultThenOverride() {
        String uri;
        
        uri = new MutableUri("http://localhost")
            .scheme("https")
            .toString();
        
        assertThat(uri, is("https://localhost"));
        
        uri = new MutableUri("http://127.0.0.1:8081")
            .scheme("https")
            .host("localhost")
            .port(8080)
            .toString();
        
        assertThat(uri, is("https://localhost:8080"));
        
        uri = new MutableUri("http://localhost:8080?b=2&a=1")
            .scheme("https")
            .port(null)
            .query("c", "3")
            .toString();
        
        assertThat(uri, is("https://localhost?b=2&a=1&c=3"));
    }
    
}
