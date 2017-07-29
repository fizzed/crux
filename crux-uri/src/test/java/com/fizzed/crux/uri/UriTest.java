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

import static org.hamcrest.CoreMatchers.is;
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
        
        muri.rel("c/d");
        
        Uri uri2 = muri.toImmutable();
        
        assertThat(uri.toString(), is("http://www.fizzed.com/a/b"));
    }
    
    @Test
    public void mutableToImmutableQuery() {
        Uri uri = new Uri("http://www.fizzed.com?a=1&b=2");
        
        MutableUri muri = new MutableUri(uri);
        
        muri.query("c", 3);
        muri.query("d", 4);
        
        Uri uri2 = muri.toImmutable();
        
        assertThat(uri.toString(), is("http://www.fizzed.com?a=1&b=2"));
    }
    
}
