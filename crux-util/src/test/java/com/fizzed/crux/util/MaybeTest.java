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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

public class MaybeTest {
 
    @Test
    public void of() {
        Maybe a = Maybe.of(null);
        
        assertThat(a.isAbsent(), is(true));
        assertThat(a.isPresent(), is(false));
        assertThat(a.orElse("blah"), is("blah"));
        assertThat(a.orElse(() -> "blah"), is("blah"));
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
    
}