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

import com.fizzed.crux.util.MessageLevel;
import com.fizzed.crux.util.Slf4jUtil;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import org.slf4j.Logger;

/**
 * Mostly based on OkHttp's LoggingInterceptor, but the code was extracted into
 * utility methods so they can be utilized outside of simply an Interceptor.
 * 
 * @author jjlauer
 */
public class OkHttpLogger {

    
    
    private Set<String> headersToRedact;

    public void addRedactHeader(String name) {
        if (this.headersToRedact == null) {
            this.headersToRedact = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        }
        this.headersToRedact.add(name);
    }
    
    public void logRequest(
            MessageLevel messageLevel,
            Logger logger,
            Request request,
            Connection connection,
            boolean logHeaders,
            boolean logBody,
            long maxBodySize) throws IOException {
        
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        String requestStartMessage = "--> "
            + request.method()
            + ' ' + request.url()
            + (connection != null ? " " + connection.protocol() : "");
        
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        
        Slf4jUtil.log(messageLevel, logger, requestStartMessage);

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    Slf4jUtil.log(messageLevel, logger, "Content-Type: {}", requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    Slf4jUtil.log(messageLevel, logger, "Content-Length: {}", requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logHeader(messageLevel, logger, headers, i);
                }
            }

            if (!logBody || !hasRequestBody) {
                Slf4jUtil.log(messageLevel, logger, "--> END {}", request.method());
            } else if (bodyHasUnknownEncoding(request.headers())) {
                Slf4jUtil.log(messageLevel, logger, "--> END {} (encoded body omitted)", request.method());
//            } else if (requestBody.isDuplex()) {
//                Slf4jUtil.log(messageLevel, logger, "--> END " + request.method() + " (duplex request body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(StandardCharsets.UTF_8);
                }

                Slf4jUtil.log(messageLevel, logger, "");
                
                long contentLength = requestBody.contentLength();
                
                if (isPlaintext(contentType, buffer) && contentLength > 0) {
                    long stringLength = contentLength;
                    boolean truncated = false;
                    if (stringLength > maxBodySize) {
                        stringLength = maxBodySize;
                        truncated = true;
                    }
                    Slf4jUtil.log(messageLevel, logger, buffer.snapshot((int)stringLength).string(charset));
                    if (truncated) {
                        Slf4jUtil.log(messageLevel, logger, "--- TRUNCATED BODY ({} of {} bytes)", maxBodySize, contentLength);
                    }
                    Slf4jUtil.log(messageLevel, logger, "--> END {} ({}-byte body)",
                        request.method(), contentLength);
                } else {
                    Slf4jUtil.log(messageLevel, logger, "--> END {} (binary {}-byte body omitted)",
                        request.method(), contentLength);
                }
            }
        }
    }
    
    public void logResponse(
            MessageLevel messageLevel,
            Logger logger,
            Response response,
            long tookMs,
            boolean logHeaders,
            boolean logBody,
            long maxBodySize) throws IOException {
        
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        Slf4jUtil.log(messageLevel, logger, "<-- "
            + response.code()
            + (response.message().isEmpty() ? "" : ' ' + response.message())
            + ' ' + response.request().url()
            + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logHeader(messageLevel, logger, headers, i);
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                Slf4jUtil.log(messageLevel, logger, "<-- END HTTP");
            } else if (bodyHasUnknownEncoding(response.headers())) {
                Slf4jUtil.log(messageLevel, logger, "<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);     // Buffer the entire body.
                Buffer buffer = source.buffer();

                Long gzippedLength = null;
                if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                    gzippedLength = buffer.size();
                    try (GzipSource gzippedResponseBody = new GzipSource(buffer.clone())) {
                        buffer = new Buffer();
                        buffer.writeAll(gzippedResponseBody);
                    }
                }

                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(StandardCharsets.UTF_8);
                }
                
                long bufferSize = buffer.size();

                if (!isPlaintext(contentType, buffer)) {
                    Slf4jUtil.log(messageLevel, logger, "");
                    Slf4jUtil.log(messageLevel, logger, "<-- END HTTP (binary {}-byte body omitted)", bufferSize);
                    return;
                }

                if (contentLength != 0) {
                    long stringLength = bufferSize;
                    boolean truncated = false;
                    if (stringLength > maxBodySize) {
                        stringLength = maxBodySize;
                        truncated = true;
                    }
                    
                    Slf4jUtil.log(messageLevel, logger, "");
                    Slf4jUtil.log(messageLevel, logger, buffer.snapshot((int)stringLength).string(charset));
                    if (truncated) {
                        Slf4jUtil.log(messageLevel, logger, "--- TRUNCATED BODY ({} of {} bytes)", maxBodySize, bufferSize);
                    }
                }

                if (gzippedLength != null) {
                    Slf4jUtil.log(messageLevel, logger, "<-- END HTTP ({}-byte, {}-gzipped-byte body)", buffer.size(), gzippedLength);
                } else {
                    Slf4jUtil.log(messageLevel, logger, "<-- END HTTP ({}-byte body)", bufferSize);
                }
            }
        }
    }
    
    private void logHeader(
            MessageLevel messageLevel,
            Logger logger,
            Headers headers, int i) {
        
        String value = headersToRedact != null && headersToRedact.contains(headers.name(i))
            ? "<redacted>" : headers.value(i);
        
        Slf4jUtil.log(messageLevel, logger, headers.name(i) + ": " + value);
    }

    /**
     * Returns true if the body in question probably contains human readable
     * text. Uses a small sample of code points to detect unicode control
     * characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(MediaType contentType, Buffer buffer) {
        // for some types of content-type, skip checking
        if (contentType != null && contentType.type() != null) {
            String type = contentType.type().toLowerCase();
            
            if (type.equalsIgnoreCase("image")) {
                return false;
            }
            
            String fullType = type + "/" + contentType.subtype().toLowerCase();
            
            switch (fullType) {
                case "application/pdf":
                    return false;
            }
        }
        
        // otherwise, try to guess it
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if ((Character.isISOControl(codePoint))
                        && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
            && !contentEncoding.equalsIgnoreCase("identity")
            && !contentEncoding.equalsIgnoreCase("gzip");
    }
    
}