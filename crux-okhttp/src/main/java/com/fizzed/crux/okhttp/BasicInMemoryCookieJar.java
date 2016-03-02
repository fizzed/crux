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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Basic in-memory cookie jar for OkHttp3. Does not try to do any handling of
 * cookies across domains, etc.  Does not enforce domains, paths, secure flags, etc.
 */
public class BasicInMemoryCookieJar implements CookieJar {

    private final AtomicReference<List<Cookie>> cookiesRef;

    public BasicInMemoryCookieJar() {
        this.cookiesRef = new AtomicReference<>();
    }
    
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookiesRef.set(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (this.cookiesRef.get() != null) {
            return this.cookiesRef.get();
        }
        return new ArrayList<>();
    }
    
}
