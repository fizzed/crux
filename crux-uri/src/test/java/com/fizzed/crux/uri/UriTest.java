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
package com.fizzed.crux.uri;

import java.util.Arrays;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class UriTest {
    
    @Test
    public void testToString() {
        Uri uri = new Uri("http://www.fizzed.com");
        
        assertThat(uri.toString(), is("http://www.fizzed.com"));
        
        uri = new Uri("/");
        
        assertThat(uri.toString(), is("/"));
    }
    
    @Test
    public void mutableToImmutablePath() {
        Uri uri = new Uri("http://www.fizzed.com/a/b");
        
        MutableUri muri = new MutableUri(uri);
        
        // modify it
        muri.path("c/d");
        
        Uri uri2 = muri.immutable();
        
        // verify original is not modified
        assertThat(uri.toString(), is("http://www.fizzed.com/a/b"));
    }
    
    @Test
    public void mutableToImmutableQuery() {
        Uri uri = new Uri("http://www.fizzed.com?a=1&b=2");
        
        MutableUri muri = new MutableUri(uri);
        
        // modify it
        muri.query("c", 3);
        muri.query("d", 4);
        
        Uri uri2 = muri.immutable();
        
        // verify original is not modified
        assertThat(uri.toString(), is("http://www.fizzed.com?a=1&b=2"));
    }
    
    @Test
    public void rels() {
        Uri uri;
        
        uri = new Uri("http://www.fizzed.com");
        
        assertThat(uri.getRels(), is(nullValue()));
        
        // empty path, but then set it to empty string
        uri = new MutableUri("http://www.fizzed.com")
            .path("")
            .toUri();
        
        assertThat(uri.getRels(), is(Arrays.asList("")));
        assertThat(uri.getRel(0), is(""));
        assertThat(uri.getRel(1), is(nullValue()));
        
        uri = new Uri("http://www.fizzed.com/");
        
        assertThat(uri.getRels(), is(Arrays.asList("")));
        
        uri = new Uri("http://www.fizzed.com/test");
        
        assertThat(uri.getRels(), is(Arrays.asList("test")));
        assertThat(uri.getRel(0), is("test"));
        assertThat(uri.getRel(1), is(nullValue()));
    }
    
    @Test
    public void getQueryFirstMap() {
        Uri uri = new Uri("http://localhost?a=1&a=2&b=2&b=1&c=1");
        
        Map<String,String> map = uri.getQueryFirstMap();
        
        // only first entries
        assertThat(map, hasEntry("a","1"));
        assertThat(map, hasEntry("b","2"));
        assertThat(map, hasEntry("c","1"));
    }
    
    @Test
    public void isAbsolute() {
        Uri uri;
        
        uri = new Uri("http://www.fizzed.com");
        
        assertThat(uri.isAbsolute(), is(true));
        
        uri = new Uri("/");
        
        assertThat(uri.isAbsolute(), is(false));
        assertThat(uri.getHost(), is(nullValue()));
    }
    
    @Test
    public void resolve() {
        Uri a;
        Uri b;
        
        a = new Uri("http://www.fizzed.com");
        
        b = a.resolve("/dude");
        
        assertThat(b.toString(), is("http://www.fizzed.com/dude"));
        
        b = a.resolve("/dude?a=1");
        
        assertThat(b.toString(), is("http://www.fizzed.com/dude?a=1"));
        
        b = a.resolve("//www.example.com?a=1");
        
        assertThat(b.toString(), is("http://www.example.com?a=1"));
        
        b = a.resolve("//www.example.com/a?a=1");
        
        assertThat(b.toString(), is("http://www.example.com/a?a=1"));
        
        b = a.resolve("https://www.example.com?a=1");
        
        assertThat(b.toString(), is("https://www.example.com?a=1"));
        
        b = a.resolve("https://www.example.com/a?a=1");
        
        assertThat(b.toString(), is("https://www.example.com/a?a=1"));
    }
    
    @Test
    public void realWorldLinks() {
        Uri uri = new Uri("mailto:info@example.com");
        
        assertThat(uri.getScheme(), is("mailto"));
        assertThat(uri.toString(), is("mailto:info@example.com"));
        
        // real-world links that chrome handles :-(
        // https://fonts.googleapis.com/css?family=Roboto:100,300,300italic,400,500italic|Open+Sans:400&subset=latin
        
        //Uri uri = new Uri("https://fonts.googleapis.com/css?family=Roboto:100,300,300italic,400,500italic|Open+Sans:400&subset=latin");
        
        //assertThat(uri, is(not(nullValue())));
    }
    
}
