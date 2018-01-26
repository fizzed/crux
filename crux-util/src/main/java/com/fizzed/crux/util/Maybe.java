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

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Similar to a Java 8 optional, but this version allows the value to be changed
 * (mutable) and is non-final (so it can be subclassed). Useful for simplified non-final
 * variable access in lambdas or subclassing.  Behavior is similiar to Java 8's
 * Optional, but with a few differences.  Here are the reasons why we like it:
 * 
 * @author jjlauer
 * @param <T> The type of Maybe
 */
public class Maybe<T> {
    
    private T value;
    
    public Maybe(T value) {
        this.value = value;
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
    
    public T orElse(T defaultValue) {
        return this.isPresent() ? value : defaultValue;
    }
    
    public T orElse(Supplier<T> defaultValueSupplier) {
        Objects.requireNonNull(defaultValueSupplier, "supplier was null");
        return this.isPresent() ? value : defaultValueSupplier.get();
    }
    
    public boolean isPresent() {
        return this.value != null;
    }
    
    public boolean isAbsent() {
        return this.value == null;
    }
    
    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }
    
    public void ifAbsent(Runnable runnable) {
        if (value == null) {
            runnable.run();
        }
    }
    
    public <U> Maybe<U> map(Function<T,U> mapper) {
        if (value != null) {
            return Maybe.of(mapper.apply(value));
        } else {
            return Maybe.empty();
        }
    }
    
    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }
    
    static public <T> Maybe<T> empty() {
        return new Maybe<>(null);
    }
    
    static public <T> Maybe<T> of(T value) {
        return new Maybe<>(value);
    }
    
}