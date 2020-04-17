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

import java.io.Closeable;
import java.io.IOException;

public class OkLoggingContext implements Closeable {
    
    static private final ThreadLocal<OkLoggingContext> CONTEXT = new ThreadLocal<OkLoggingContext>() {
        @Override
        protected OkLoggingContext initialValue() {
            return new OkLoggingContext();
        }
    };
    
    static public OkLoggingContext current() {
        return CONTEXT.get();
    }
    
    static public void clear() {
        CONTEXT.remove();
    }
    
    private OkLoggingLevel requestLoggingLevel;
    private Boolean allowRequestBody;
    private OkLoggingLevel responseLoggingLevel;
    private Boolean allowResponseBody;
    private Long maxRequestBodySize;
    private Long maxResponseBodySize;
    
    @Override
    public void close() throws IOException {
        clear();
    }
    
    public Boolean isAllowRequestBody() {
        return allowRequestBody;
    }

    public OkLoggingContext setAllowRequestBody(Boolean allowRequestBody) {
        this.allowRequestBody = allowRequestBody;
        return this;
    }

    public Boolean isAllowResponseBody() {
        return allowResponseBody;
    }

    public OkLoggingContext setAllowResponseBody(Boolean allowResponseBody) {
        this.allowResponseBody = allowResponseBody;
        return this;
    }

    public OkLoggingLevel getRequestLoggingLevel() {
        return requestLoggingLevel;
    }

    public OkLoggingContext setRequestLoggingLevel(OkLoggingLevel requestLoggingLevel) {
        this.requestLoggingLevel = requestLoggingLevel;
        return this;
    }

    public OkLoggingLevel getResponseLoggingLevel() {
        return responseLoggingLevel;
    }

    public OkLoggingContext setResponseLoggingLevel(OkLoggingLevel responseLoggingLevel) {
        this.responseLoggingLevel = responseLoggingLevel;
        return this;
    }

    public Long getMaxRequestBodySize() {
        return maxRequestBodySize;
    }

    public OkLoggingContext setMaxRequestBodySize(Long maxRequestBodySize) {
        this.maxRequestBodySize = maxRequestBodySize;
        return this;
    }

    public Long getMaxResponseBodySize() {
        return maxResponseBodySize;
    }

    public OkLoggingContext setMaxResponseBodySize(Long maxResponseBodySize) {
        this.maxResponseBodySize = maxResponseBodySize;
        return this;
    }
    
}