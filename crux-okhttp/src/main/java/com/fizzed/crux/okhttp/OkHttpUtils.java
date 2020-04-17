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

import com.fizzed.crux.util.MessageLevel;
import static com.fizzed.crux.util.Maybe.maybe;
import com.fizzed.crux.util.SecureUtil;
import java.io.IOException;
import static java.util.Optional.ofNullable;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkHttpUtils {
    
    static public final SSLSocketFactory TRUST_ALL_SSL_SOCKET_FACTORY
        = SecureUtil.createTrustAllSSLSocketFactory();
    
    static public final HostnameVerifier TRUST_ALL_HOSTNAME_VERIFIER
        = SecureUtil.createTrustAllHostnameVerifier();
    
    static public HttpLoggingInterceptor createLoggingInterceptor(OkLoggingLevel loggingLevel) {
        return createLoggingInterceptor(loggingLevel, null, null);
    }
    
    static public HttpLoggingInterceptor createLoggingInterceptor(OkLoggingLevel loggingLevel, String loggerName) {
        return createLoggingInterceptor(loggingLevel, loggerName, null);
    }
    
    static public HttpLoggingInterceptor createLoggingInterceptor(OkLoggingLevel loggingLevel, String loggerName, MessageLevel messageLevel) {
        final OkLoggingLevel _loggingLevel = maybe(loggingLevel).orElse(OkLoggingLevel.NONE);
        final MessageLevel _messageLevel = maybe(messageLevel).orElse(MessageLevel.DEBUG);
        
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            private final Logger log = LoggerFactory.getLogger(loggerName != null ? loggerName : "okhttp");
            @Override
            public void log(String message) {
                switch (_messageLevel) {
                    case ERROR:
                        log.error("{}", message);
                        break;
                    case WARN:
                        log.warn("{}", message);
                        break;
                    case INFO:
                        log.info("{}", message);
                        break;
                    case DEBUG:
                        log.debug("{}", message);
                        break;
                    case TRACE:
                        log.trace("{}", message);
                        break;
                }
            }
        });
        
        switch (_loggingLevel) {
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
                // NOTE: okhttp doesn't do a great job at body logging
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                break;
        }
        
        return loggingInterceptor;
    }
    
    static public OkHttpClient buildClient(OkHttpOptions options) {
        return createBuilder(options).build();
    }
    
    static public OkHttpClient.Builder createBuilder(OkHttpOptions options) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        
        configureBuilder(clientBuilder, options);
        
        return clientBuilder;
    }
    
    static public void configureBuilder(OkHttpClient.Builder clientBuilder, OkHttpOptions options) {
        if (options.getInsecure() != null && options.getInsecure()) {
            clientBuilder.sslSocketFactory(OkHttpUtils.TRUST_ALL_SSL_SOCKET_FACTORY);
            clientBuilder.hostnameVerifier(OkHttpUtils.TRUST_ALL_HOSTNAME_VERIFIER);
        }
        
        if (options.getConnectTimeout() != null) {
            clientBuilder.connectTimeout(options.getConnectTimeout(), TimeUnit.MILLISECONDS);
        }
        
        if (options.getWriteTimeout() != null) {
            clientBuilder.writeTimeout(options.getWriteTimeout(), TimeUnit.MILLISECONDS);
        }
        
        if (options.getReadTimeout() != null) {
            clientBuilder.readTimeout(options.getReadTimeout(), TimeUnit.MILLISECONDS);
        }
        
        if (options.getFollowRedirects() != null) {
            clientBuilder.followRedirects(options.getFollowRedirects());
            clientBuilder.followSslRedirects(options.getFollowRedirects());
        }
        

        OkHttpLoggingInterceptor loggingInterceptor = new OkHttpLoggingInterceptor();
        
        if (options.getLoggingLevel() != null) {
            loggingInterceptor.setRequestLoggingLevel(options.getLoggingLevel());
            loggingInterceptor.setResponseLoggingLevel(options.getLoggingLevel());
        }
        if (options.getRequestLoggingLevel() != null) {
            loggingInterceptor.setRequestLoggingLevel(options.getRequestLoggingLevel());
        }
        if (options.getResponseLoggingLevel() != null) {
            loggingInterceptor.setResponseLoggingLevel(options.getResponseLoggingLevel());
        }
        if (options.getLoggerName() != null) {
            loggingInterceptor.setLogger(LoggerFactory.getLogger(options.getLoggerName()));
        }
        if (options.getVerboseOnFailure() != null) {
            loggingInterceptor.setVerboseOnFailure(options.getVerboseOnFailure());
        }
        if (options.getLoggingRedactHeaders() != null) {
            String[] headers = options.getLoggingRedactHeaders().split("\\,");
            if (headers != null) {
                for (String h : headers) {
                    loggingInterceptor.addRedactHeader(h);
                }
            }
            loggingInterceptor.setMessageLevel(options.getMessageLevel());
        }
        if (options.getMessageLevel() != null) {
            loggingInterceptor.setMessageLevel(options.getMessageLevel());
        }
        
        clientBuilder.addNetworkInterceptor(loggingInterceptor);
    }
    
    static public boolean hasStatusCode(Response response, int... statusCodes) {
        final int actualStatusCode = response.code();
        for (int statusCode : statusCodes) {
            if (statusCode == actualStatusCode) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Does the response have a status code between min (inclusive) and max (exclusive)?
     * @param response The response
     * @param minStatusCode Min status code (inclusive)
     * @param maxStatusCode Max status code (exclusive)
     * @return True if in between range or otherwise false
     */
    static public boolean hasStatusCodeRange(Response response, int minStatusCode, int maxStatusCode) {
        final int actualStatusCode = response.code();
        return actualStatusCode >= minStatusCode && actualStatusCode < maxStatusCode;
    }
    
    static public boolean hasContentType(Request request, String expectedContentType) {
        return hasContentType(request.header("Content-Type"), expectedContentType);
    }
    
    static public boolean hasContentType(Response response, String expectedContentType) {
        return hasContentType(response.header("Content-Type"), expectedContentType);
    }
    
    static public boolean hasContentType(String actualContentType, String expectedContentType) {
        MediaType actualMediaType = ofNullable(actualContentType)
            .map(v -> MediaType.parse(v))
            .orElse(null);

        MediaType expectedMediaType = MediaType.parse(expectedContentType);

        if (actualMediaType == null || actualMediaType.type() == null) {
            return false;
        }
        
        if (!actualMediaType.type().equalsIgnoreCase(expectedMediaType.type())) {
            return false;
        }

        return !(expectedMediaType.charset() != null &&
                !expectedMediaType.charset().equals(actualMediaType.charset()));
    }
    
    static public void verifyContentType(Response response, String expectedContentType) throws IOException {
        if (!hasContentType(response, expectedContentType)) {
            throw new IOException("Unexpected response content type (" +
                "expected " + expectedContentType + " but was "
                + response.header("Content-Type") + ")");
        }
    }
 
}