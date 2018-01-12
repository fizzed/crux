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

import java.nio.charset.Charset;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.Response;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;

public class OkHttpMatchers {
    
    static public Matcher<Response> hasStatusCode(final int statusCode) {
        return new TypeSafeDiagnosingMatcher<Response>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("response code() should return ").appendValue(statusCode);
            }

            @Override
            protected boolean matchesSafely(final Response response, final Description mismatchDescription) {
                mismatchDescription.appendText(" was ").appendValue(response.code());
                return OkHttpUtils.hasStatusCode(response, statusCode);
            }
        };
    }
    
    static public Matcher<Response> hasAnyStatusCode(final int ... statusCodes) {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                return OkHttpUtils.hasStatusCode(response, statusCodes);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("response code() should return  any of ").appendValue(statusCodes);
            }

            @Override
            protected void describeMismatchSafely(Response response, Description mismatchDescription) {
                mismatchDescription.appendText(" was ").appendValue(response.code());
            }
        };
    }
    
    static public Matcher<Response> hasContentType(final String contentType) {
        return new TypeSafeDiagnosingMatcher<Response>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("response Content-Type header should be ").appendValue(contentType);
            }

            @Override
            protected boolean matchesSafely(final Response response, final Description mismatchDescription) {
                mismatchDescription.appendText(" was ").appendValue(contentType);
                
                return OkHttpUtils.hasContentType(response, contentType);
            }
        };
    }
    
    static public Matcher<Response> hasCharset(final String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return hasCharset(charset);
    }
    
    static public Matcher<Response> hasCharset(final Charset charset) {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                String responseContentType = response.header("Content-Type");
                MediaType responseMediaType = MediaType.parse(responseContentType);
                return charset.equals(responseMediaType.charset());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("response content-type charset should be ").appendValue(charset);
            }

            @Override
            protected void describeMismatchSafely(Response response, Description mismatchDescription) {
                String responseContentType = response.header("Content-Type");
                MediaType responseMediaType = MediaType.parse(responseContentType);
                mismatchDescription.appendText(" was ").appendValue(responseMediaType.charset());
            }
        };
    }
    
    static public Matcher<Response> hasHeader(final String name) {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                return response.header(name) != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("response should have a header ").appendValue(name);
            }

            @Override
            protected void describeMismatchSafely(Response response, Description mismatchDescription) {
                String actualValue = response.header(name);
                mismatchDescription.appendText(" was missing");
            }
        };
    }
    
    static public Matcher<Response> hasHeader(final String name, final String value) {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                String actualValue = response.header(name);
                return Objects.equals(value, actualValue);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("response should have header ").appendValue(name + ": " + value);
            }

            @Override
            protected void describeMismatchSafely(Response response, Description mismatchDescription) {
                String actualValue = response.header(name);
                mismatchDescription.appendText(" was header ").appendValue(name + ": " + actualValue);
            }
        };
    }
    
    static public Matcher<Response> hasHeaderIgnoringCase(final String name, final String value) {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                String actualValue = response.header(name);
                return value.equalsIgnoreCase(actualValue);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("response should have header ").appendValue(name + ": " + value);
            }

            @Override
            protected void describeMismatchSafely(Response response, Description mismatchDescription) {
                String actualValue = response.header(name);
                mismatchDescription.appendText(" was header ").appendValue(name + ": " + actualValue);
            }
        };
    }
    
    static public Matcher<Response> isRedirect() {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                return response.isRedirect() && response.header("Location") != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("response should be a redirect with a location header");
            }

            @Override
            protected void describeMismatchSafely(Response response, Description mismatchDescription) {
                String locationHeader = response.header("Location");
                mismatchDescription.appendText(" was status code ")
                    .appendValue(response.code())
                    .appendText(" with location header ")
                    .appendValue(locationHeader);
            }
        };
    }
    
    static public Matcher<Response> hasEmptyBody() {
        return new TypeSafeMatcher<Response>() {
            @Override
            protected boolean matchesSafely(Response response) {
                return response.body() == null || response.body().contentLength() == 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("response should have an empty body");
            }

            @Override
            protected void describeMismatchSafely(Response response, Description mismatchDescription) {
                mismatchDescription.appendText(" was a body with length ")
                    .appendValue(response.body().contentLength());
            }
        };
    }
    
}