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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author jjlauer
 */
public class MoreObjectsTest {
    
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
    
}
