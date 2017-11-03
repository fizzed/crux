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

import java.util.Optional;

/**
 * Similar to a Java 8 optional, but this version is mutable, non-final, and
 * will return a null if empty (Java 8's throws an exception by default).
 * Useful for simplified non-final variable access in lambdas.
 * 
 * @author jjlauer
 */
public class Maybe<T> {
    
    private T value;
    
    public Maybe() {
        this(null);
    }
    
    public Maybe(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
    
    public boolean isPresent() {
        return this.value != null;
    }
    
    public boolean isAbsent() {
        return this.value == null;
    }
    
    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }
    
    static public <T> Maybe<T> empty() {
        return new Maybe<>();
    }
    
    static public <T> Maybe<T> of(T value) {
        return new Maybe<>(value);
    }
    
}