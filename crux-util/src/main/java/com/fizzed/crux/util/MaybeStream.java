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
    
    final private Iterable<T> values;
    
    protected MaybeStream(Iterable<T> values) {
        this.values = values;
    }

    public Stream<T> get() {
        if (values == null) {
            throw new NoSuchElementException("No values present");
        }
        return this.stream();
    }
    
    public Stream<T> stream() {
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
    
    public Maybe<T> first() {
        return Maybe.of(MoreObjects.first(this.values));
    }
    
    public Maybe<T> last() {
        return Maybe.of(MoreObjects.last(this.values));
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
    
    public MaybeStream<T> forEach(BiConsumer<T,Integer> consumer) {
        if (values == null) {
            return this;
        }
        IterableIter<T> iter = new IterableIter<>(this.values);
        iter.forEach(consumer);
        return this;
    }
    
    public boolean isPresent() {
        return this.values != null;
    }
    
    public boolean isAbsent() {
        return this.values == null;
    }

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
    
    // helpers
    
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
            if (this.values == null) {
                return null;
            }
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