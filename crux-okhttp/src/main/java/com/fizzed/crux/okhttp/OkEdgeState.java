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
package com.fizzed.crux.okhttp;

import com.fizzed.crux.okhttp.OkLoggingLevel;
import okhttp3.CookieJar;
import okhttp3.Credentials;
import okhttp3.Headers;

/**
 * State shared between OkEdge instances (e.g. cookie handling)
 */
public class OkEdgeState {
    
    static public final OkEdgeState DEFAULT_INSTANCE = new OkEdgeState();
    
    private OkLoggingLevel loggingLevel;
    private Boolean followRedirects;
    private Boolean insecure;
    private CookieJar cookieJar;
    private final Headers.Builder headersBuilder;

    public OkEdgeState() {
        this.headersBuilder = new Headers.Builder();
        this.cookieJar = null;
    }
    
    OkLoggingLevel logging() {
        return this.loggingLevel;
    }
    
    public OkEdgeState logging(OkLoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
        return this;
    }
    
    Boolean followRedirects() {
        return this.followRedirects;
    }
    
    public OkEdgeState followRedirects(Boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }
    
    Boolean insecure() {
        return this.insecure;
    }
    
    public OkEdgeState insecure(Boolean insecure) {
        this.insecure = insecure;
        return this;
    }
    
    CookieJar cookieJar() {
        return cookieJar;
    }
    
    public OkEdgeState cookies(boolean cookies) {
        if (cookies) {
            if (this.cookieJar == null) {
                this.cookieJar = new BasicInMemoryCookieJar();
            }
        } else {
            this.cookieJar = null;
        }
        return this;
    }
    
    public OkEdgeState basicAuth(String username, String password) {
        this.header(
            "Authorization",
            Credentials.basic(username, password));
        return this;
    }
    
    Headers.Builder headersBuilder() {
        return this.headersBuilder;
    }
    
    public OkEdgeState header(String name, String value) {
        this.headersBuilder.set(name, value);
        return this;
    }
    
    public OkEdgeState addHeader(String name, String value) {
        this.headersBuilder.add(name, value);
        return this;
    }
    
}
