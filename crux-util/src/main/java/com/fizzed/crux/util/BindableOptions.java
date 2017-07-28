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
package com.fizzed.crux.util;

import java.util.Map;
import java.util.Set;

public class BindableOptions<A extends BindableOptions<A>> {
    
    private final BindableHandlers handlers;

    public BindableOptions(BindableHandlers handlers) {
        this.handlers = handlers;
    }
    
    public Set<String> getParameterKeys() {
        return this.handlers.getKeys();
    }
    
    public A setParameter(String key, String value) {
        this.handlers.process(this, key, value);
        return (A)this;
    }
    
    public A setParameters(Map<String,String> values) {
        this.handlers.process(this, values);
        return (A)this;
    }
    
}
