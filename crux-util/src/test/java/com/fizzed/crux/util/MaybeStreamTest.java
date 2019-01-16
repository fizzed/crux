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

import static com.fizzed.crux.util.MaybeStream.maybeStream;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

public class MaybeStreamTest {
 
    @Test
    public void nullLists() {
        List<String> list = null;
        
        MaybeStream<String> maybeList = maybeStream(list);
        
        try {
            maybeList.get();
            fail();
        } catch (NoSuchElementException e) {
            // expected
        }
        
        assertThat(maybeList.orEmpty(), is(not(nullValue())));
        assertThat(maybeList.isAbsent(), is(true));
        assertThat(maybeList.isPresent(), is(false));
        
        AtomicInteger forEachCounter = new AtomicInteger();
        maybeList.map((v,i) -> {
           forEachCounter.incrementAndGet();
        });
        
        assertThat(forEachCounter.get(), is(0));
        
        assertThat(maybeList.orEmpty().count(), is(0L));
    }
    
    @Test
    public void actualLists() {
        List<String> list = Arrays.asList("1", "2", "3");
        
        MaybeStream<String> maybeList = maybeStream(list);

        assertThat(maybeList.get(), is(not(nullValue())));
        assertThat(maybeList.isAbsent(), is(false));
        assertThat(maybeList.isPresent(), is(true));
        
        AtomicInteger forEachCounter = new AtomicInteger();
        maybeList.map((v,i) -> {
           forEachCounter.incrementAndGet();
        });
        
        assertThat(forEachCounter.get(), is(3));
        
        assertThat(maybeList.orEmpty().count(), is(3L));
    }
    
    @Test
    public void emptyArrays() {
        String[] a = null;
        
        MaybeStream<String> maybeList = maybeStream(a);
        
        try {
            maybeList.get();
            fail();
        } catch (NoSuchElementException e) {
            // expected
        }
        
        assertThat(maybeList.orEmpty(), is(not(nullValue())));
        assertThat(maybeList.isAbsent(), is(true));
        assertThat(maybeList.isPresent(), is(false));
        
        AtomicInteger forEachCounter = new AtomicInteger();
        maybeList.map((v,i) -> {
           forEachCounter.incrementAndGet();
        });
        
        assertThat(forEachCounter.get(), is(0));
        
        maybeList.map(v -> {
           forEachCounter.incrementAndGet();
        });
        
        assertThat(forEachCounter.get(), is(0));
        
        
        assertThat(maybeList.orEmpty().count(), is(0L));
    }
 
    @Test
    public void actualArrays() {
        String[] a = { "1", "2", "3" };
        
        MaybeStream<String> maybeList = maybeStream(a);

        assertThat(maybeList.get(), is(not(nullValue())));
        assertThat(maybeList.isAbsent(), is(false));
        assertThat(maybeList.isPresent(), is(true));
        
        AtomicInteger forEachCounter = new AtomicInteger();
        maybeList.map((v,i) -> {
           forEachCounter.incrementAndGet();
        });
        
        assertThat(forEachCounter.get(), is(3));
        
        assertThat(maybeList.orEmpty().count(), is(3L));
    }
    
    @Test
    public void enhancedForLoop() {
        String[] a = { "1", "2", "3" };
        
        MaybeStream<String> maybeList = maybeStream(a);
        
        int count = 0;
        for (String v : maybeList) {
            count++;
        }
        
        assertThat(count, is(3));
    }
    
}