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
import java.util.Properties;

public interface BindingPropertySupport<A extends BindingPropertySupport<A>> {

    BindingPropertyMap<A> getPropertyMap();
    
    default boolean hasProperty(String key) {
        return this.getPropertyMap().hasKey(key);
    }
    
    default A setProperty(String key, Object value) {
        this.getPropertyMap().set((A)this, key, value);
        return (A)this;
    }
    
    default A setProperty(String key, Object value, boolean skipUnknownKeys) {
        this.getPropertyMap().set((A)this, key, value, skipUnknownKeys);
        return (A)this;
    }
    
    default A setProperties(Properties properties) {
        this.getPropertyMap().setAll((A)this, properties);
        return (A)this;
    }
    
    default A setProperties(Properties properties, boolean skipUnknownKeys) {
        this.getPropertyMap().setAll((A)this, properties, skipUnknownKeys);
        return (A)this;
    }
    
    default A setProperties(Map<String,?> properties) {
        this.getPropertyMap().setAll((A)this, properties);
        return (A)this;
    }
    
    default A setProperties(Map<String,?> properties, boolean skipUnknownKeys) {
        this.getPropertyMap().setAll((A)this, properties, skipUnknownKeys);
        return (A)this;
    }
    
}
