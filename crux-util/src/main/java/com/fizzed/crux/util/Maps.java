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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Helper methods for working with maps.
 * 
 * @author jjlauer
 */
public class Maps {
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1) {
        Objects.requireNonNull(k1, "k1 was null");
        return _mapOf(k1, v1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k1, "k2 was null");
        return _mapOf(k1, v1, k2, v2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        return _mapOf(k1, v1, k2, v2, k3, v3, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        return _mapOf(k1, v1, k2, v2, k3, v3, k4, v4, null, null, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        return _mapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        return _mapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        Objects.requireNonNull(k7, "k7 was null");
        return _mapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, null, null, null, null, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        Objects.requireNonNull(k7, "k7 was null");
        Objects.requireNonNull(k8, "k8 was null");
        return _mapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, null, null, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        Objects.requireNonNull(k7, "k7 was null");
        Objects.requireNonNull(k8, "k8 was null");
        Objects.requireNonNull(k9, "k9 was null");
        return _mapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, null, null);
    }
 
    static public <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        Objects.requireNonNull(k7, "k7 was null");
        Objects.requireNonNull(k8, "k8 was null");
        Objects.requireNonNull(k9, "k9 was null");
        Objects.requireNonNull(k10, "k10 was null");
        return _mapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }
 
    static private <K,V> Map<K,V> _mapOf(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5,
            K k6, V v6,
            K k7, V v7,
            K k8, V v8,
            K k9, V v9,
            K k10, V v10) {

        // order is important
        Map<K,V> map = new LinkedHashMap<>();
        if (k1 != null) { map.put(k1, v1); }
        if (k2 != null) { map.put(k2, v2); }
        if (k3 != null) { map.put(k3, v3); }
        if (k4 != null) { map.put(k4, v4); }
        if (k5 != null) { map.put(k5, v5); }
        if (k6 != null) { map.put(k6, v6); }
        if (k7 != null) { map.put(k7, v7); }
        if (k8 != null) { map.put(k8, v8); }
        if (k9 != null) { map.put(k9, v9); }
        if (k10 != null) { map.put(k10, v10); }
        
        return map;
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1) {
        Objects.requireNonNull(k1, "k1 was null");
        return _immutableMapOf(k1, v1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k1, "k2 was null");
        return _immutableMapOf(k1, v1, k2, v2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        return _immutableMapOf(k1, v1, k2, v2, k3, v3, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        return _immutableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, null, null, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        return _immutableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, null, null, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        return _immutableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, null, null, null, null, null, null, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        Objects.requireNonNull(k7, "k7 was null");
        return _immutableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, null, null, null, null, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        Objects.requireNonNull(k7, "k7 was null");
        Objects.requireNonNull(k8, "k8 was null");
        return _immutableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, null, null, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        Objects.requireNonNull(k7, "k7 was null");
        Objects.requireNonNull(k8, "k8 was null");
        Objects.requireNonNull(k9, "k9 was null");
        return _immutableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, null, null);
    }
 
    static public <K,V> ImmutableMap<K,V> immutableMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        Objects.requireNonNull(k1, "k1 was null");
        Objects.requireNonNull(k2, "k2 was null");
        Objects.requireNonNull(k3, "k3 was null");
        Objects.requireNonNull(k4, "k4 was null");
        Objects.requireNonNull(k5, "k5 was null");
        Objects.requireNonNull(k6, "k6 was null");
        Objects.requireNonNull(k7, "k7 was null");
        Objects.requireNonNull(k8, "k8 was null");
        Objects.requireNonNull(k9, "k9 was null");
        Objects.requireNonNull(k10, "k10 was null");
        return _immutableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }
 
    static private <K,V> ImmutableMap<K,V> _immutableMapOf(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5,
            K k6, V v6,
            K k7, V v7,
            K k8, V v8,
            K k9, V v9,
            K k10, V v10) {

        // order is important
        Map<K,V> map = new LinkedHashMap<>();
        
        if (k1 != null) { map.put(k1, v1); }
        if (k2 != null) { map.put(k2, v2); }
        if (k3 != null) { map.put(k3, v3); }
        if (k4 != null) { map.put(k4, v4); }
        if (k5 != null) { map.put(k5, v5); }
        if (k6 != null) { map.put(k6, v6); }
        if (k7 != null) { map.put(k7, v7); }
        if (k8 != null) { map.put(k8, v8); }
        if (k9 != null) { map.put(k9, v9); }
        if (k10 != null) { map.put(k10, v10); }
        
        return new ImmutableMap<>(map);
    }
    
}