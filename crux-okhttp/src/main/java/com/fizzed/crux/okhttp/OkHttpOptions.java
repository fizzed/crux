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

/**
 * Options for building an OkHttp client.
 */
public class OkHttpOptions implements BindingPropertySupport {
    
    static private final BindingPropertyMap<OkHttpOptions> PROP_MAP
        = new BindingPropertyMap<OkHttpOptions>()
            .bindBoolean("insecure", OkHttpOptions::setInsecure)
            .bindLong("connect_timeout", OkHttpOptions::setConnectTimeout)
            .bindLong("write_timeout", OkHttpOptions::setWriteTimeout)
            .bindLong("read_timeout", OkHttpOptions::setReadTimeout)
            .bindBoolean("follow_redirects", OkHttpOptions::setFollowRedirects)
            .bindType("logging_level", OkHttpOptions::setLoggingLevel, OkLoggingLevel.class, (s) -> {
                OkLoggingLevel level = OkLoggingLevel.valueOf(s.toUpperCase());
                if (level == null) {
                    throw new IllegalArgumentException("Invalid logging level " + s);
                }
                return level;
            });
    
    private Boolean insecure;
    private Long connectTimeout;
    private Long writeTimeout;
    private Long readTimeout;
    private Boolean followRedirects;
    private OkLoggingLevel loggingLevel;
    
    @Override
    public BindingPropertyMap getPropertyMap() {
        return PROP_MAP;
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

    public OkLoggingLevel getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(OkLoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public Boolean getFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(Boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public void setUri(Uri uri) {
        
    }

}
