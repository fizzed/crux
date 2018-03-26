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
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Similar to a Java 8 optional, but this version is different in the following
 * ways:
 * <ul>
 *  <li>Nulls are accepted in constructor (no need for of() vs. ofNullable())
 *  <li>Value is mutable (useful to get around lambda & final variables restriction)
 *  <li>Non-final and can be subclassed
 *  <li>toString() returns the .toString() of the value rather than Optional[value]
 * </ul>
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
    
    public T orGet(Supplier<T> defaultSupplier) {
        Objects.requireNonNull(defaultSupplier, "supplier was null");
        return this.isPresent() ? value : defaultSupplier.get();
    }
    
    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.value == null) {
            throw exceptionSupplier.get();
        }
        return this.value;
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
    
    public <U> Maybe<U> typed(Class<? super U> type) {
        if (this.value != null && type.isInstance(this.value)) {
            return Maybe.of((U)this.value);
        } else {
            return Maybe.empty();
        }
    }
    
    public Maybe<T> filter(Predicate<? super T> predicate) {
        if (value != null) {
            if (predicate.test(value)) {
                return this;
            } else {
                return Maybe.empty();
            }
        }
        return this;
    }
    
    public <U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
        if (value != null) {
            return Maybe.of(mapper.apply(value));
        } else {
            return Maybe.empty();
        }
    }
    
    public <U> Maybe<U> flatMap(Function<? super T, Maybe<U>> mapper) {
        if (value != null) {
            return mapper.apply(value);
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
    
    static public <T> Maybe<T> maybe(T value) {
        return new Maybe<>(value);
    }

    @Override
    public String toString() {
        return this.value != null ? this.value.toString() : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Maybe)) {
            return false;
        }

        Maybe<?> other = (Maybe<?>) obj;
        return Objects.equals(value, other.value);
    }

    /**
     * Returns the hash code of the value, if present, otherwise {@code 0}
     * (zero) if no value is present.
     *
     * @return hash code value of the present value or {@code 0} if no value is
     *         present
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}