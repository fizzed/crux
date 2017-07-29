/*
 * Copyright 2015 Fizzed, Inc.
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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Helps to build a URI.  Why another one?  Unlike Java's URI this one allows
 * modification after it's been created. This one has a simple fluent style
 * to help build uris.
 */
public class MutableUri extends Uri {

    public MutableUri() {
        // empty
    }
    
    public MutableUri(String uri) {
        this(URI.create(uri));
    }
    
    public MutableUri(URI uri) {
        this.apply(uri);
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MutableUri(Uri uri) {
        this.scheme = uri.scheme;
        this.userInfo = uri.userInfo;
        this.host = uri.host;
        this.port = uri.port;
        this.paths = copy(uri.paths);
        this.query = copy(uri.query);
        this.fragment = uri.fragment;
    }
    
    @Deprecated
    public Uri toImmutable() {
        return this.immutable();
    }
    
    public Uri immutable() {
        return new Uri(this.scheme, this.userInfo, this.host, this.port, this.paths, this.query, this.fragment);
    }
    
    public URI toURI() {
        // only way to correctly set query string
        return URI.create(toString());
    }
    
    public MutableUri scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }
    
    public MutableUri userInfo(String userInfo) {
        this.userInfo = userInfo;
        return this;
    }
    
    public MutableUri host(String host) {
        this.host = host;
        return this;
    }
    
    public MutableUri port(Integer port) {
        this.port = port;
        return this;
    }
    
    /**
     * Sets the url-encoded path. Note that it will be split into components
     * based on the '/' char so make sure its url-encoded.  If you need to
     * set the path as relative to the existing then use rel(). If you need to
     * set a value in the path that may need url-encoding then use fs().
     * 
     * @param path The path to set such as "/a/b/c"
     * @return This insance
     * @see #relPath(java.lang.String) 
     * @see #rel(java.lang.String[]) 
     */
    public MutableUri path(String path) {
        // clear it?
        if (path == null) {
            this.paths = null;
            return this;
        }
        
        // add leading '/' if its missing
        if (path.length() == 0 || path.charAt(0) != '/') {
            path = '/' + path;
        }
        
        // split + decode the path
        this.paths = splitPath(path, true);
        
        return this;
    }
    
    /**
     * Sets (adds) a relative url-encoded path to the existing path.  The path will be
     * split
     * @param path
     * @return 
     */
    public MutableUri relPath(String path) {
        // if current path is null or empty then this is simply path
        if (this.paths == null || this.paths.isEmpty()) {
            return this.path(path);
        }
        
        // split + decode the path
        splitPath(path, this.paths, true);
        
        return this;
    }
    
    /**
     * Adds one or more relative path components that are NOT url-encoded. This
     * is the ideal method to use for building urls.
     * @param paths
     * @return 
     */
    public MutableUri rel(Object... paths) {
        if (paths == null || paths.length == 0) {
            return this;
        }
        
        String[] strings = new String[paths.length];
        
        for (int i = 0; i < paths.length; i++) {
            strings[i] = Objects.toString(paths[i]);
        }
        
        return rel(strings);
    }
    
    /**
     * Adds one or more relative path components that are NOT url-encoded. This
     * is the ideal method to use for building urls.
     * @param paths
     * @return 
     */
    public MutableUri rel(String... paths) {
        if (paths == null || paths.length == 0) {
            return this;    // nothing to do
        }
        
        for (String path : paths) {
            if (this.paths == null) {
                this.paths = new ArrayList<>();
                this.paths.add(""); // since its first we need to add this
            }
            // do not decode
            this.paths.add(path);
        }
        
        return this;
    }
    
    public MutableUri query(String name, Object value) {
        Objects.requireNonNull(name, "name was null");
        return this.query(name, Objects.toString(value));
    }
    
    public MutableUri query(String name, String value) {
        Objects.requireNonNull(name, "name was null");
        
        List<String> values = getQueryValues(name);
        
        values.add(value);

        return this;
    }
    
    public MutableUri setQuery(String name, Object value) {
        Objects.requireNonNull(name, "name was null");
        Objects.requireNonNull(value, "value was null");
        return this.setQuery(name, Objects.toString(value));
    }
    
    public MutableUri setQuery(String name, String value) {
        Objects.requireNonNull(name, "name was null");
        Objects.requireNonNull(value, "value was null");
        
        List<String> values = getQueryValues(name);
        
        values.clear();
        values.add(value);

        return this;
    }
    
    private List<String> getQueryValues(String name) {
        Objects.requireNonNull(name, "name cannot be null");
        
        if (this.query == null) {
            this.query = new LinkedHashMap<>(); // order of insertion important
        }
        
        return this.query.computeIfAbsent(name, (key) -> new ArrayList<>());
    }
    
    /**
     * Sets the non url-encoded fragment.
     * @param fragment Non url-encoded fragment.
     * @return 
     */
    public MutableUri fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }
    
    private MutableUri apply(URI uri) {
        if (uri.getScheme() != null) {
            this.scheme = uri.getScheme();
        }
        
        if (uri.getRawUserInfo() != null) {
            this.userInfo = decode(uri.getRawUserInfo());
        }
        
        if (uri.getHost() != null) {
            this.host = uri.getHost();
        }
        
        if (uri.getPort() >= 0) {
            this.port = uri.getPort();
        }

        // if the uri contains reserved, punctuation, and other chars in the
        // host section of the uri, then those are actually set as the authority
        // we're going to make a design decision to try and parse it as the host
        // and potentially the port
        if (uri.getHost() == null && uri.getUserInfo() == null && uri.getAuthority() != null) {
            String authority = uri.getAuthority();
            int portIndex = authority.indexOf(':');
            if (portIndex >= 0) {
                this.host = authority.substring(0, portIndex);
                this.port = Integer.valueOf(authority.substring(portIndex+1));
            } else {
                this.host = authority;
            }
        }
        
        String rawPath = uri.getRawPath();
        if (rawPath != null && rawPath.length() > 0) {
            this.path(uri.getRawPath());
        }
        
        if (uri.getRawQuery() != null) {
            // get rid of map to rebuild
            this.query = null;
            
            // split on ampersand...
            String[] pairs = uri.getRawQuery().split("&");
            for (String pair : pairs) {
                String[] nv = pair.split("=");
                switch (nv.length) {
                    case 1:
                        this.query(decode(nv[0]), null);
                        break;
                    case 2:
                        this.query(decode(nv[0]), decode(nv[1]));
                        break;
                    default:
                        throw new IllegalArgumentException("Name value pair [" + pair + "] in query [" + uri.getRawQuery() + "] missing = char");
                }
            }
        }
        
        if (uri.getRawFragment() != null) {
            this.fragment = decode(uri.getRawFragment());
        }
        
        return this;
    }
    
    
    
    /*
    static public MutableUri of(String uri, Object... parameters) {
        String s = format(uri, parameters);
        return new MutableUri(s);
    }
    
    static public URI uri(String uri, Object... parameters) {
        String s = format(uri, parameters);
        return new MutableUri(s).toURI();
    }
    */
    
    static public List<String> splitPath(String path, boolean decode) {
        List<String> paths = new ArrayList<>();
        splitPath(path, paths, decode);
        return paths;
    }
    
    static public void splitPath(String path, List<String> paths, boolean decode) {
        int pos = 0;
        for (int i = 0; i < path.length(); i++) {
            // found slash or on last char?
            if (path.charAt(i) == '/') {
                String p = path.substring(pos, i);
                paths.add((decode ? decode(p) : p));
                pos = i+1;
            }
        }
        // add last token?
        if (pos < path.length()) {
            String p = path.substring(pos);
            paths.add((decode ? decode(p) : p));
        } else if (pos == path.length()) {
            // add an empty string at end
            paths.add("");
        }
    }
    
    static String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    static String decode(String value) {
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    /**
     * Builds a String that accepts place holders and replaces them with URL encoded
     * values.
     * @param uri A string with place holders (e.g. "http://localhost:{}/path?a={}&b={}", 80, "valForA", "valForB")
     * @param parameters
     * @return 
     */
    /*
    static private String format(String uri, Object... parameters) {
        if (parameters == null || parameters.length == 0 || !uri.contains("{}")) {
            return uri;
        }
        
        List<String> tokens = splitter(uri, "{}");
        
        // there should be tokens.length - 1 parameters supplied
        if (tokens.size() - 1 != parameters.length) {
            throw new IllegalArgumentException("Incorrect number of parameters (expected " + (tokens.size() - 1)
                + "; actual " + parameters.length + ")");
        }
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < tokens.size(); i++) {
            if (i > 0) {
                String v = parameters[i-1].toString();
                sb.append(encode(v));
            }
            sb.append(tokens.get(i));
        }
        
        return sb.toString();
    }
    
    static private List<String> splitter(String s, String delimiter) {
        List<String> tokens = new ArrayList<>();
        
        int index = 0;
        int matches = 0;
        while (index < s.length()) {
            int pos = s.indexOf(delimiter, index);
            
            tokens.add(s.substring(index, (pos < 0 ? s.length() : pos)));
            
            if (pos < 0) {
                break;
            }
            
            matches++;
            index = pos + delimiter.length();
        }
        
        // add an empty value at end if needed
        if (matches + 1 != tokens.size()) {
            tokens.add("");
        }
        
        return tokens;
    }
    */
    
    
}
