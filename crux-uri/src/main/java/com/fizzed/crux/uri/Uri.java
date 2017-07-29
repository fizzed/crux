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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RFC 3986
 * 
 * The generic URI syntax consists of a hierarchical sequence of
 * components referred to as the scheme, authority, path, query, and
 * fragment.
 * 
 *  URI         = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
 * 
 *  hier-part   = "//" authority path-abempty
 *                / path-absolute
 *                / path-rootless
 *                / path-empty
 * 
 * The scheme and path components are required, though the path may be
 * empty (no characters).  When authority is present, the path must
 * either be empty or begin with a slash ("/") character.  When
 * authority is not present, the path cannot begin with two slash
 * characters ("//").  These restrictions result in five different ABNF
 * rules for a path (Section 3.3), only one of which will match any
 * given URI reference.
 */
public class Uri {

    protected String scheme;
    protected String userInfo;
    protected String host;
    protected Integer port;
    protected List<String> paths;
    protected Map<String,List<String>> query;
    protected String fragment;

    protected Uri() {
        // do nothing
    }
    
    public Uri(String uri) {
        this(new MutableUri(uri));
    }
    
    public Uri(Uri uri) {
        this(uri.scheme, uri.userInfo, uri.host, uri.port, uri.paths, uri.query, uri.fragment);
    }
    
    protected Uri(String scheme, String userInfo, String host, Integer port, List<String> paths, Map<String,List<String>> query, String fragment) {
        this.scheme = scheme;
        this.userInfo = userInfo;
        this.host = host;
        this.port = port;
        this.paths = copy(paths);
        this.query = copy(query);
        this.fragment = fragment;
    }
    
    public MutableUri mutable() {
        return new MutableUri(this);
    }
    
    public String getScheme() {
        return this.scheme;
    }
    
    public String getUserInfo() {
        return this.userInfo;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public Integer getPort() {
        return this.port;
    }
    
    public String getPath() {
        return this.encodedPath();
    }
    
    public List<String> getPaths() {
        return this.paths;
    }
    
    public Map<String,List<String>> getQuery() {
        return this.query;
    }
    
    public List<String> getQueryAll(String name) {
        if (this.query == null) {
            return null;
        }
        return this.query.get(name);
    }
    
    public String getQueryFirst(String name) {
        List<String> values = this.getQueryAll(name);
        
        if (values == null || values.isEmpty()) {
            return null;
        }
        
        return values.get(0);
    }
    
    public Map<String,String> getQueryFirstMap() {
        if (this.query == null) {
            return null;
        }
        
        Map<String,String> flatMap = new HashMap<>();
        this.query.forEach((key, values) -> {
            flatMap.put(key, values.get(0));
        });
        
        return flatMap;
    }
    
    public String getFragment() {
        return this.fragment;
    }
    
    protected final Map<String,List<String>> copy(Map<String,List<String>> map) {
        if (map == null) {
            return null;
        }
        
        Map<String,List<String>> copy = new LinkedHashMap<>();
        
        map.forEach((key, values) -> {
            copy.put(key, new ArrayList<>(values));
        });
        
        return copy;
    }
    
    protected final List<String> copy(List<String> list) {
        if (list == null) {
            return null;
        }
        
        return new ArrayList<>(list);
    }
    
    protected String encodedPath() {
        if (this.paths == null || this.paths.isEmpty()) {
            return null;
        }
        
        StringBuilder s = new StringBuilder();
        
        for (int i = 0; i < this.paths.size(); i++) {
            String path = this.paths.get(i);
            if (i != 0) {
                s.append('/');
            }
            s.append(MutableUri.encode(path));
        }
        
        return s.toString();
    }
    
    protected String encodedQueryString() {
        if (this.query == null || this.query.isEmpty()) {
            return null;
        }
        
        StringBuilder s = new StringBuilder();
        
        this.query.forEach((key, values) -> {
            values.forEach((value) -> {
                if (s.length() != 0) {
                    s.append("&");
                }
                s.append(key);
                if (value != null) {
                    s.append("=");
                    s.append(MutableUri.encode(value));
                }
            });
        });
        
        return s.toString();
    }
    
    @Override
    public String toString() {
        // Note this code is essentially a copy of 'java.net.URI.defineString',
        // which is private. We cannot use the 'new URI( scheme, userInfo, ... )' or
        // 'new URI( scheme, authority, ... )' constructors because they double
        // encode the query string using 'java.net.URI.quote'
        StringBuilder uri = new StringBuilder();
        
        if (this.scheme != null) {
            uri.append(this.scheme);
            uri.append(':');
        }
        
        if (this.host != null) {
            uri.append("//");
            
            if (this.userInfo != null) {
                uri.append(MutableUri.encode(this.userInfo));
                uri.append('@');
            }
            
            uri.append(this.host);
            
            if (this.port != null) {
                uri.append(':');
                uri.append(this.port);
            }
            
        }
        
        if (this.paths != null && !this.paths.isEmpty()) {
            uri.append(this.encodedPath());
        }

        if (this.query != null && !this.query.isEmpty()) {
            uri.append('?');
            uri.append(this.encodedQueryString());
        }
        
        if (this.fragment != null) {
            uri.append('#');
            uri.append(MutableUri.encode(this.fragment));
        }
       
        return uri.toString();
    }
    
}
