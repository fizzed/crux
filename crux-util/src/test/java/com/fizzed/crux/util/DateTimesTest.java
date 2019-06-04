/*
 * Copyright 2019 Fizzed, Inc.
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class DateTimesTest {
 
    @Test
    public void now() {
        assertThat(DateTimes.now().getZone(), is(DateTimeZone.UTC));
    }
    
    @Test
    public void equals() {
        DateTime a = DateTime.parse("2019-04-02T01:02:03.456Z");
        DateTime b = a.withZone(DateTimeZone.forID("America/Los_Angeles"));
        
        // default joda is null
        assertThat(a.equals(b), is(false));
        assertThat(a.getMillis(), is(b.getMillis()));
        assertThat(DateTimes.equals(a, b), is(true));
    }
    
    @Test
    public void min() {
        DateTime a = DateTime.parse("2019-04-02T01:02:03.456Z");
        DateTime b = DateTime.parse("2019-04-02T01:03:03.456Z");
        
        // default joda is null
        assertThat(DateTimes.min(null, null), is(nullValue()));
        assertThat(DateTimes.min(a, null), is(a));
        assertThat(DateTimes.min(null, b), is(b));
        assertThat(DateTimes.min(a, b), is(a));
        assertThat(DateTimes.min(b, a), is(a));
    }
    
    @Test
    public void max() {
        DateTime b = DateTime.parse("2019-04-02T01:02:03.456Z");
        DateTime a = DateTime.parse("2019-04-02T01:03:03.456Z");
        
        // default joda is null
        assertThat(DateTimes.max(null, null), is(nullValue()));
        assertThat(DateTimes.max(a, null), is(a));
        assertThat(DateTimes.max(null, b), is(b));
        assertThat(DateTimes.max(a, b), is(a));
        assertThat(DateTimes.max(b, a), is(a));
    }
    
    @Test
    public void age() {
        DateTime a = DateTime.parse("2019-04-02T01:03:03.456Z");
        
        TimeDuration td = DateTimes.age(a, a.getMillis() + 435);
        
        assertThat(td.getDuration(), is(435L));
    }
    
}