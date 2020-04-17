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
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkHttpLoggingInterceptor implements Interceptor {
    
    private Logger logger;
    private MessageLevel messageLevel;
    private OkLoggingLevel requestLoggingLevel;
    private long maxRequestBodySize;
    private OkLoggingLevel responseLoggingLevel;
    private long maxResponseBodySize;
    private final OkHttpLogger loggerHelper;
    private boolean verboseOnFailure;
    
    public OkHttpLoggingInterceptor() {
        this.logger = LoggerFactory.getLogger("okhttp");
        this.messageLevel = MessageLevel.DEBUG;
        this.requestLoggingLevel = OkLoggingLevel.NONE;
        this.responseLoggingLevel = OkLoggingLevel.NONE;
        this.loggerHelper = new OkHttpLogger();
        this.verboseOnFailure = true;
        this.maxRequestBodySize = 524288L;     // 0.5MB by default...
        this.maxResponseBodySize = 524288L;     // 0.5MB by default...
    }

    public void addRedactHeader(String headerName) {
        this.loggerHelper.addRedactHeader(headerName);
    }
    
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public MessageLevel getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(MessageLevel messageLevel) {
        this.messageLevel = messageLevel;
    }

    public OkLoggingLevel getRequestLoggingLevel() {
        return requestLoggingLevel;
    }

    public void setRequestLoggingLevel(OkLoggingLevel requestLoggingLevel) {
        Objects.requireNonNull(requestLoggingLevel, "requestLoggingLevel was null");
        this.requestLoggingLevel = requestLoggingLevel;
    }

    public OkLoggingLevel getResponseLoggingLevel() {
        return responseLoggingLevel;
    }

    public void setResponseLoggingLevel(OkLoggingLevel responseLoggingLevel) {
        Objects.requireNonNull(responseLoggingLevel, "responseLoggingLevel was null");
        this.responseLoggingLevel = responseLoggingLevel;
    }
    
    public boolean isVerboseOnFailure() {
        return verboseOnFailure;
    }

    public void setVerboseOnFailure(boolean verboseOnFailure) {
        this.verboseOnFailure = verboseOnFailure;
    }

    public long getMaxRequestBodySize() {
        return maxRequestBodySize;
    }

    public void setMaxRequestBodySize(long maxRequestBodySize) {
        this.maxRequestBodySize = maxRequestBodySize;
    }

    public long getMaxResponseBodySize() {
        return maxResponseBodySize;
    }

    public void setMaxResponseBodySize(long maxResponseBodySize) {
        this.maxResponseBodySize = maxResponseBodySize;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        //
        // global vs. context logging
        //
        OkLoggingLevel reqLogLevel = this.requestLoggingLevel;
        OkLoggingLevel resLogLevel = this.responseLoggingLevel;
        boolean reqBodyAllowed = true;
        boolean resBodyAllowed = true;
        long reqMaxBodySize = this.maxRequestBodySize;
        long resMaxBodySize = this.maxResponseBodySize;
        
        final OkLoggingContext context = OkLoggingContext.current();
        if (context != null) {
            if (context.getRequestLoggingLevel() != null) {
                reqLogLevel = context.getRequestLoggingLevel();
            }
            if (context.getResponseLoggingLevel() != null) {
                resLogLevel = context.getResponseLoggingLevel();
            }
            if (context.isAllowRequestBody() != null) {
                reqBodyAllowed = context.isAllowRequestBody();
            }
            if (context.isAllowResponseBody() != null) {
                resBodyAllowed = context.isAllowResponseBody();
            }
            if (context.getMaxRequestBodySize() != null) {
                reqMaxBodySize = context.getMaxRequestBodySize();
            }
            if (context.getMaxResponseBodySize() != null) {
                resMaxBodySize = context.getMaxResponseBodySize();
            }
        }
        
        final Request request = chain.request();
        boolean loggedRequest = false;

        if (reqLogLevel != OkLoggingLevel.NONE) {
            this.loggerHelper.logRequest(
                this.messageLevel,
                this.logger,
                request,
                chain.connection(),
                (reqLogLevel == OkLoggingLevel.HEADERS || reqLogLevel == OkLoggingLevel.BODY),
                (reqLogLevel == OkLoggingLevel.BODY && reqBodyAllowed),
                reqMaxBodySize
            );
            loggedRequest = true;
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            long failureMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            
            if (resLogLevel != OkLoggingLevel.NONE || this.verboseOnFailure) {
                // if we haven't logged the request yet, do it now...
                if (!loggedRequest) {
                    this.loggerHelper.logRequest(
                        this.messageLevel,
                        this.logger,
                        request,
                        chain.connection(),
                        true, true, reqMaxBodySize);
                }
                Slf4jUtil.log(this.messageLevel, this.logger, "<-- HTTP FAILED ({} ms): " + e, failureMillis);
            }
            
            throw e;
        }
        
        
        long responseMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        boolean resVerboseFailure = this.verboseOnFailure && !response.isSuccessful();
        
        if (resLogLevel != OkLoggingLevel.NONE || resVerboseFailure) {
            // if verbose on failure was requested and the request not logged yet...
            if (resVerboseFailure && !loggedRequest) {
                this.loggerHelper.logRequest(
                    this.messageLevel,
                    this.logger,
                    request,
                    chain.connection(),
                    true, true, reqMaxBodySize);
            }
            
            this.loggerHelper.logResponse(
                this.messageLevel,
                this.logger,
                response,
                responseMillis,
                (resLogLevel == OkLoggingLevel.HEADERS || resLogLevel == OkLoggingLevel.BODY || resVerboseFailure),
                ((resLogLevel == OkLoggingLevel.BODY && resBodyAllowed) || resVerboseFailure),
                resMaxBodySize
            );
        }

        return response;
    }

}