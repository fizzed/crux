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

import com.fizzed.crux.util.DateTimes;
import com.fizzed.crux.util.JavaTimes;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.joda.time.DateTime;

public class JodaTimeMatchers {
    
    /**
     * Is the Joda DateTime value the same moment in time regardless of timezone.
     * @param expected
     * @return 
     */
    static public Matcher<DateTime> jodaSameToMillis(final DateTime expected) {
        return new BaseMatcher<DateTime>() {
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
                final DateTime v = (DateTime)actual;
                return JavaTimes.sameMillisPrecision(
                    DateTimes.javaInstant(expected),
                    DateTimes.javaInstant(v),
                    false);
            }
        };
    }
    
}