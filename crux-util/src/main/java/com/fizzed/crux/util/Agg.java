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

import java.util.Comparator;
import java.util.Objects;

/**
 * Aggregates (e.g. min/max), with null-safety!
 * 
 * @author jjlauer
 */
public class Agg {
 
    static public class OrderedValue<T> {
        
        protected final Comparator<? super T> comparator;
        protected T value;

        protected OrderedValue(Comparator<? super T> comparator, T initialValue) {
            this.comparator = comparator;
            this.value = initialValue;
        }

        /**
         * Gets the current value.
         * 
         * @return 
         */
        public T get() {
            return value;
        }

        /**
         * Null-safe apply a new value, calculate the agg (e.g. min or max), and
         * returns the new value.  Nulls are skipped for evaluation!
         * 
         * @param newValue
         * @return 
         */
        public T apply(T newValue) {
            return this.apply(newValue, false);
        }

        /**
         * Apply a new value, calculate the agg, and returns the new value.  If
         * you do includeNull, you MUST provide a null-safe comparator otherwise
         * you will get a runtime NullPointerException.
         * 
         * @param newValue
         * @param includeNulls
         * @return 
         */
        public T apply(T newValue, boolean includeNulls) {
            if (includeNulls || newValue != null) {
                if (!includeNulls && this.value == null) {
                    this.value = newValue;
                } else {
                    int c = this.comparator.compare(this.value, newValue);
                    if (c > 0) {
                        this.value = newValue;
                    }
                }
            }
            return this.value;
        }
        
    }
    
    static public class Min<T> extends OrderedValue<T> {
        
        protected Min(Comparator<? super T> comparator, T initialValue) {
            super(comparator, initialValue);
        }
        
    }
    
    static public class Max<T> extends OrderedValue<T> {
        
        protected Max(Comparator<? super T> comparator, T initialValue) {
            super(comparator, initialValue);
        }
        
    }
    
    /**
     * Helps aggregate the min value according to natural ordering of the type.
     * 
     * @param <V>
     * @param type
     * @return 
     */
    static public <V extends Comparable<V>> Min<V> min(Class<V> type) {
        final Comparator<V> comparator = Comparator.naturalOrder();
        return min(comparator, null);
    }    

    /**
     * Helps aggregate the min value according to the natural ordering of the type
     * of initial value.
     * 
     * @param <V>
     * @param initialValue
     * @return 
     */
    static public <V extends Comparable<V>> Min<V> min(V initialValue) {
        final Comparator<V> comparator = Comparator.naturalOrder();
        return min(comparator, initialValue);
    } 
    
    /**
     * Helps aggregate the min value. Comparator is the natural ordering
     * of values such that 1 &lt; 2.
     * 
     * @param <V>
     * @param comparator
     * @return 
     */
    static public <V> Min<V> min(Comparator<V> comparator) {
        return min(comparator, null);
    }
    
    /**
     * Helps aggregate the min value. Comparator is the natural ordering
     * of values such that 1 &lt; 2.
     * 
     * @param <V>
     * @param comparator
     * @param initialValue
     * @return 
     */
    static public <V> Min<V> min(Comparator<V> comparator, V initialValue) {
        return new Min(comparator, initialValue);
    }
    
    /**
     * Helps aggregate the max value according to natural ordering of the type.
     * 
     * @param <V>
     * @param type
     * @return 
     */
    static public <V extends Comparable<V>> Max<V> max(Class<V> type) {
        final Comparator<V> comparator = Comparator.naturalOrder();
        return max(comparator, null);
    }
    
    /**
     * Helps aggregate the max value according to the natural ordering of the type
     * of initial value.
     * 
     * @param <V>
     * @param initialValue
     * @return 
     */
    static public <V extends Comparable<V>> Max<V> max(V initialValue) {
        final Comparator<V> comparator = Comparator.naturalOrder();
        return max(comparator, initialValue);
    }

    /**
     * Helps aggregate the max value. Comparator is the natural ordering
     * of values such that 1 &lt; 2.
     * 
     * @param <V>
     * @param comparator
     * @return 
     */
    static public <V> Max<V> max(Comparator<V> comparator) {
        return max(comparator, null);
    }
    
    /**
     * Helps aggregate the max value. Comparator is the natural ordering
     * of values such that 1 &lt; 2.
     * 
     * @param <V>
     * @param comparator
     * @param initialValue
     * @return 
     */
    static public <V> Max<V> max(Comparator<V> comparator, V initialValue) {
        Objects.requireNonNull(comparator, "comparator was null");
        return new Max(comparator.reversed(), initialValue);
    }
    
}