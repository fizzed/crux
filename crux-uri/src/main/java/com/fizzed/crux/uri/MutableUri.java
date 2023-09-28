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

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Helps to build a URI.  Why another one?  Unlike Java's URI this one allows
 * modification after it's been created. This one has a simple fluent style
 * to help build uris.
 *
 * https://en.wikipedia.org/wiki/Uniform_Resource_Identifier
 * URI = scheme ":" ["//" authority] path ["?" query] ["#" fragment]
 */
public class MutableUri extends Uri {

    public MutableUri() {
        // empty
    }
    
    public MutableUri(String uri) {
        // use our new string parser
        this.parse(uri);
    }
    
    public MutableUri(URI uri) {
        this.parse(uri);
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MutableUri(Uri uri) {
        this.scheme = uri.scheme;
        this.hasAuthority = uri.hasAuthority;
        this.userInfo = uri.userInfo;
        this.host = uri.host;
        this.port = uri.port;
        this.rels = copy(uri.rels);
        this.query = copy(uri.query);
        this.fragment = uri.fragment;
    }
    
    @Deprecated
    public Uri toImmutable() {
        return this.immutable();
    }
    
    public Uri immutable() {
        return new Uri(this.scheme, this.hasAuthority, this.userInfo, this.host, this.port, this.rels, this.query, this.fragment);
    }
    
    public Uri toUri() {
        return this.immutable();
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
     * Either sets an absolute or relative path.  The path MUST be url-encoded. If
     * the path starts with "/" then it will set the entire path, otherwise it will be
     * considered relative to the current path. If you want
     * to safely build a path and have this library do the url-encoding for you
     * then take a look at the rel() method as an alternative.
     * 
     * @param path The path to set such as "/a/b/c" if absolute or if the current
     *      path is "/a/b" and you call this method with "c" then the final
     *      path would be "/a/b/c".
     * @return This instance
     * @see #rel(java.lang.String[])
     */
    public MutableUri path(String path) {
        // clear it?
        if (path == null) {
            this.rels = null;
            return this;
        }

        List<String> newRels = splitPath(path, true);
        
        // if absolute then normalize it (chop off leading empty rel)
        if (path.length() > 0 && path.charAt(0) == '/') {
            newRels = normalizeRootPath(newRels);
            this.rels = null;
        }
        
        // create array if its missing
        if (this.rels == null) {
            this.rels = new ArrayList<>();
        }
        
        // append everything
        this.rels.addAll(newRels);

        return this;
    }
    
    /**
     * Only adds the relative path if the value is present in the optional.
     * @param rels One or more optionals.
     * @return 
     */
    public MutableUri relIfPresent(Optional<?>... rels) {
        if (rels == null || rels.length == 0) {
            return this;
        }
        
        for (Optional<?> rel : rels) {
            // optionals should always be non-null
            Objects.requireNonNull(rel, "rel was null");
            if (rel.isPresent()) {
                this.rel(rel.get());
            }
        }
        
        return this;
    }
    
    
    /**
     * Adds one or more relative path components as-is. If the existing path
     * is "/a/b" and you supply "c@/d" to this method then the underlying
     * path would be "/a/b/c%40%2fd". This is the recommended method to safely
     * build a path in a url.
     * @param rels One or more relative path components to add
     * @return This instance
     */
    public MutableUri rel(Object... rels) {
        if (rels == null || rels.length == 0) {
            return this;
        }
        
        String[] strings = new String[rels.length];
        
        for (int i = 0; i < rels.length; i++) {
            Object rel = rels[i];
            Objects.requireNonNull(rel, "rel was null");
            strings[i] = Objects.toString(rel);
        }
        
        return rel(strings);
    }
    
    /**
     * Adds one or more relative path components as-is. If the existing path
     * is "/a/b" and you supply "c@/d" to this method then the underlying
     * path would be "/a/b/c%40%2fd". This is the recommended method to safely
     * build a path in a url.
     * @param rels One or more relative path components to add
     * @return This instance
     */
    public MutableUri rel(String... rels) {
        if (rels == null || rels.length == 0) {
            return this;    // nothing to do
        }
        
        if (this.rels == null) {
            this.rels = new ArrayList<>();
        }
        
        for (String rel : rels) {
            Objects.requireNonNull(rel, "rel was null");
            this.rels.add(rel);
        }
        
        return this;
    }
    
    public MutableUri queryIfPresent(String name, Optional<?> value) {
        Objects.requireNonNull(name, "name was null");
        Objects.requireNonNull(name, "value was null");
        
        if (value.isPresent()) {
            this.query(name, value.get());
        }
        
        return this;
    }
    
    public MutableUri query(String name, Object value) {
        Objects.requireNonNull(name, "name was null");
        return this.query(name, Objects.toString(value, null));
    }
    
    public MutableUri query(String name, String value) {
        Objects.requireNonNull(name, "name was null");
        
        List<String> values = getQueryValues(name);
        
        values.add(value);

        return this;
    }
    
    /**
     * Adds the entire map as query values.
     * @param queryMap The map to add.  Must not be a null map.
     * @return 
     */
    public MutableUri query(Map<String,?> queryMap) {
        Objects.requireNonNull(queryMap, "queryMap was null");
        queryMap.forEach((name, value) -> {
            if (value instanceof Iterable) {
                Iterable it = (Iterable)value;
                it.forEach(v -> {
                    this.query(name, v);
                });
            } else {
                this.query(name, value);
            }
        });
        return this;
    }
    
    public MutableUri setQueryIfPresent(String name, Optional<?> value) {
        Objects.requireNonNull(name, "name was null");
        Objects.requireNonNull(name, "value was null");
        
        if (value.isPresent()) {
            this.setQuery(name, value.get());
        }
        
        return this;
    }
    
    public MutableUri setQuery(String name, Object value) {
        Objects.requireNonNull(name, "name was null");
        return this.setQuery(name, Objects.toString(value, null));
    }
    
    public MutableUri setQuery(String name, String value) {
        Objects.requireNonNull(name, "name was null");
        
        List<String> values = getQueryValues(name);
        
        values.clear();
        values.add(value);

        return this;
    }
    
    /**
     * Adds the entire map as query values.
     * @param queryMap The map to add or null to clear.  The value can either be
     *      a value or an Iterable of values.
     * @return 
     */
    public MutableUri setQuery(Map<String,?> queryMap) {
        if (this.query != null) {
            this.query.clear();
        }
        
        if (queryMap != null) {
            queryMap.forEach((name, value) -> {
                if (value instanceof Iterable) {
                    Iterable it = (Iterable)value;
                    it.forEach(v -> {
                        this.setQuery(name, v);
                    });
                } else {
                    this.setQuery(name, value);
                }
            });
        }
        
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
     * @param fragment Non url-encoded fragment or null to clear.
     * @return 
     */
    public MutableUri fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    private MutableUri parse(URI uri) {
        // parsing a URI as a string is WAY more reliable
        return this.parse(uri.toString());
    }

    private MutableUri parse(String uri) {
        // https://en.wikipedia.org/wiki/Uniform_Resource_Identifier
        // URI = scheme ":" ["//" authority] path ["?" query] ["#" fragment]
        // authority = [userinfo "@"] host [":" port]

        // we will implement our own parsing routine
        int pos = 0;
        int len = uri.length();

        // scheme is everything to start up to the "://" such as ldap://[2001:db8::7]/c=GB?objectClass?one
        boolean maybeHasAuthority = false;

        int schemeEndPos = uri.indexOf("://", pos);
        if (schemeEndPos > 0) {
            this.scheme = uri.substring(pos, schemeEndPos);
            pos = schemeEndPos + 3; // skip over :// on next part to parse
            maybeHasAuthority = true;
            this.hasAuthority = true;
        } else {
            // this could be a scheme:path scenario such as tel:+1-816-555-1212
            schemeEndPos = uri.indexOf(":", pos);
            if (schemeEndPos > 0) {
                this.scheme = uri.substring(pos, schemeEndPos);
                pos = schemeEndPos + 1; // skip over : on next part to parse
                // the rest is apparently always a path
            } else {
                // there isn't any scheme, the rest must be a path
                // we will simply continue on parsing it as a path
            }
        }

        boolean maybeHasPath = true;
        boolean maybeHasQuery = true;

        if (maybeHasAuthority) {
            int authorityEndPos = uri.indexOf("/", pos);
            if (authorityEndPos < 0) {
                // definitely no path
                maybeHasPath = false;
                // was it instead a ?
                authorityEndPos = uri.indexOf("?", pos);
                if (authorityEndPos < 0) {
                    maybeHasQuery = false;
                    // was it instead a #
                    authorityEndPos = uri.indexOf("#", pos);
                    if (authorityEndPos < 0) {
                        // the rest of the uri must be the authority
                        // e.g. https://john.doe@www.example.com:123
                        authorityEndPos = len;
                    }
                }
            }

            // any userinfo?
            int userInfoPos = uri.indexOf("@", pos);
            if (userInfoPos > 0 && userInfoPos <= authorityEndPos) {        // in case an @ symbol is AFTER the authority
                this.userInfo = urlDecode(uri.substring(pos, userInfoPos));
                pos = userInfoPos + 1;  // skip @ char
            }

            // we're left with a potential host[:port]
            int hostEndPos = uri.indexOf(":", pos);

            // does the host start with an "[" indicating its an ipv6 address?
            if (pos < len && uri.charAt(pos) == '[') {
                // find the next matching ]
                int closingSquarePos = uri.indexOf(']', pos);
                if (closingSquarePos < 0) {
                    throw new IllegalArgumentException("Invalid IPv6 host (no matching ] char)");
                }
                hostEndPos = uri.indexOf(":", closingSquarePos);
            }

            // was no port found?
            if (hostEndPos < 0 || hostEndPos >= authorityEndPos) {
                // host makes up rest of authority
                hostEndPos = authorityEndPos;
                this.host = trimAndBlankToNull(uri.substring(pos, hostEndPos));
            } else {
                // a port was found and we should parse it and the host
                this.host = trimAndBlankToNull(uri.substring(pos, hostEndPos));
                pos = hostEndPos + 1;       // skip over : char
                String portStr = uri.substring(pos, authorityEndPos);
                try {
                    this.port = Integer.parseInt(portStr);
                } catch (Exception e) {
                    throw new IllegalArgumentException("port " + portStr + " was not an integer in uri " + uri);
                }
            }

            pos = authorityEndPos;
        }

        // we may be done at this point
        if (pos >= len) {
            return this;
        }

        if (maybeHasPath) {
            // we're left with the path, query, fragment
            int pathEndPos = uri.indexOf("?", pos);
            if (pathEndPos < 0) {
                // we know there is no query
                maybeHasQuery = false;
                // we may need to read to the fragment
                pathEndPos = uri.indexOf("#", pos);
                if (pathEndPos < 0) {
                    // the rest of the uri is path
                    pathEndPos = len;
                }
            }

            final String rawPath = uri.substring(pos, pathEndPos);
            this.path(rawPath);
            pos = pathEndPos;

            // we may be done at this point
            if (pos >= len) {
                return this;
            }
        }

        if (maybeHasQuery) {
            // there must have been a ?, we'll skip over it
            pos++;

            int queryEndPos = uri.indexOf("#", pos);
            if (queryEndPos < 0) {
                // the rest of the uri is query
                queryEndPos = len;
            }

            final String rawQuery = uri.substring(pos, queryEndPos);
            // parse raw query now
            {
                // get rid of map to rebuild
                this.query = null;

                // split on ampersand...
                String[] pairs = rawQuery.split("&");
                for (String pair : pairs) {
                    String[] nv = pair.split("=");
                    switch (nv.length) {
                        case 1:
                            this.query(urlDecode(nv[0]), null);
                            break;
                        case 2:
                            this.query(urlDecode(nv[0]), urlDecode(nv[1]));
                            break;
                        default:
                            throw new IllegalArgumentException("Name value pair [" + pair + "] in query [" + rawQuery + "] missing = char");
                    }
                }
            }

            pos = queryEndPos;

            // we may be done at this point
            if (pos >= len) {
                return this;
            }
        }

        // there must have been a #, we'll skip over it
        pos++;

        // remaining part is now fragment
        String rawFragment = uri.substring(pos);
        this.fragment = urlDecode(rawFragment);
        
        return this;
    }
    
    static private List<String> normalizeRootPath(List<String> rels) {
        if (rels.size() < 2) {
            // no path at all
            return null;
        } else {
            // drop off the empty root
            return rels.subList(1, rels.size());
        }
    }
    
    static List<String> normalizeRels(List<String> rels) {
        if (rels == null || rels.isEmpty()) {
            return rels;
        }
        
        List<String> newRels = new ArrayList<>();
        
        for (String rel : rels) {
            switch (rel) {
                case ".":
                    // skip it
                    break;
                case "..":
                    // remove last newRel (if it exists)
                    if (newRels.size() > 0) {
                        newRels.remove(newRels.size()-1);
                    }
                    break;
                default:
                    newRels.add(rel);
                    break;
            }
        }
        
        return newRels;
    }
    
    // not sure we want this public (so package-level for now)
    static List<String> splitPath(String path, boolean decode) {
        List<String> paths = new ArrayList<>();
        splitPath(path, paths, decode);
        return paths;
    }
    
    // not sure we want this public (so package-level for now)
    static void splitPath(String path, List<String> paths, boolean decode) {
        int pos = 0;
        for (int i = 0; i < path.length(); i++) {
            // found slash or on last char?
            if (path.charAt(i) == '/') {
                String p = path.substring(pos, i);
                paths.add((decode ? urlDecode(p) : p));
                pos = i+1;
            }
        }
        // add last token?
        if (pos < path.length()) {
            String p = path.substring(pos);
            paths.add((decode ? urlDecode(p) : p));
        } else if (pos == path.length()) {
            // add an empty string at end
            paths.add("");
        }
    }

    static String trimAndBlankToNull(String s) {
        if (s != null) {
            s = s.trim();
            if (s.length() == 0) {
                return null;
            }
        }
        return s;
    }

}