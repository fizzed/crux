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

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
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
        
        uri = new Uri("mailto:joe@example.com");
        
        assertThat(uri.isAbsolute(), is(true));
    }
    
    @Test
    public void resolve() {
        Uri a;
        Uri b;
        
        a = new Uri("http://www.fizzed.com");
        
        b = a.resolve("/dude");
        assertThat(b.toString(), is("http://www.fizzed.com/dude"));
        
        b = a.resolve("   /dude   ");
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
        
        b = a.resolve("mailto:joe@example.com");
        assertThat(b.toString(), is("mailto:joe@example.com"));
        
        // relative urls...
        a = new Uri("http://www.fizzed.com/a/b/c");
        
        b = a.resolve("d");
        assertThat(b.toString(), is("http://www.fizzed.com/a/b/d"));
        
        b = a.resolve("./d");
        assertThat(b.toString(), is("http://www.fizzed.com/a/b/d"));
        
        b = a.resolve("../d");
        assertThat(b.toString(), is("http://www.fizzed.com/a/d"));
        
        b = a.resolve("../../d");
        assertThat(b.toString(), is("http://www.fizzed.com/d"));
        
        b = a.resolve("../../.././.././../d");
        assertThat(b.toString(), is("http://www.fizzed.com/d"));
    }
    
    @Test
    public void schemeSpecificPart() {
        Uri uri;
        
        uri = new Uri("mailto:info@example.com");

        assertThat(uri.getScheme(), is("mailto"));
        assertThat(uri.getSchemeSpecificPart(), is("info@example.com"));
        assertThat(uri.toString(), is("mailto:info@example.com"));
        
        uri = new Uri("tel:+1-816-555-1212");

        assertThat(uri.getScheme(), is("tel"));
        assertThat(uri.getSchemeSpecificPart(), is("+1-816-555-1212"));
        assertThat(uri.toString(), is("tel:+1-816-555-1212"));
        
        uri = new Uri("urn:oasis:names:specification:docbook:dtd:xml:4.1.2");

        assertThat(uri.getScheme(), is("urn"));
        assertThat(uri.getSchemeSpecificPart(), is("oasis:names:specification:docbook:dtd:xml:4.1.2"));
        assertThat(uri.toString(), is("urn:oasis:names:specification:docbook:dtd:xml:4.1.2"));
        
        uri = new Uri("urn:oasis:names:specification:docbook:dtd:xml:4.1.2");

        assertThat(uri.getScheme(), is("urn"));
        assertThat(uri.getSchemeSpecificPart(), is("oasis:names:specification:docbook:dtd:xml:4.1.2"));
        assertThat(uri.toString(), is("urn:oasis:names:specification:docbook:dtd:xml:4.1.2"));
        
//        //uri = new Uri("ldap://[2001:db8::7]/c=GB?objectClass?one");
//        URI uri2 = URI.create("ldap://[2001:db8::7]/c=GB?objectClass?one");
//        
//        assertThat(uri2.getScheme(), is("ldap"));
//        //assertThat(uri2.getSchemeSpecificPart(), is(nullValue()));
//        assertThat(uri2.toString(), is("ldap://[2001:db8::7]/c=GB?objectClass?one"));
        
        
        // real-world links that chrome handles :-(
        // https://fonts.googleapis.com/css?family=Roboto:100,300,300italic,400,500italic|Open+Sans:400&subset=latin
        
        //Uri uri = new Uri("https://fonts.googleapis.com/css?family=Roboto:100,300,300italic,400,500italic|Open+Sans:400&subset=latin");
        
        //assertThat(uri, is(not(nullValue())));
    }
    
    @Test
    public void navigate() {
        Uri uri;

        uri = Uri.navigate(null);
        assertThat(uri, is(nullValue()));
        
        uri = Uri.navigate("");
        assertThat(uri, is(nullValue()));
        
        uri = Uri.navigate("   ");
        assertThat(uri, is(nullValue()));
        
        uri = Uri.navigate("http://www.fizzed.com/");
        assertThat(uri.toString(), is("http://www.fizzed.com/"));
        
        uri = Uri.navigate("https://www.fizzed.com");
        assertThat(uri.toString(), is("https://www.fizzed.com/"));
        
        uri = Uri.navigate("    http://www.fizzed.com/    ");
        assertThat(uri.toString(), is("http://www.fizzed.com/"));
        
        uri = Uri.navigate("http://www.fizzed.com");
        assertThat(uri.toString(), is("http://www.fizzed.com/"));
        
        uri = Uri.navigate("www.fizzed.com");
        assertThat(uri.toString(), is("http://www.fizzed.com/"));
        
        uri = Uri.navigate("www.fizzed.com/dude");
        assertThat(uri.toString(), is("http://www.fizzed.com/dude"));
        
        uri = Uri.navigate("www.fizzed.com/dude?a=1");
        assertThat(uri.toString(), is("http://www.fizzed.com/dude?a=1"));
        
        uri = Uri.navigate("www.fizzed.com?a=1");
        assertThat(uri.toString(), is("http://www.fizzed.com/?a=1"));
        
        uri = Uri.navigate("www.fizzed.com?a=1#test");
        assertThat(uri.toString(), is("http://www.fizzed.com/?a=1#test"));
        
        uri = Uri.navigate("www.fizzed.com:81");
        assertThat(uri.toString(), is("http://www.fizzed.com:81/"));
        
        uri = Uri.navigate("www.fizzed.com:81/");
        assertThat(uri.toString(), is("http://www.fizzed.com:81/"));
        
        uri = Uri.navigate("www.fizzed.com:81/dude");
        assertThat(uri.toString(), is("http://www.fizzed.com:81/dude"));
        
        uri = Uri.navigate("fizzed.com:81/dude");
        assertThat(uri.toString(), is("http://fizzed.com:81/dude"));
        
        // this causes the original uri to fail parsing, but the navigate
        // method should work around that case as well
        uri = Uri.navigate("127.0.0.1:81/dude");
        assertThat(uri.toString(), is("http://127.0.0.1:81/dude"));
        
        uri = Uri.navigate("localhost:81");
        assertThat(uri.toString(), is("http://localhost:81/"));
        
        uri = Uri.navigate("localhost");
        assertThat(uri.toString(), is("http://localhost/"));
    }
    
}