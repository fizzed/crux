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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JavaTimeMatchers {
    
    static public Matcher<Instant> isSameToMillis(final Instant expected) {
        return new TypeSafeDiagnosingMatcher<Instant>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("value should be ").appendValue(expected);
            }

            @Override
            protected boolean matchesSafely(final Instant actual, final Description mismatchDescription) {
                mismatchDescription.appendText(" was ").appendValue(actual);
                if (actual == null && expected == null) {
                    return true;
                }
                if (actual != null && expected == null) {
                    return false;
                }
                if (expected != null && actual == null) {
                    return false;
                }
                final Instant expectedTruncated = expected.truncatedTo(ChronoUnit.MILLIS);
                final Instant actualTruncated = actual.truncatedTo(ChronoUnit.MILLIS);
                return expectedTruncated.equals(actualTruncated);
            }
        };
    }
    
//    static public Matcher<Instant> isSameToMillis(final ZonedDateTime expected) {
//        return new TypeSafeDiagnosingMatcher<Instant>() {
//            @Override
//            public void describeTo(final Description description) {
//                description.appendText("value should be ").appendValue(expected);
//            }
//
//            @Override
//            protected boolean matchesSafely(final Instant actual, final Description mismatchDescription) {
//                mismatchDescription.appendText(" was ").appendValue(actual);
//                if (actual == null && expected == null) {
//                    return true;
//                }
//                if (actual != null) {
//                    return false;
//                }
//                if (expected != null) {
//                    return false;
//                }
//                final Instant expectedTruncated = expected.truncatedTo(ChronoUnit.MILLIS);
//                final Instant actualTruncated = actual.truncatedTo(ChronoUnit.MILLIS);
//                return expectedTruncated.equals(actualTruncated);
//            }
//        };
//    }
    
}