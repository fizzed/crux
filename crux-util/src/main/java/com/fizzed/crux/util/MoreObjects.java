/*
 * Copyright 2018 Fizzed, Inc.
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

import java.util.Collection;
import java.util.Objects;

/**
 * Methods that  java.util.Objects should include.
 * 
 * @author jjlauer
 */
public class MoreObjects {
 
    /**
     * Checks whether the value is in the list of values.
     * @param <T> The type
     * @param value The value to check
     * @param values The list of values to check against
     * @return True if the value is contained otherwise false
     */
    static public <T> boolean in(T value, T... values) {
        if (values == null) {
            throw new IllegalArgumentException("values was null");
        }
        for (T v : values) {
            if (Objects.equals(v, value)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks whether the value is in the list of values.
     * @param <T> The type
     * @param value The value to check
     * @param values The list of values to check against
     * @return True if the value is contained otherwise false
     */
    static public <T> boolean in(T value, Iterable<T> values) {
        for (T v : values) {
            if (Objects.equals(v, value)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Null-safe evaluation of whether an array is empty.
     * @param <T>
     * @param values The array
     * @return True if array is null or has 0 elements
     */
    static public <T> boolean isEmpty(T[] values) {
        return values == null || values.length == 0;
    }
    
    /**
     * Null-safe evaluation of whether a collection is empty.
     * @param values The collection
     * @return True if the collection is null or has 0 elements.
     */
    static public boolean isEmpty(Collection<?> values) {
        return values == null || values.isEmpty();
    }
    
}