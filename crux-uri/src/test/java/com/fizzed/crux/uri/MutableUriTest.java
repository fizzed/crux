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

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MutableUriTest {

    @Test
    public void splitPath() {
        List<String> paths;
        
        paths = MutableUri.splitPath("", true);
        assertThat(paths, is(asList("")));
        
        paths = MutableUri.splitPath("/", true);
        assertThat(paths, is(asList("", "")));
        
        paths = MutableUri.splitPath("test", true);
        assertThat(paths, is(asList("test")));
        
        paths = MutableUri.splitPath("/test", true);
        assertThat(paths, is(asList("", "test")));
        
        paths = MutableUri.splitPath("/test/", true);
        assertThat(paths, is(asList("", "test", "")));
        
        paths = MutableUri.splitPath("/test/t", true);
        assertThat(paths, is(asList("", "test", "t")));
    }
    
    @Test
    public void normalizeRels() {
        List<String> rels;
        
        rels = MutableUri.normalizeRels(asList());
        assertThat(rels, is(asList()));
        
        rels = MutableUri.normalizeRels(asList("a"));
        assertThat(rels, is(asList("a")));
        
        rels = MutableUri.normalizeRels(asList("a", "b"));
        assertThat(rels, is(asList("a", "b")));
        
        rels = MutableUri.normalizeRels(asList("a", ".", "b"));
        assertThat(rels, is(asList("a", "b")));
        
        rels = MutableUri.normalizeRels(asList("a", "..", "b"));
        assertThat(rels, is(asList("b")));
        
        rels = MutableUri.normalizeRels(asList("..", "..", "b"));
        assertThat(rels, is(asList("b")));
    }
    
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
        assertThat(uri.getQueryAll("a"), is(asList("1", "2")));
        assertThat(uri.getQueryFirst("b"), is("2"));
        assertThat(uri.getQuery(), hasKey("c"));
        assertThat(uri.getFragment(), is("frag"));

        // only a fragment, no query
        uri = new MutableUri("http://user1@localhost:8080#frag");

        assertThat(uri.getScheme(), is("http"));
        assertThat(uri.getUserInfo(), is("user1"));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is("frag"));
    }

    @Test
    public void parseIpAddress() {
        MutableUri uri;

        uri = new MutableUri("ws://127.0.0.1");

        assertThat(uri.getScheme(), is("ws"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("127.0.0.1"));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("ws://127.0.0.1"));

        uri = new MutableUri("ws://127.0.0.1:8080");

        assertThat(uri.getScheme(), is("ws"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("127.0.0.1"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("ws://127.0.0.1:8080"));

        // ipv6
        uri = new MutableUri("ws://[2001:db8::7]");

        assertThat(uri.getScheme(), is("ws"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("[2001:db8::7]"));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("ws://[2001:db8::7]"));

        // ipv6
        uri = new MutableUri("ws://[2001:db8::7]:8080");

        assertThat(uri.getScheme(), is("ws"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("[2001:db8::7]"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("ws://[2001:db8::7]:8080"));
    }

    @Test
    public void parsePath() {
        MutableUri uri;

        uri = new MutableUri("/");

        assertThat(uri.getScheme(), is(nullValue()));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is("/"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("/"));

        uri = new MutableUri("/?a=1");

        assertThat(uri.getScheme(), is(nullValue()));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is("/"));
        assertThat(uri.getQuery(), hasKey("a"));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("/?a=1"));

        uri = new MutableUri("/#frag");

        assertThat(uri.getScheme(), is(nullValue()));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is("/"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is("frag"));
        assertThat(uri.toString(), is("/#frag"));
    }

    @Test
    public void parseOther() {
        MutableUri uri;

        uri = new MutableUri("mailto:John.Doe@example.com");

        assertThat(uri.getScheme(), is("mailto"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is("John.Doe@example.com"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("mailto:John.Doe@example.com"));

        uri = new MutableUri("news:comp.infosystems.www.servers.unix");

        assertThat(uri.getScheme(), is("news"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is("comp.infosystems.www.servers.unix"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("news:comp.infosystems.www.servers.unix"));

        uri = new MutableUri("tel:+1-816-555-1212");

        assertThat(uri.getScheme(), is("tel"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is("+1-816-555-1212"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("tel:+1-816-555-1212"));
    }

    @Test
    public void parseJdbcUrl() {
        MutableUri uri;

        uri = new MutableUri("jdbc:mysql://localhost");

        assertThat(uri.getScheme(), is("jdbc:mysql"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("jdbc:mysql://localhost"));

        uri = new MutableUri("jdbc:mysql://localhost:8080");

        assertThat(uri.getScheme(), is("jdbc:mysql"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getPath(), is(nullValue()));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("jdbc:mysql://localhost:8080"));

        uri = new MutableUri("jdbc:mysql://localhost:8080/this/is/a/path?a=1&b=2");

        assertThat(uri.getScheme(), is("jdbc:mysql"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("localhost"));
        assertThat(uri.getPort(), is(8080));
        assertThat(uri.getQuery(), hasEntry("a", asList("1")));
        assertThat(uri.getQuery(), hasEntry("b", asList("2")));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("jdbc:mysql://localhost:8080/this/is/a/path?a=1&b=2"));
    }
    
    @Test
    public void parseFile() {
        MutableUri uri;
        
        uri = new MutableUri("file:///path/to/file");
        
        assertThat(uri.getScheme(), is("file"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is("/path/to/file"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is("file:///path/to/file"));
    }
    
    @Test
    public void parseUrisWithAuthority() {
        MutableUri uri;
        String s;
        
        // puncuation in host is usually in authority, but this lib sets it as host
        s = "local://./path/to/file";
        uri = new MutableUri(s);
        
        assertThat(uri.getScheme(), is("local"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("."));
        assertThat(uri.getPort(), is(nullValue()));
        assertThat(uri.getPath(), is("/path/to/file"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is(s));
        
        // modify it then make sure its good
        assertThat(uri.host("localhost").toString(), is("local://localhost/path/to/file"));
        
        // with port (special parsing by this lib)
        s = "local://.:80/path/to/file";
        uri = new MutableUri(s);
        
        assertThat(uri.getScheme(), is("local"));
        assertThat(uri.getUserInfo(), is(nullValue()));
        assertThat(uri.getHost(), is("."));
        assertThat(uri.getPort(), is(80));
        assertThat(uri.getPath(), is("/path/to/file"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
        assertThat(uri.toString(), is(s));
        
        // other puncuation
        uri = new MutableUri("local://joe@c$wd:80/path/to/file");
        
        assertThat(uri.getScheme(), is("local"));
        assertThat(uri.getUserInfo(), is("joe"));
        assertThat(uri.getHost(), is("c$wd"));
        assertThat(uri.getPort(), is(80));
        assertThat(uri.getPath(), is("/path/to/file"));
        assertThat(uri.getQuery(), is(nullValue()));
        assertThat(uri.getFragment(), is(nullValue()));
    }
    
    @Test
    public void stringify() {
        MutableUri uri;
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost");
        
        assertThat(uri.toString(), is("http://localhost"));
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .port(8080);
        
        assertThat(uri.toString(), is("http://localhost:8080"));
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .query("a", "1")
            .query("b", "2");
        
        assertThat(uri.toString(), is("http://localhost:8080?a=1&b=2"));
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .path("/")
            .query("a", "1")
            .query("b", "2");
        
        assertThat(uri.toString(), is("http://localhost:8080/?a=1&b=2"));
        
        uri = new MutableUri()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .path("/")
            .query("a", "1")
            .query("b", "2")
            .fragment("frag");
        
        assertThat(uri.toString(), is("http://localhost:8080/?a=1&b=2#frag"));
        
        uri = new MutableUri()
            .scheme("http")
            .userInfo("user1")
            .host("localhost")
            .port(8080)
            .path("/")
            .query("a", "1")
            .query("b", "2")
            .fragment("frag");
        
        assertThat(uri.toString(), is("http://user1@localhost:8080/?a=1&b=2#frag"));
        
        uri = new MutableUri()
            .scheme("http")
            .userInfo("user1")
            .host("localhost")
            .port(8080)
            .path("/this/is/a/path")
            .query("a", "1")
            .query("b", "2")
            .fragment("frag");
        
        assertThat(uri.toString(), is("http://user1@localhost:8080/this/is/a/path?a=1&b=2#frag"));
        
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
            .fragment("frag");
        
        assertThat(uri.toString(), is("http://user1@localhost:8080/this/is/a/path?a=1&a=2&b=2&c#frag"));
        
        // multiple parameters & requiring encoding
        uri = new MutableUri()
            .scheme("http")
            .userInfo("user@1")
            .host("localhost")
            .port(8080)
            .path("/this/is/a/path")
            .query("a", "@")
            .query("a", "2")
            .query("b", 2)
            .query("c", null)
            .fragment("fr@g");
        
        assertThat(uri.toString(), is("http://user%401@localhost:8080/this/is/a/path?a=%40&a=2&b=2&c#fr%40g"));
    }
    
    @Test
    public void queryIfPresent() {
        String uri;
        
        uri = new MutableUri("http://localhost")
            .queryIfPresent("a", Optional.of(1L))
            .toString();
        
        assertThat(uri, is("http://localhost?a=1"));
        
        uri = new MutableUri("http://localhost")
            .queryIfPresent("a", Optional.ofNullable(null))
            .toString();
        
        assertThat(uri, is("http://localhost"));
    }
    
    @Test
    public void setQueryIfPresent() {
        String uri;
        
        uri = new MutableUri("http://localhost")
            .setQueryIfPresent("a", Optional.of(1L))
            .toString();
        
        assertThat(uri, is("http://localhost?a=1"));
        
        uri = new MutableUri("http://localhost")
            .query("a", "2")
            .setQueryIfPresent("a", Optional.ofNullable("1"))
            .toString();
        
        assertThat(uri, is("http://localhost?a=1"));
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
    
    @Test
    public void queryWithMap() {
        MutableUri uri;
        
        uri = new MutableUri("http://localhost?a=1&b=2");
        
        // either single value OR iterable :-)
        Map<String,Object> query = new LinkedHashMap<>();
        query.put("a", "2");
        query.put("b", asList("3"));
        
        uri.query(query);
        
        // order within each name is only retained
        assertThat(uri.toString(), is("http://localhost?a=1&a=2&b=2&b=3"));
    }
    
    @Test
    public void setQueryWithMap() {
        MutableUri uri;
        
        uri = new MutableUri("http://localhost?a=1&b=2&c=3");
        
        Map<String,Object> query = new LinkedHashMap<>();
        query.put("a", "2");
        query.put("b", asList("3"));
        
        uri.setQuery(query);
        
        // order within each name is only retained
        assertThat(uri.toString(), is("http://localhost?a=2&b=3"));
    }
    
    @Test
    public void pathAsRelative() {
        MutableUri uri;
        
        uri = new MutableUri("t://l")
            .path("");
        
        assertThat(uri.toString(), is("t://l/"));
        
        uri = new MutableUri("t://l")
            .path("/");
        
        assertThat(uri.toString(), is("t://l/"));
        
        uri = new MutableUri("t://l")
            .path("a").path("b");
        
        assertThat(uri.toString(), is("t://l/a/b"));
        
        uri = new MutableUri("t://l")
            .path("a/b");
        
        assertThat(uri.toString(), is("t://l/a/b"));
        
        uri = new MutableUri("t://l")
            .path("a/b").path("c/d");
        
        assertThat(uri.toString(), is("t://l/a/b/c/d"));
        
        uri = new MutableUri("t://l")
            .path("a/b").path("c/d/");
        
        assertThat(uri.toString(), is("t://l/a/b/c/d/"));
        
        uri = new MutableUri("t://l")
            .path("a/b").path("c/d").path("");
        
        assertThat(uri.toString(), is("t://l/a/b/c/d/"));
        
        uri = new MutableUri("t://l/api/v1")
            .path("test");
        
        assertThat(uri.toString(), is("t://l/api/v1/test"));
        assertThat(uri.getRels(), is(asList("api", "v1", "test")));
        assertThat(uri.getRel(0), is("api"));
        assertThat(uri.getRel(4), is(nullValue()));
        
        // relative then absolute
        uri = new MutableUri("t://l/api/v1")
            .path("test")
            .path("/a");
        
        assertThat(uri.toString(), is("t://l/a"));
    }
    
    @Test
    public void relWithNull() {
        MutableUri uri;
        
        uri = new MutableUri("t://l/test");
        
        try {
            uri.rel((String)null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
        
        assertThat(uri.toString(), is("t://l/test"));
    }
    
    @Test
    public void rel() {
        MutableUri uri;
        
        uri = new MutableUri("t://l")
            .rel("");
        
        assertThat(uri.toString(), is("t://l/"));
        
        uri = new MutableUri("t://l")
            .rel("/");
        
        assertThat(uri.toString(), is("t://l/%2F"));
        
        uri = new MutableUri("t://l")
            .rel("a").rel("b");
        
        assertThat(uri.toString(), is("t://l/a/b"));
        
        uri = new MutableUri("t://l")
            .rel("a@b");
        
        assertThat(uri.toString(), is("t://l/a@b"));
        assertThat(uri.getRel(0), is("a@b"));
        
        uri = new MutableUri("t://l")
            .rel("a@b", "c");
        
        assertThat(uri.toString(), is("t://l/a@b/c"));
        
        uri = new MutableUri("t://l")
            .rel("a@b", 80);
        
        assertThat(uri.toString(), is("t://l/a@b/80"));
    }
    
    @Test
    public void relIfPresent() {
        MutableUri uri;
        
        uri = new MutableUri("t://l");
        
        try {
            uri.relIfPresent((Optional<?>)null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
        
        assertThat(uri.toString(), is("t://l"));
        
        uri = new MutableUri("t://l")
            .relIfPresent(Optional.ofNullable(null));
        
        assertThat(uri.toString(), is("t://l"));

        uri = new MutableUri("t://l")
            .relIfPresent(Optional.ofNullable(null), Optional.of("test"));
        
        assertThat(uri.toString(), is("t://l/test"));
    }
    
    @Test
    public void fixIncorrectRelativePath() {
        MutableUri uri;
        
        uri = new MutableUri("http://localhost?a=1");
        
        assertThat(uri.getPath(), is(nullValue()));
        
        uri.path("test");
        
        assertThat(uri.toString(), is("http://localhost/test?a=1"));
    }
    
    @Test
    public void pathWithNull() {
        MutableUri uri;
        
        uri = new MutableUri("http://localhost/a");
        
        assertThat(uri.getPath(), is("/a"));
        
        uri.path(null);
        
        assertThat(uri.getPath(), is(nullValue()));
    }
    
    @Test
    public void buildProgrammatically() throws URISyntaxException {
        MutableUri uri;
        
        Path baseDir = Paths.get("/tmp/pronto");

        uri = new MutableUri()
            .scheme("local")
            .host(null)
            .path("/tmp/pronto");
        
        // this is identical to how java.net.URI would construct it
        assertThat(uri.toString(), is("local:/tmp/pronto"));
        
        uri = new MutableUri()
            .scheme("local")
            .host("")
            .path("/tmp/pronto");
        
        // this is identical to how java.net.URI would construct it
        assertThat(uri.toString(), is("local:///tmp/pronto"));
    }
    
    @Test
    public void buildProgrammatically2() throws URISyntaxException {
        Uri uri = new MutableUri()
            .scheme("https")
            .host("fizzed.com")
            .path("/contact")
            .query("a", 1);
        
        // this is identical to how java.net.URI would construct it
        assertThat(uri.toString(), is("https://fizzed.com/contact?a=1"));
    }
    
}
