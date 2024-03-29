/*
 * Copyright 2021 Fizzed, Inc.
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
package com.fizzed.crux.matchers;

import com.fizzed.crux.util.JavaTimes;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import java.time.Instant;
import org.hamcrest.BaseMatcher;

public class JavaTimeMatchers {
    
    static public Matcher<Instant> sameToMillis(final Instant expected) {
        return sameToMillis(expected, true);
    }
    
    static public Matcher<Instant> sameToMillis(final Instant expected, boolean rounding) {
        return new BaseMatcher<Instant>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("value should be ").appendValue(expected);
            }
            
            @Override
            public void describeMismatch(Object actual, Description mismatchDescription) {
                mismatchDescription.appendText(" was ").appendValue(actual);
            }

            @Override
            public boolean matches(Object actual) {
                final Instant v = (Instant)actual;
                return JavaTimes.sameMillisPrecision(
                    expected,
                    v,
                    rounding);
            }
        };
    }
    
}