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

import java.io.IOException;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkEdge {
    
    private final HttpLoggingInterceptor loggingInterceptor;
    private final OkHttpClient.Builder clientBuilder;
    private final Request.Builder requestBuilder;
    private Boolean insecure;
    private LoggingLevel loggingLevel;
    
    public OkEdge() {
        this(null);
    }
    
    public OkEdge(OkEdgeState state) {
        this.loggingInterceptor  = new HttpLoggingInterceptor();
        this.clientBuilder = new OkHttpClient.Builder()
            .addNetworkInterceptor(this.loggingInterceptor);
        this.requestBuilder = new Request.Builder();
        // other initial state passed on from state
        if (state != null) {
            init(state);
        } else {
            init(OkEdgeState.DEFAULT_INSTANCE);
        }
    }
    
    // simply for convenience with source readability in some usage scenarios
    
    static public OkEdge create() {
        return new OkEdge();
    }
    
    static public OkEdge create(OkEdgeState state) {
        return new OkEdge(state);
    }
    
    private void init(OkEdgeState state) {
        if (state.cookieJar() != null) {
            this.clientBuilder.cookieJar(state.cookieJar());
        }
        if (state.logging() != null) {
            this.logging(state.logging());
        }
        if (state.followRedirects() != null) {
            this.followRedirects(state.followRedirects());
        }
        if (state.insecure() != null) {
            this.insecure(state.insecure());
        }
        this.requestBuilder.headers(state.headersBuilder().build());
    }
    
    public OkEdge followRedirects(boolean followRedirects) {
        this.clientBuilder.followRedirects(followRedirects);
        this.clientBuilder.followSslRedirects(followRedirects);
        return this;
    }
    
    public OkEdge insecure(Boolean insecure) {
        this.insecure = insecure;
        return this;
    }
    
    static public enum LoggingLevel {
        NONE, BASIC, HEADERS, BODY
    }
    
    /**
     * Logs nothing, basic, to headers, or to the body. Please note that logging
     * the body requires buffering the entire response first, logging it, and
     * then letting the application use it.
     * @param loggingLevel The level got log to
     * @return This instance
     */
    public OkEdge logging(LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
        
        if (loggingLevel == null) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            return this;
        }
        
        switch (loggingLevel) {
            case NONE:
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                break;
            case BASIC:
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                break;
            case HEADERS:
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                break;
            case BODY:
                // do not ever log using square's body logger!
                // they actually do an awful job of handling body
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                break;
        }
        
        return this;
    }
    
    public OkEdge basicAuth(String username, String password) {
        requestBuilder.header(
            "Authorization",
            Credentials.basic(username, password));
        return this;
    }
    
    public OkEdge header(String name, String value) {
        requestBuilder.header(name, value);
        return this;
    }
    
    public OkEdge addHeader(String name, String value) {
        requestBuilder.addHeader(name, value);
        return this;
    }
    
    // GET
    
    public OkEdge get(String url) {
        requestBuilder.get();
        requestBuilder.url(url);
        return this;
    }
    
    public OkEdge getJson(String url) {
        requestBuilder.get();
        requestBuilder.url(url);
        requestBuilder.addHeader("Accept", "application/json");
        return this;
    }
    
    // POST
    
    public OkEdgeForm postForm(String url) {
        return methodForm("POST", url);
    }
    
    public OkEdgeBody postJson(String url) {
        return methodJson("POST", url);
    }
    
    // PUT
    
    public OkEdgeForm putForm(String url) {
        return methodForm("PUT", url);
    }
    
    public OkEdgeBody putJson(String url) {
        return methodJson("PUT", url);
    }
    
    // PATCH
    
    public OkEdgeForm patchForm(String url) {
        return methodForm("PATCH", url);
    }
    
    public OkEdgeBody patchJson(String url) {
        return methodJson("PATCH", url);
    }
    
    // GENERIC METHOD
    
    public OkEdgeForm methodForm(String method, String url) {
        requestBuilder.url(url);
        final OkEdgeForm form = new OkEdgeForm(this);
        form.callback(() -> {
            requestBuilder.method(method, form.getFormBodyBuilder().build());
        });
        return form;
    }
    
    public OkEdgeBody methodJson(String method, String url) {
        requestBuilder.url(url);
        requestBuilder.addHeader("Accept", "application/json");
        final OkEdgeBody body = new OkEdgeBody(this, "application/json; charset=utf-8");
        body.callback(() -> {
            requestBuilder.method(method, body.getRequestBody());
        });
        return body;
    }
    
    public Response execute() throws IOException {
        // swap in values before client built...
        if (insecure != null && insecure) {
            this.clientBuilder.sslSocketFactory(OkHttpUtils.TRUST_ALL_SSL_SOCKET_FACTORY);
            this.clientBuilder.hostnameVerifier(OkHttpUtils.TRUST_ALL_HOSTNAME_VERIFIER);
        }
        
        OkHttpClient client = this.clientBuilder.build();
        Request request = this.requestBuilder.build();
        Response response = client.newCall(request).execute();
        
        // network-level logging interceptor won't log encoded bodies
        if (this.loggingLevel != null && this.loggingLevel == LoggingLevel.BODY) {
            ResponseBodyLogger.log(response, response.body());
        }
        
        return response;
    }
    
}
