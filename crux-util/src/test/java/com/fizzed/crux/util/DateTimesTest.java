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

import java.time.Instant;
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
        DateTime c = DateTime.parse("2019-04-01T01:03:03.456Z");
        DateTime d = DateTime.parse("2019-04-04T01:03:03.456Z");
        
        // default joda is null
        assertThat(DateTimes.min(null, null), is(nullValue()));
        assertThat(DateTimes.min(a, null), is(a));
        assertThat(DateTimes.min(null, b), is(b));
        assertThat(DateTimes.min(a, b), is(a));
        assertThat(DateTimes.min(b, a), is(a));
        
        assertThat(DateTimes.min(a, b, c, d), is(c));
        assertThat(DateTimes.min(null, a, b, c, d), is(c));
        assertThat(DateTimes.min((DateTime[])null), is(nullValue()));
    }
    
    @Test
    public void max() {
        DateTime b = DateTime.parse("2019-04-02T01:02:03.456Z");
        DateTime a = DateTime.parse("2019-04-02T01:03:03.456Z");
        DateTime c = DateTime.parse("2019-04-03T01:03:03.456Z");
        DateTime d = DateTime.parse("2019-04-01T01:03:03.456Z");
        
        // default joda is null
        assertThat(DateTimes.max(null, null), is(nullValue()));
        assertThat(DateTimes.max(a, null), is(a));
        assertThat(DateTimes.max(null, b), is(b));
        assertThat(DateTimes.max(a, b), is(a));
        assertThat(DateTimes.max(b, a), is(a));
        
        assertThat(DateTimes.max(a, b, c, d), is(c));
        assertThat(DateTimes.max(null, b, c, d), is(c));
        assertThat(DateTimes.max((DateTime[])null), is(nullValue()));
    }
    
    @Test
    public void gt() {
        DateTime a = DateTime.parse("2019-04-02T01:02:03.456Z");
        DateTime b = DateTime.parse("2019-04-02T01:03:03.456Z");
        
        assertThat(DateTimes.gt(null, null), is(false));
        assertThat(DateTimes.gt(null, a), is(false));
        assertThat(DateTimes.gt(a, b), is(false));
        assertThat(DateTimes.gt(a, a), is(false));
        assertThat(DateTimes.gt(b, b), is(false));
        assertThat(DateTimes.gt(b, a), is(true));
        assertThat(DateTimes.gt(a, null), is(true));
    }
    
    @Test
    public void gte() {
        DateTime a = DateTime.parse("2019-04-02T01:02:03.456Z");
        DateTime b = DateTime.parse("2019-04-02T01:03:03.456Z");
        
        assertThat(DateTimes.gte(null, null), is(true));
        assertThat(DateTimes.gte(null, a), is(false));
        assertThat(DateTimes.gte(a, b), is(false));
        assertThat(DateTimes.gte(a, a), is(true));
        assertThat(DateTimes.gte(b, b), is(true));
        assertThat(DateTimes.gte(b, a), is(true));
        assertThat(DateTimes.gte(a, null), is(true));
    }
    
    @Test
    public void lt() {
        DateTime b = DateTime.parse("2019-04-02T01:02:03.456Z");
        DateTime a = DateTime.parse("2019-04-02T01:03:03.456Z");
        
        assertThat(DateTimes.lt(null, null), is(false));
        assertThat(DateTimes.lt(null, a), is(true));
        assertThat(DateTimes.lt(a, b), is(false));
        assertThat(DateTimes.lt(a, a), is(false));
        assertThat(DateTimes.lt(b, b), is(false));
        assertThat(DateTimes.lt(b, DateTime.parse("2019-04-02T01:02:03.456Z").withZone(DateTimeZone.forID("America/New_York"))), is(false));
        assertThat(DateTimes.lt(b, a), is(true));
        assertThat(DateTimes.lt(a, null), is(false));
    }
    
    @Test
    public void lte() {
        DateTime b = DateTime.parse("2019-04-02T01:02:03.456Z");
        DateTime a = DateTime.parse("2019-04-02T01:03:03.456Z");
        
        assertThat(DateTimes.lte(null, null), is(true));
        assertThat(DateTimes.lte(null, a), is(true));
        assertThat(DateTimes.lte(a, b), is(false));
        assertThat(DateTimes.lte(a, a), is(true));
        assertThat(DateTimes.lte(b, b), is(true));
        assertThat(DateTimes.lte(b, a), is(true));
        assertThat(DateTimes.lte(a, null), is(false));
    }
    
    @Test
    public void age() {
        DateTime a = DateTime.parse("2019-04-02T01:03:03.456Z");
        
        TimeDuration td = DateTimes.age(a, a.getMillis() + 435);
        
        assertThat(td.getDuration(), is(435L));
    }
    
    @Test
    public void within() {
        
        DateTime dt0 = DateTime.parse("2019-04-01T01:03:03.456Z");
        DateTime dt1 = DateTime.parse("2019-04-02T01:03:03.456Z");
        DateTime dt2 = DateTime.parse("2019-04-03T01:03:03.456Z");
        DateTime dt3 = DateTime.parse("2019-04-04T01:03:03.456Z");
        DateTime dt4 = DateTime.parse("2019-04-05T01:03:03.456Z");
        
        assertThat(DateTimes.within(null, dt0, dt2, true, true), is(false));
        assertThat(DateTimes.within(dt1, null, null, true, true), is(false));
        assertThat(DateTimes.within(dt1, null, dt2, true, true), is(false));
        assertThat(DateTimes.within(dt1, dt2, null, true, true), is(false));
        assertThat(DateTimes.within(dt1, dt0, dt2, true, true), is(true));
        assertThat(DateTimes.within(dt1, dt2, dt0, true, true), is(true));      // auto picked?
        assertThat(DateTimes.within(dt1, dt0, dt2, false, false), is(true));
        assertThat(DateTimes.within(dt0, dt2, dt3, true, true), is(false));
        assertThat(DateTimes.within(dt0, dt3, dt4, true, true), is(false));
        assertThat(DateTimes.within(dt0, dt0, dt0, true, true), is(true));
        assertThat(DateTimes.within(dt0, dt0, dt0, false, true), is(false));
        assertThat(DateTimes.within(dt0, dt0, dt0, true, false), is(false));
        assertThat(DateTimes.within(dt0, dt0, dt0, true, false), is(false));
        assertThat(DateTimes.within(dt3, dt0, dt4, true, true), is(true));
        assertThat(DateTimes.within(dt4, dt0, dt4, true, true), is(true));
        assertThat(DateTimes.within(dt4, dt0, dt4, true, false), is(false));
        assertThat(DateTimes.within(dt0, dt0, dt4, true, true), is(true));
        assertThat(DateTimes.within(dt0, dt0, dt4, false, true), is(false));
        assertThat(DateTimes.within(dt0, dt4, dt0, false, true), is(false));
    }
    
    @Test
    public void uptime() {
        DateTime dt0 = DateTime.parse("2019-04-01T01:03:03.456Z");
        DateTime dt1 = DateTime.parse("2019-04-02T02:02:01.123Z");
        DateTime dt2 = DateTime.parse("2019-04-01T01:03:03.457Z");
        DateTime dt3 = DateTime.parse("2019-04-01T01:03:04.456Z");
        DateTime dt4 = DateTime.parse("2019-04-01T02:03:04.456Z");
        DateTime dt5 = DateTime.parse("2019-04-03T02:03:04.456Z");
        DateTime dt6 = DateTime.parse("2019-04-05T02:03:04.456Z");
        DateTime dt7 = DateTime.parse("2019-04-10T02:03:04.456Z");
        DateTime dt8 = DateTime.parse("2019-04-10T03:12:02.812Z");
        
        assertThat(DateTimes.uptime(dt2, dt2), is("0ms"));
        assertThat(DateTimes.uptime(dt2, dt0), is("1ms"));
        assertThat(DateTimes.uptime(dt1, dt0), is("1d 58m 57s 667ms"));
        assertThat(DateTimes.uptime(dt3, dt0), is("1s"));
        assertThat(DateTimes.uptime(dt4, dt0), is("1h 1s"));
        assertThat(DateTimes.uptime(dt5, dt0), is("2d 1h 1s"));
        assertThat(DateTimes.uptime(dt6, dt0), is("4d 1h 1s"));
        assertThat(DateTimes.uptime(dt7, dt0), is("9d 1h 1s"));
        assertThat(DateTimes.uptime(dt8, dt0), is("9d 2h 8m 59s 356ms"));
    }
    
}