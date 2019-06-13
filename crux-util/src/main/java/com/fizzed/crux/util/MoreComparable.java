/*
 * Copyright 2019 Fizzed, Inc.
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

/**
 * Extends a Comparable to provide greater than and less than methods.
 * 
 * @author jjlauer
 * @param <T> 
 */
public interface MoreComparable<T> extends Comparable<T> {
 
    /**
     * Whether <code>this</code> value is greater than <code>other</code>
     * value.
     * @param other The other value to compare against
     * @return True if <code>this</code> value is greater than <code>other</code>
     *      value
     */
    default public boolean gt(T other) {
        return this.compareTo(other) > 0;
    }
 
    /**
     * Whether <code>this</code> value is greater than or equal to <code>other</code>
     * value.
     * @param other The other value to compare against
     * @return True if <code>this</code> value is greater than or equal to <code>other</code>
     *      value
     */
    default public boolean gte(T other) {
        return this.compareTo(other) >= 0;
    }
 
    /**
     * Whether <code>this</code> value is less than <code>other</code>
     * value.
     * @param other The other value to compare against
     * @return True if <code>this</code> value is less than <code>other</code>
     *      value
     */
    default public boolean lt(T other) {
        return this.compareTo(other) < 0;
    }
 
    /**
     * Whether <code>this</code> value is less than or equal to <code>other</code>
     * value.
     * @param other The other value to compare against
     * @return True if <code>this</code> value is less than or equal to <code>other</code>
     *      value
     */
    default public boolean lte(T other) {
        return this.compareTo(other) <= 0;
    }
 
    /**
     * The minimum value between <code>this</code> or the other value.
     * @param other The other value to compare against
     * @return Will return <code>this</code> value if less than or equal to
     *      the <code>other</code> value, otherwise will return <code>other</code>.
     */
    default public T min(T other) {
        if (this.lte(other)) {
            return (T)this;
        } else {
            return other;
        }
    }
 
    /**
     * The maximum value between <code>this</code> or the other value.
     * @param other The other value to compare against
     * @return Will return <code>this</code> value if greater than or equal to
     *      the <code>other</code> value, otherwise will return <code>other</code>.
     */
    default public T max(T other) {
        if (this.gte(other)) {
            return (T)this;
        } else {
            return other;
        }
    }
    
}