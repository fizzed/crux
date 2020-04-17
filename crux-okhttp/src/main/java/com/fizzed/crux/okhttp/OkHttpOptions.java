/*
 * Copyright 2017 Fizzed, Inc.
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

import com.fizzed.crux.uri.Uri;
import com.fizzed.crux.util.BindingPropertyMap;
import com.fizzed.crux.util.BindingPropertySupport;
import com.fizzed.crux.util.MessageLevel;
import java.util.function.Function;

/**
 * Options for building an OkHttp client.
 * @param <A>
 */
public class OkHttpOptions<A extends OkHttpOptions<A>> implements BindingPropertySupport<A> {
    
    static private final Function<String,OkLoggingLevel> LOGGING_LEVEL_CONVERTER = (s) -> {
        OkLoggingLevel level = OkLoggingLevel.valueOf(s.toUpperCase());
        if (level == null) {
            throw new IllegalArgumentException("Invalid logging level " + s);
        }
        return level;
    };
    
    protected final BindingPropertyMap<A> bindingPropertyMap = new BindingPropertyMap<A>()
        .bindString("base_uri", A::setBaseUri)
        .bindBoolean("insecure", A::setInsecure)
        .bindLong("connect_timeout", A::setConnectTimeout)
        .bindLong("write_timeout", A::setWriteTimeout)
        .bindLong("read_timeout", A::setReadTimeout)
        .bindBoolean("follow_redirects", A::setFollowRedirects)
        .bindType("logging_level", A::setLoggingLevel, OkLoggingLevel.class, LOGGING_LEVEL_CONVERTER)
        .bindType("request_logging_level", A::setRequestLoggingLevel, OkLoggingLevel.class, LOGGING_LEVEL_CONVERTER)
        .bindType("response_logging_level", A::setResponseLoggingLevel, OkLoggingLevel.class, LOGGING_LEVEL_CONVERTER)
        .bindString("logger_name", OkHttpOptions::setLoggerName)
        .bindBoolean("verbose_on_failure", A::setVerboseOnFailure)
        .bindString("logging_redact_headers", A::setLoggingRedactHeaders)
        .bindType("message_level", A::setMessageLevel, MessageLevel.class, (s) -> {
            MessageLevel level = MessageLevel.valueOf(s.toUpperCase());
            if (level == null) {
                throw new IllegalArgumentException("Invalid message level " + s);
            }
            return level;
        });
    
    private String baseUri;
    private Boolean insecure;
    private Long connectTimeout;
    private Long writeTimeout;
    private Long readTimeout;
    private Boolean followRedirects;
    private OkLoggingLevel loggingLevel;
    private OkLoggingLevel requestLoggingLevel;
    private OkLoggingLevel responseLoggingLevel;
    private Boolean verboseOnFailure;
    private String loggerName;
    private String loggingRedactHeaders;
    private MessageLevel messageLevel;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public OkHttpOptions() {
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public OkHttpOptions(String uri) {
        this.setUri(new Uri(uri));
    }
    
    @Override
    public BindingPropertyMap<A> getPropertyMap() {
        return this.bindingPropertyMap;
    }

    public void setUri(Uri uri) {
        this.setUri(uri, true);
    }
    
    public void setUri(Uri uri, boolean skipUnknownKeys) {
        // set properties via query parameters in a uri
        this.setProperties(uri.getQueryFirstMap(), skipUnknownKeys);
        // strip out query parameters to set base uri
        this.baseUri = uri.mutable()
            .setQuery(null)
            .fragment(null)
            .toString();
    }
    
    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
    
    public void setBaseUri(Uri baseUri) {
        this.baseUri = baseUri.toString();
    }
    
    public Boolean getInsecure() {
        return insecure;
    }

    public void setInsecure(Boolean insecure) {
        this.insecure = insecure;
    }

    public Long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Boolean getFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(Boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public OkLoggingLevel getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(OkLoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public OkLoggingLevel getRequestLoggingLevel() {
        return requestLoggingLevel;
    }

    public void setRequestLoggingLevel(OkLoggingLevel requestLoggingLevel) {
        this.requestLoggingLevel = requestLoggingLevel;
    }

    public OkLoggingLevel getResponseLoggingLevel() {
        return responseLoggingLevel;
    }

    public void setResponseLoggingLevel(OkLoggingLevel responseLoggingLevel) {
        this.responseLoggingLevel = responseLoggingLevel;
    }
    
    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public Boolean getVerboseOnFailure() {
        return verboseOnFailure;
    }

    public void setVerboseOnFailure(Boolean verboseOnFailure) {
        this.verboseOnFailure = verboseOnFailure;
    }

    public String getLoggingRedactHeaders() {
        return loggingRedactHeaders;
    }

    public void setLoggingRedactHeaders(String loggingRedactHeaders) {
        this.loggingRedactHeaders = loggingRedactHeaders;
    }

    public MessageLevel getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(MessageLevel messageLevel) {
        this.messageLevel = messageLevel;
    }
    
}