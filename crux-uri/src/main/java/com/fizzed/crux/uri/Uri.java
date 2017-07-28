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
    protected String path;
    protected Map<String,List<String>> query;
    protected String fragment;

    protected Uri() {
        // do nothing
    }
    
    public Uri(Uri uri) {
        this(uri.scheme, uri.userInfo, uri.host, uri.port, uri.path, uri.query, uri.fragment);
    }
    
    public Uri(String scheme, String userInfo, String host, Integer port, String path, Map<String, List<String>> query, String fragment) {
        this.scheme = scheme;
        this.userInfo = userInfo;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
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
        return this.path;
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
    
    protected Map<String,List<String>> copy(Map<String,List<String>> map) {
        if (map == null) {
            return null;
        }
        
        Map<String,List<String>> copy = new LinkedHashMap<>();
        
        map.forEach((key, values) -> {
            copy.put(key, new ArrayList<>(values));
        });
        
        return copy;
    }
    
}
