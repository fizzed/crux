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

import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author jjlauer
 */
public class MoreObjectsTest {
    
    @Test
    public void isTrue() {
        assertThat(MoreObjects.isTrue(null), is(false));
        assertThat(MoreObjects.isTrue(false), is(false));
        assertThat(MoreObjects.isTrue(true), is(true));
        assertThat(MoreObjects.isTrue(Boolean.TRUE), is(true));
        assertThat(MoreObjects.isTrue(Boolean.FALSE), is(false));
    }
    
    @Test
    public void in() {
        assertThat(MoreObjects.in(null, "a", null), is(true));
        
        assertThat(MoreObjects.in("a", "b", "a"), is(true));
        assertThat(MoreObjects.in("c", "b", "a"), is(false));
        assertThat(MoreObjects.in("a", new String[] { "b", "a" }), is(true));
        assertThat(MoreObjects.in("a", Arrays.asList("b", "a")), is(true));
        
        Set<String> set = new HashSet<>();
        set.add("b");
        set.add("a");
        assertThat(MoreObjects.in("a", set), is(true));
        assertThat(MoreObjects.in("c", set), is(false));
    }
    
    @Test
    public void iterable() {
        for (String s : MoreObjects.iterable((String[])null)) {
            
        }
        for (String s : MoreObjects.iterable(new String[] { "s" })) {
            assertThat(s, is("s"));
        }
        for (Object s : MoreObjects.iterable((ArrayList)null)) {
            
        }
        for (String s : MoreObjects.iterable(asList("s"))) {
            assertThat(s, is("s"));
        }
    }
    
    @Test
    public void size() {
        assertThat(MoreObjects.size((Object[])null), is(0));
        assertThat(MoreObjects.size(new Object[0]), is(0));
        assertThat(MoreObjects.size((Collection)null), is(0));
        assertThat(MoreObjects.size(new ArrayList<>()), is(0));
        assertThat(MoreObjects.size((Map)null), is(0));
        assertThat(MoreObjects.size(new HashMap<>()), is(0));
    }
    
    @Test
    public void first() {
        assertThat(MoreObjects.first((Object[])null), is(nullValue()));
        assertThat(MoreObjects.first(new Object[0]), is(nullValue()));
        assertThat(MoreObjects.first(new String[] { "s" }), is("s"));
        assertThat(MoreObjects.first((Collection)null), is(nullValue()));
        assertThat(MoreObjects.first(new ArrayList<>()), is(nullValue()));
        assertThat(MoreObjects.first(asList("s")), is("s"));
    }
    
    @Test
    public void isEmpty() {
        assertThat(MoreObjects.isEmpty((Object[])null), is(true));
        assertThat(MoreObjects.isEmpty(new HashSet<>()), is(true));
        assertThat(MoreObjects.isEmpty(new ArrayList<>()), is(true));
        Iterable<String> iterable = new ArrayList<>();
        assertThat(MoreObjects.isEmpty(iterable), is(true));
        assertThat(MoreObjects.isEmpty((String)null), is(true));
        assertThat(MoreObjects.isEmpty(""), is(true));
    }
    
    @Test
    public void isBlank() {
        assertThat(MoreObjects.isBlank((String)null), is(true));
        assertThat(MoreObjects.isBlank(""), is(true));
        assertThat(MoreObjects.isBlank(" "), is(true));
        assertThat(MoreObjects.isBlank("  "), is(true));
        assertThat(MoreObjects.isBlank("d"), is(false));
    }
    
}
