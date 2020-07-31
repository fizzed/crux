/*
 * Copyright 2020 Fizzed, Inc.
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
import java.nio.charset.StandardCharsets;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkHttpLoggerTest {
    static private final Logger log = LoggerFactory.getLogger(OkHttpLoggerTest.class);
 
    @Test
    public void isPlainTextWithMediaType() {
        boolean v;
        
        v = OkHttpLogger.isPlaintext(MediaType.parse("application/pdf"), null);
        assertThat(v, is(false));
        
        v = OkHttpLogger.isPlaintext(MediaType.parse("image/png"), null);
        assertThat(v, is(false));
        
        v = OkHttpLogger.isPlaintext(MediaType.parse("image/jpeg"), null);
        assertThat(v, is(false));
    }
 
    @Test
    public void isPlainTextWithProbe() {
        boolean v;
        Buffer buffer;
        
        buffer = new Buffer();
        buffer.writeString("hi dude", StandardCharsets.UTF_8);
        
        v = OkHttpLogger.isPlaintext(null, buffer);
        assertThat(v, is(true));
        
        buffer = new Buffer();
        buffer.writeString("<?xml", StandardCharsets.UTF_8);
        
        v = OkHttpLogger.isPlaintext(null, buffer);
        assertThat(v, is(true));
        
        buffer = new Buffer();
        buffer.writeString("{}", StandardCharsets.UTF_8);
        
        v = OkHttpLogger.isPlaintext(null, buffer);
        assertThat(v, is(true));
        
        buffer = new Buffer();
        buffer.writeByte((byte)1);
        
        v = OkHttpLogger.isPlaintext(null, buffer);
        assertThat(v, is(false));
    }
 
    @Test
    public void responseMaxBodySize() throws IOException {
        
        OkHttpLogger ohl = new OkHttpLogger();
        Response response;
        
        Request request = new Request.Builder()
            .url("http://localhost")
            .build();
        
        response = new Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(ResponseBody.create(MediaType.parse("text/html; charset=utf-8"), "\u20AC"))
            .build();
        
        ohl.logResponse(null, log, response, 0, true, true, 1);
    }
 
    @Test
    public void noContent() throws IOException {
        
        OkHttpLogger ohl = new OkHttpLogger();
        Response response;
        
        Request request = new Request.Builder()
            .url("http://localhost")
            .build();
        
        response = new Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(204)
            .message("No Content")
            .addHeader("Content-Length", "0")
            .body(ResponseBody.create(MediaType.parse("application/json"), new byte[0]))
            .build();
        
        ohl.logResponse(null, log, response, 0, true, true, 1);
    }
 
    @Test
    public void noContentGzippedEOFException() throws IOException {
        
        OkHttpLogger ohl = new OkHttpLogger();
        Response response;
        
        Request request = new Request.Builder()
            .url("http://localhost")
            .build();
        
        response = new Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(204)
            .message("No Content")
            .addHeader("Content-Encoding", "gzip")
            .addHeader("Content-Length", "0")
            .body(ResponseBody.create(MediaType.parse("application/json"), new byte[0]))
            .build();
        
        ohl.logResponse(null, log, response, 0, true, true, 1);
    }
    
}