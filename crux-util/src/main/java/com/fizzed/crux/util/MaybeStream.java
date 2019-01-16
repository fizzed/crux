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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A null-safe way of working with iterables and arrays as streams.  Useful
 * for iterating with indexes too :-)
 * 
 * @author jjlauer
 * @param <T> The type of Maybe
 */
public class MaybeStream<T> implements Iterable<T> {
    
    private Iterable<T> values;
    
    public MaybeStream(Iterable<T> values) {
        this.values = values;
    }

    public Stream<T> get() {
        if (values == null) {
            throw new NoSuchElementException("No values present");
        }
        return this.toStream();
    }
    
    public Stream<T> orEmpty() {
        return this.toStream();
    }
    
    @Override
    public Iterator<T> iterator() {
        if (values == null) {
            return new ArrayIterator<>(null);   // empty
        }
        // optimized for collections (e.g. List)
        if (values instanceof Collection) {
            return ((Collection<T>)values).iterator();
        } else {
            return values.iterator();
        }
    }
    
    private Stream<T> toStream() {
        if (values == null) {
            return Stream.empty();
        }
        // optimized for collections (e.g. List)
        if (values instanceof Collection) {
            return ((Collection<T>)values).stream();
        } else {
            return StreamSupport.stream(this.values.spliterator(), false);
        }
    }
    
    public MaybeStream<T> map(Consumer<T> consumer) {
        return this.map((v,i) -> consumer.accept(v));
    }
    
    public MaybeStream<T> map(BiConsumer<T,Integer> consumer) {
        if (values == null) {
            return this;
        }
        IterableIter<T> iter = new IterableIter<>(this.values);
        iter.forEach(consumer);
        return this;
    }
    
//    public T get() {
//        if (value == null) {
//            throw new NoSuchElementException("No value present");
//        }
//        return value;
//    }
//
//    public void set(T value) {
//        this.value = value;
//    }
//    
//    public T orElse(T defaultValue) {
//        return this.isPresent() ? value : defaultValue;
//    }
//    
//    public T orGet(Supplier<T> defaultSupplier) {
//        Objects.requireNonNull(defaultSupplier, "supplier was null");
//        return this.isPresent() ? value : defaultSupplier.get();
//    }
//    
//    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X {
//        if (this.value == null) {
//            throw exceptionSupplier.get();
//        }
//        return this.value;
//    }
    
    public boolean isPresent() {
        return this.values != null;
    }
    
    public boolean isAbsent() {
        return this.values == null;
    }
    
//    
//    public MaybeStream<T> ifPresent(Consumer<? super T> consumer) {
//        if (value != null) {
//            consumer.accept(value);
//        }
//        return this;
//    }
//    
//    public MaybeStream<T> ifAbsent(Runnable runnable) {
//        if (value == null) {
//            runnable.run();
//        }
//        return this;
//    }
//    
//    public <U> MaybeStream<U> typed(Class<? super U> type) {
//        if (this.value != null && type.isInstance(this.value)) {
//            return MaybeStream.of((U)this.value);
//        } else {
//            return MaybeStream.empty();
//        }
//    }
//    
//    public MaybeStream<T> filter(Predicate<? super T> predicate) {
//        if (value != null) {
//            if (predicate.test(value)) {
//                return this;
//            } else {
//                return MaybeStream.empty();
//            }
//        }
//        return this;
//    }
//    
//    public <U> MaybeStream<U> map(Function<? super T, ? extends U> mapper) {
//        if (value != null) {
//            return MaybeStream.of(mapper.apply(value));
//        } else {
//            return MaybeStream.empty();
//        }
//    }
//    
//    public <U> MaybeStream<U> flatMap(Function<? super T, MaybeStream<U>> mapper) {
//        if (value != null) {
//            return mapper.apply(value);
//        } else {
//            return MaybeStream.empty();
//        }
//    }
//    
//    public <U> MaybeStream<U> optMap(Function<? super T, Optional<U>> mapper) {
//        if (value != null) {
//            return MaybeStream.of(mapper.apply(value));
//        } else {
//            return MaybeStream.empty();
//        }
//    }
//    
//    public Optional<T> toOptional() {
//        return Optional.ofNullable(value);
//    }
    
    static public <T> MaybeStream<T> empty() {
        return new MaybeStream<>(null);
    }
    
    static public <T> MaybeStream<T> of(T[] values) {
        if (values == null) {
            return empty();
        }
        return maybeStream(new ArrayIterable<>(values));
    }
    
    static public <T> MaybeStream<T> of(Iterable<T> values) {
        return maybeStream(values);
    }
    
    static public <T> MaybeStream<T> maybeStream(T[] values) {
        return MaybeStream.of(values);
    }
    
    static public <T> MaybeStream<T> maybeStream(Iterable<T> value) {
        return new MaybeStream<>(value);
    }

//    @Override
//    public String toString() {
//        return this.value != null ? this.value.toString() : null;
//    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//
//        if (!(obj instanceof MaybeStream)) {
//            return false;
//        }
//
//        MaybeStream<?> other = (MaybeStream<?>) obj;
//        return Objects.equals(value, other.value);
//    }

    /**
     * Returns the hash code of the value, if present, otherwise {@code 0}
     * (zero) if no value is present.
     *
     * @return hash code value of the present value or {@code 0} if no value is
     *         present
     */
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(value);
//    }
    
    static public class ArrayIterable<V> implements Iterable<V> {
        
        private final V[] values;

        public ArrayIterable(V[] values) {
            this.values = values;
        }
        
        @Override
        public Iterator<V> iterator() {
            return new ArrayIterator<>(this.values);
        }
        
    }
    
    static public class ArrayIterator<V> implements Iterator<V> {

        private final V[] values;
        private int index;

        public ArrayIterator(V[] values) {
            this.values = values;
            this.index = 0;
        }
        
        @Override
        public boolean hasNext() {
            return this.values != null && this.index < this.values.length;
        }

        @Override
        public V next() {
            V value = this.values[this.index];
            this.index++;
            return value;
        }
        
    }
    
    static public interface Iter<V> {
        void forEach(BiConsumer<V,Integer> consumer);
    }
    
    static public class IterableIter<V> implements Iter<V> {
        
        private final Iterable<V> values;

        public IterableIter(Iterable<V> values) {
            this.values = values;
        }

        @Override
        public void forEach(BiConsumer<V, Integer> consumer) {
            if (this.values == null) {
                return;
            }
            final Iterator<V> iterator = this.values.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                V value = iterator.next();
                consumer.accept(value, i);
                i++;
            }
        }
    }
    
}