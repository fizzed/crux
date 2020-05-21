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
package com.fizzed.crux.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ImmutableMap<K,V> implements Map<K,V> {

    private final Map<K,V> backingMap;

    ImmutableMap(Map<K, V> backingMap) {
        Objects.requireNonNull(backingMap, "backingMap was null");
        this.backingMap = backingMap;
    }
    
    @Override
    public int size() {
        return this.backingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.backingMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.backingMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.backingMap.get(key);
    }

    @Override @Deprecated
    public V put(K key, V value) {
        throw new IllegalStateException("Not allowed to modify ImmutableMap");
    }

    @Override @Deprecated
    public V remove(Object key) {
        throw new IllegalStateException("Not allowed to modify ImmutableMap");
    }

    @Override @Deprecated
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new IllegalStateException("Not allowed to modify ImmutableMap");
    }

    @Override @Deprecated
    public void clear() {
        throw new IllegalStateException("Not allowed to modify ImmutableMap");
    }

    @Override
    public Set<K> keySet() {
        return this.backingMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.backingMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.backingMap.entrySet();
    }
    
}