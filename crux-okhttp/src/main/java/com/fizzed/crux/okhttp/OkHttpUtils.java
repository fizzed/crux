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

import com.fizzed.crux.util.SecureUtil;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils {
    
    static public final SSLSocketFactory TRUST_ALL_SSL_SOCKET_FACTORY
        = SecureUtil.createTrustAllSSLSocketFactory();
    
    static public final HostnameVerifier TRUST_ALL_HOSTNAME_VERIFIER
        = SecureUtil.createTrustAllHostnameVerifier();
    
    static public HttpLoggingInterceptor createLoggingInterceptor(OkLoggingLevel loggingLevel) {
        if (loggingLevel == null) {
            return null;
        }
        
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        
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
        
        HttpLoggingInterceptor loggingInterceptor = createLoggingInterceptor(options.getLoggingLevel());
        if (loggingInterceptor != null) {
            clientBuilder.addNetworkInterceptor(loggingInterceptor);
        }
    }
    
}
