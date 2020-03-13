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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;

/**
 * Methods that  java.util.Objects should include.
 * 
 * @author jjlauer
 */
public class MoreObjects {
    
    /**
     * Null-safe evaluation of whether a boolean is true.
     * @param value The value to check
     * @return True if value is non-null AND true otherwise false
     */
    static public boolean isTrue(Boolean value) {
        return value != null && value;
    }
 
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
     * Null-safe iterable to simplify use in for-loops, etc.
     * @param <T>
     * @param values 
     * @return An empty iterable if values is null otherwise an iterable of values.
     */
    static public <T> Iterable<T> iterable(T[] values) {
        return new MaybeStream.ArrayIterable<>(values);
    }
    
    /**
     * Null-safe iterable to simplify use in for-loops, etc.
     * @param <T>
     * @param values 
     * @return An empty iterable if values is null otherwise an iterable of values.
     */
    static public <T> Iterable<T> iterable(Iterable<T> values) {
        if (values == null) {
            return new MaybeStream.ArrayIterable<>(null);
        }
        return values;
    }
    
    /**
     * Null-safe evaluation of the length of an array.
     * @param values
     * @return 0 if array is null or empty otherwise the length
     */
    static public int size(Object[] values) {
        return values != null ? values.length : 0;
    }
    
    /**
     * Null-safe evaluation of the size of a collection.
     * @param values
     * @return 0 if collection is null or empty otherwise the length
     */
    static public int size(Collection<?> values) {
        return values != null ? values.size() : 0;
    }
    
    /**
     * Null-safe evaluation of the size of a map.
     * @param map
     * @return 0 if map is null or empty otherwise the length
     */
    static public int size(Map<?,?> map) {
        return map != null ? map.size() : 0;
    }
    
    static public <T> T first(T[] values) {
        return values != null && values.length > 0 ? values[0] : null;
    }
    
    static public <T> T first(Iterable<T> values) {
        if (values == null) {
            return null;
        }
        if (values instanceof List) {
            List<T> l = (List<T>)values;
            return l.isEmpty() ? null : l.get(0);
        }
        if (values instanceof SortedSet) {
            SortedSet<T> s = (SortedSet<T>)values;
            return s.first();
        }
        Iterator<T> it = values.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }
    
    static public <T> T last(T[] values) {
        return values != null && values.length > 0 ? values[values.length-1] : null;
    }
    
    static public <T> T last(Iterable<T> values) {
        if (values == null) {
            return null;
        }
        if (values instanceof List) {
            List<T> l = (List<T>)values;
            return l.isEmpty() ? null : l.get(l.size()-1);
        }
        if (values instanceof SortedSet) {
            SortedSet<T> s = (SortedSet<T>)values;
            return s.last();
        }
        Iterator<T> it = values.iterator();
        T last = null;
        while (it.hasNext()) {
            last = it.next();
        }
        return last;
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
    
    /**
     * Null-safe evaluation of whether an Iterable is empty.
     * @param values The iterable
     * @return True if the iterable is null or has 0 elements.
     */
    static public boolean isEmpty(Iterable<?> values) {
        return values == null || !values.iterator().hasNext();
    }
    
    /**
     * Null-safe evaluation of whether a map is empty.
     * @param map The map
     * @return True if the map is null or has 0 elements.
     */
    static public boolean isEmpty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }
    
    /**
     * Checks if the string is null or has zero chars.
     */
    static public boolean isEmpty(CharSequence chars) {
        return chars == null || chars.length() == 0;
    }
    
    /**
     * Checks if the string is null, empty, or has only characters consisting
     * of spaces.
     */
    static public boolean isBlank(CharSequence chars) {
        if (chars == null || chars.length() == 0) {
            return true;
        }
        for (int i = 0; i < chars.length(); i++) {
            if (chars.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
    
}