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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class UriTest {
    
    @Test
    public void testToString() {
        Uri uri = new Uri("http://www.fizzed.com");
        
        assertThat(uri.toString(), is("http://www.fizzed.com"));
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
    
}
