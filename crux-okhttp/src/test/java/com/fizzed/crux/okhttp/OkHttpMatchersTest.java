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

import static com.fizzed.crux.okhttp.OkHttpMatchers.hasAnyStatusCode;
import static com.fizzed.crux.okhttp.OkHttpMatchers.hasCharset;
import static com.fizzed.crux.okhttp.OkHttpMatchers.hasContentType;
import static com.fizzed.crux.okhttp.OkHttpMatchers.hasEmptyBody;
import static com.fizzed.crux.okhttp.OkHttpMatchers.hasHeader;
import static com.fizzed.crux.okhttp.OkHttpMatchers.hasHeaderIgnoringCase;
import static com.fizzed.crux.okhttp.OkHttpMatchers.hasStatusCode;
import static com.fizzed.crux.okhttp.OkHttpMatchers.isRedirect;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class OkHttpMatchersTest {
    
    private final Request request
        = new Request.Builder()
            .url("http://example.com")
            .build();
    
    @Test
    public void tryHasStatusCode() {
        Response response = new Response.Builder().request(request)
            .code(200)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .build();
        
        assertThat(response, hasStatusCode(200));
        assertThat(response, not(hasStatusCode(201)));
    }
    
    @Test
    public void tryHasAnyStatusCode() {
        Response response = new Response.Builder().request(request)
            .code(200)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .build();
        
        assertThat(response, hasAnyStatusCode(201, 202, 200));
        assertThat(response, not(hasAnyStatusCode(201, 202, 203)));
    }
    
    @Test
    public void tryHasContentType() {
        Response response = new Response.Builder().request(request)
            .code(200)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .header("content-type", "application/json")
            .build();
        
        assertThat(response, hasContentType("application/json"));
        assertThat(response, not(hasContentType("text/html")));
        // response did not include a charset, therefore this exact match failed
        assertThat(response, not(hasContentType("application/json; charset=utf-8")));
    }
    
    @Test
    public void tryHasCharset() {
        Response response = new Response.Builder().request(request)
            .code(200)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .header("content-type", "application/json; charset=utf-8")
            .build();
        
        assertThat(response, hasCharset("utf-8"));
        assertThat(response, not(hasCharset("iso8859-1")));
    }
    
    @Test
    public void tryHasHeader() {
        Response response = new Response.Builder().request(request)
            .code(200)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .header("content-type", "application/json; charset=utf-8")
            .header("transfer-encoding", "chunked")
            .build();
        
        assertThat(response, hasHeader("transfer-encoding"));
        assertThat(response, hasHeader("transfer-encoding", "chunked"));
        assertThat(response, hasHeaderIgnoringCase("transfer-encoding", "ChUnKed"));
        assertThat(response, not(hasHeader("content-encoding")));
    }
    
    @Test
    public void tryIsRedirect() {
        Response response = new Response.Builder().request(request)
            .code(302)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .header("Location", "/go/here")
            .build();
        
        assertThat(response, isRedirect());
    }
    
    @Test
    public void tryHasEmptyBody() {
        Response response = new Response.Builder().request(request)
            .code(302)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .build();
        
        assertThat(response, hasEmptyBody());
        
        response = new Response.Builder().request(request)
            .code(302)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .body(ResponseBody.create(null, ""))
            .build();
        
        assertThat(response, hasEmptyBody());
        
        response = new Response.Builder().request(request)
            .code(302)
            .protocol(Protocol.HTTP_1_1)
            .message("Test")
            .body(ResponseBody.create(null, "1"))
            .build();
        
        assertThat(response, not(hasEmptyBody()));
    }
    
}
