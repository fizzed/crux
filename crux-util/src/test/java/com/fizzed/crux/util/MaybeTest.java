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

import static com.fizzed.crux.util.Maybe.maybe;
import static java.util.Arrays.asList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

public class MaybeTest {
 
    @Test
    public void map() {
        String s = Maybe.of(4)
            .map(i -> i.toString())
            .orElse("dude");
        
        assertThat(s, is("4"));
        
        String t = Maybe.of((Integer)null)
            .map(i -> i.toString())
            .orElse("dude");
        
        assertThat(t, is("dude"));
    }
 
    @Test
    public void of() {
        Maybe a = Maybe.of(null);
        
        assertThat(a.isAbsent(), is(true));
        assertThat(a.isPresent(), is(false));
        assertThat(a.orElse("blah"), is("blah"));
        assertThat(a.orGet(() -> "blah"), is("blah"));
        a.ifPresent(v -> fail("should fail"));
        
        Maybe<String> b = Maybe.of("dude");
        
        assertThat(b.get(), is("dude"));
        assertThat(b.orElse("blah"), is("dude"));
        assertThat(b.isAbsent(), is(false));
        assertThat(b.isPresent(), is(true));
        b.ifAbsent(() -> fail("should fail"));
    }
    
    @Test
    public void getThrowsNoSuchElement() {
        Maybe<String> a = Maybe.of(null);
        
        try {
            a.get();
        } catch (NoSuchElementException e) {
            // expected
        }
        
        Maybe<String> b = Maybe.empty();
        
        try {
            b.get();
        } catch (NoSuchElementException e) {
            // expected
        }
    }
    
    @Test
    public void orElse() {
        Maybe<String> s = maybe("dude");
        
        assertThat(s.orElse("yo"), is("dude"));
        assertThat(Maybe.empty().orElse("yo"), is("yo"));
        assertThat(Maybe.empty().orGet(() -> "yo"), is("yo"));
    }
    
    @Test
    public void flatMapAndOptMap() {
        Maybe<String> s = maybe("dude");
        
        assertThat(s.flatMap(v -> maybe("yo")).orElse(null), is("yo"));
        assertThat(s.optMap(v -> Optional.ofNullable("yo")).orElse(null), is("yo"));
    }

    static public class Widget {
        
        private List<String> values;
        private String[] strings;
        private Set<Integer> set;

        public void setValues(List<String> values) {
            this.values = values;
        } 
        
        public List<String> getValues() {
            return this.values;
        }

        public String[] getStrings() {
            return strings;
        }

        public void setStrings(String[] strings) {
            this.strings = strings;
        }

        public Set<Integer> getSet() {
            return set;
        }

        public void setSet(Set<Integer> set) {
            this.set = set;
        }

    }
    
    @Test
    public void maybeStream() {
        Widget w = new Widget();
        
        long count = maybe(w)
            .mapStream(v -> v.getValues())
            .count();

        assertThat(count, is(0L));
        
        w.setValues(asList("a", "b", "c"));
        
        count = maybe(w)
            .mapStream(v -> v.getValues())
            .count();

        assertThat(count, is(3L));
     
        w.setSet(new HashSet<>(asList(1, 2)));
        
        count = maybe(w)
            .mapStream(v -> v.getSet())
            .count();

        assertThat(count, is(2L));
    }
    
//    @Test
//    public void typed() {
//        Map<String,String> a = new HashMap<>();
//        
//        maybe(a)
//            .typed(HashMap<String,String>)
//    }
    
}