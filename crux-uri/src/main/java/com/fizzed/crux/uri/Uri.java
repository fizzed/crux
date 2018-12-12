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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    protected String schemeSpecificPart;
    protected String userInfo;
    protected String host;
    protected Integer port;
    // path consists of 0+ relative path components
    protected List<String> rels;
    protected Map<String,List<String>> query;
    protected String fragment;

    protected Uri() {
        // do nothing
    }
    
    public Uri(String uri) {
        this(new MutableUri(uri));
    }
    
    public Uri(URI uri) {
        this(new MutableUri(uri));
    }
    
    public Uri(Uri uri) {
        this(uri.scheme, uri.schemeSpecificPart, uri.userInfo, uri.host, uri.port, uri.rels, uri.query, uri.fragment);
    }
    
    protected Uri(String scheme, String schemeSpecificPart, String userInfo, String host, Integer port, List<String> rels, Map<String,List<String>> query, String fragment) {
        this.scheme = scheme;
        this.schemeSpecificPart = schemeSpecificPart;
        this.userInfo = userInfo;
        this.host = host;
        this.port = port;
        this.rels = copy(rels);
        this.query = copy(query);
        this.fragment = fragment;
    }
    
    public MutableUri mutable() {
        return new MutableUri(this);
    }
    
    public MutableUri toMutableUri() {
        return this.mutable();
    }
    
    public URI toURI() {
        // only way to correctly set query string
        return URI.create(toString());
    }
    
    public String getScheme() {
        return this.scheme;
    }
    
    public String getSchemeSpecificPart() {
        return this.schemeSpecificPart;
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
    
    /**
     * Gets the url-encoded path.
     * @return The url-encoded path.
     * @see #getRels()
     */
    public String getPath() {
        return this.encodedPath();
    }
    
    /**
     * Gets the raw path components that will be used to build the url-encoded
     * path.
     * @return Null if no path is set or one ore more relative path components.
     *      The root path will always be an empty string.
     */
    public List<String> getRels() {
        return this.rels;
    }
    
    public String getRel(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Invalid index (< 0)");
        }
        if (rels == null || index >= this.rels.size()) {
            return null;
        }
        return this.rels.get(index);
    }
    
    /**
     * Gets the url-encoded query string or null if no query values exist.
     * @return The url-encoded query string
     */
    public String getQueryString() {
        return this.encodedQueryString();
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
    
    public boolean isAbsolute() {
        return this.scheme != null;
    }
    
    public Uri resolve(String otherUrl) {
        if (otherUrl == null) {
            return null;
        }
        
        otherUrl = otherUrl.trim();
        if (otherUrl.length() == 0) {
            return null;
        }
        
        // same scheme as us?
        if (otherUrl.startsWith("//")) {
            return new Uri(this.getScheme() + ":" + otherUrl);
        }
        
        // time to parse the other url
        Uri otherUri = new Uri(otherUrl);
        
        if (otherUri.isAbsolute()) {
            return otherUri;
        }
        
        // calculate new rels by overlying new relative path
        List<String> newRels = null;
        
        if (otherUrl.startsWith("/")) {
            // new path is absolute
            newRels = otherUri.getRels();
        } else {
            newRels = new ArrayList<>();
            
            // copy all rels
            if (this.getRels() != null) {
                newRels.addAll(this.getRels());
            }

            // remove last rel (if it exists) to resolve against current directory
            if (newRels.size() > 0) {
                newRels.remove(newRels.size()-1);
            }

            if (otherUri.getRels() != null) {
                newRels.addAll(otherUri.getRels());
            }
        }
        
        MutableUri resolvedUri = this.toMutableUri()
            .setQuery(otherUri.getQuery())
            .fragment(otherUri.getFragment());
        
        // TODO: should we allow users to set rels with a list?
        resolvedUri.rels = MutableUri.normalizeRels(newRels);
        
        return resolvedUri.immutable();
    }
    
    /**
     * Attempts to mimic a web browser navigate bar by interpreting the request
     * differently than as the RFC specs dictate for parsing URIs.  For example,
     * the RFC specs indicate text of of "www.example.com" would be parsed as
     * the path of the URI, when a browser would parse that as though the user
     * typed "http://www.example.com/".  The path is sanitized for http requests
     * to for empty paths to the root path of "/".
     * @param maybeUrl The possible url
     * @return The detected, well-formed Uri
     */
    static public Uri navigate(String maybeUrl) {
        if (maybeUrl == null) {
            return null;
        }
        
        maybeUrl = maybeUrl.trim();
        if (maybeUrl.length() == 0) {
            return null;
        }
        
        // we parse url to see if an absolute url was provided
        MutableUri maybeUri = null;
        try {
            // some case of input may cause a parsing exception, in which case
            // we'll try to fix it and try parsing again
            maybeUri = new MutableUri(maybeUrl);
        } catch (IllegalArgumentException e) {
            if (!maybeUrl.contains("://")) {
                maybeUri = new MutableUri("http://" + maybeUrl);
            } else {
                throw e;    // rethrow it
            }
        }
     
        if (!maybeUri.isAbsolute()) {
            // assume something like www.example.com was entered and the user
            // really meant to specify the host first (rather than it being
            // interpreted at the path per the specs of a URL
            maybeUri = new MutableUri("http://" + maybeUrl);
        }
        
        // edge case: www.example.com:81 would be parsed as simply a scheme
        // with a schemeSpecificPart -- we'll detect this unique case by 
        // determining if the scheme contains a period and assume the user
        // really meant it as a domain name
        if (maybeUri.getScheme() != null && maybeUri.getSchemeSpecificPart() != null
                && maybeUri.getHost() == null) {
            maybeUri = new MutableUri("http://" + maybeUrl);
        }
        
        // if no path was supplied then the default is / for http or https
        String path = maybeUri.getPath();
        if (path == null || path.equals("")) {
            if (maybeUri.getScheme().equalsIgnoreCase("http") || maybeUri.getScheme().equalsIgnoreCase("https")) {
                maybeUri.path("/");
            }
        }
        
        return maybeUri.toUri();
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
        if (this.rels == null || this.rels.isEmpty()) {
            return null;
        }
        
        StringBuilder s = new StringBuilder();
        
        for (int i = 0; i < this.rels.size(); i++) {
            String path = this.rels.get(i);
            s.append('/');
            s.append(MutableUri.urlEncode(path));
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
                    s.append(MutableUri.urlEncode(value));
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
        // urlEncode the query string using 'java.net.URI.quote'
        StringBuilder uri = new StringBuilder();
        boolean afterScheme = false;
        
        if (this.scheme != null) {
            uri.append(this.scheme);
            if (this.schemeSpecificPart != null) {
                uri.append(':');
                uri.append(this.schemeSpecificPart);
            }
        } else {
            afterScheme = true;
        }
        
        if (this.host != null) {
            if (!afterScheme) {
                uri.append(':');
                afterScheme = true;
            }
            
            uri.append("//");
            
            if (this.userInfo != null) {
                uri.append(MutableUri.urlEncode(this.userInfo));
                uri.append('@');
            }
            
            uri.append(this.host);
            
            if (this.port != null) {
                uri.append(':');
                uri.append(this.port);
            }
            
        }
        
        if (this.rels != null && !this.rels.isEmpty()) {
            if (!afterScheme) {
                uri.append(':');
                afterScheme = true;
            }
            uri.append(this.encodedPath());
        }

        if (this.query != null && !this.query.isEmpty()) {
            if (!afterScheme) {
                uri.append(':');
                afterScheme = true;
            }
            uri.append('?');
            uri.append(this.encodedQueryString());
        }
        
        if (this.fragment != null) {
            uri.append('#');
            uri.append(MutableUri.urlEncode(this.fragment));
        }
       
        return uri.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.toString());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Uri other = (Uri) obj;
        if (!Objects.equals(this.toString(), other.toString())) {
            return false;
        }
        return true;
    }
    
    static String urlEncode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    static String urlDecode(String value) {
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
}
