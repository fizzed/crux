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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;

public class JavaTimesTest {
 
    @Test
    public void now() {
        assertThat(JavaTimes.now().getZone(), is(ZoneId.of("Z")));
    }
    
    @Test
    public void age() {
        ZonedDateTime a = ZonedDateTime.parse("2019-04-02T01:03:03.456Z");
        
        TimeDuration td = JavaTimes.age(a, a.toInstant().toEpochMilli() + 435);
        
        assertThat(td.getDuration(), is(435L));
    }
    
    @Test
    public void uptime() {
        Instant dt0 = Instant.parse("2019-04-01T01:03:03.456Z");
        Instant dt1 = Instant.parse("2019-04-02T02:02:01.123Z");
        Instant dt2 = Instant.parse("2019-04-01T01:03:03.457Z");
        Instant dt3 = Instant.parse("2019-04-01T01:03:04.456Z");
        Instant dt4 = Instant.parse("2019-04-01T02:03:04.456Z");
        Instant dt5 = Instant.parse("2019-04-03T02:03:04.456Z");
        Instant dt6 = Instant.parse("2019-04-05T02:03:04.456Z");
        Instant dt7 = Instant.parse("2019-04-10T02:03:04.456Z");
        Instant dt8 = Instant.parse("2019-04-10T03:12:02.812Z");
        
        assertThat(JavaTimes.uptime(dt2, dt2), is("0ms"));
        assertThat(JavaTimes.uptime(dt2, dt0), is("1ms"));
        assertThat(JavaTimes.uptime(dt1, dt0), is("1d 58m 57s 667ms"));
        assertThat(JavaTimes.uptime(dt3, dt0), is("1s"));
        assertThat(JavaTimes.uptime(dt4, dt0), is("1h 1s"));
        assertThat(JavaTimes.uptime(dt5, dt0), is("2d 1h 1s"));
        assertThat(JavaTimes.uptime(dt6, dt0), is("4d 1h 1s"));
        assertThat(JavaTimes.uptime(dt7, dt0), is("9d 1h 1s"));
        assertThat(JavaTimes.uptime(dt8, dt0), is("9d 2h 8m 59s 356ms"));
    }

    @Test
    public void min() {
        Instant a = Instant.parse("2019-04-02T01:02:03.456Z");
        Instant b = Instant.parse("2019-04-02T01:03:03.456Z");
        Instant c = Instant.parse("2019-04-01T01:03:03.456Z");
        Instant d = Instant.parse("2019-04-04T01:03:03.456Z");

        // default joda is null
        assertThat(JavaTimes.min(null, null), is(nullValue()));
        assertThat(JavaTimes.min(a, null), is(a));
        assertThat(JavaTimes.min(null, b), is(b));
        assertThat(JavaTimes.min(a, b), is(a));
        assertThat(JavaTimes.min(b, a), is(a));

        assertThat(JavaTimes.min(a, b, c, d), is(c));
        assertThat(JavaTimes.min(null, a, b, c, d), is(c));
        assertThat(JavaTimes.min((Instant[])null), is(nullValue()));
    }

    @Test
    public void max() {
        Instant b = Instant.parse("2019-04-02T01:02:03.456Z");
        Instant a = Instant.parse("2019-04-02T01:03:03.456Z");
        Instant c = Instant.parse("2019-04-03T01:03:03.456Z");
        Instant d = Instant.parse("2019-04-01T01:03:03.456Z");

        // default joda is null
        assertThat(JavaTimes.max(null, null), is(nullValue()));
        assertThat(JavaTimes.max(a, null), is(a));
        assertThat(JavaTimes.max(null, b), is(b));
        assertThat(JavaTimes.max(a, b), is(a));
        assertThat(JavaTimes.max(b, a), is(a));

        assertThat(JavaTimes.max(a, b, c, d), is(c));
        assertThat(JavaTimes.max(null, b, c, d), is(c));
        assertThat(JavaTimes.max((Instant[])null), is(nullValue()));
    }

    @Test
    public void gt() {
        Instant a = Instant.parse("2019-04-02T01:02:03.456Z");
        Instant b = Instant.parse("2019-04-02T01:03:03.456Z");

        assertThat(JavaTimes.gt(null, null), is(false));
        assertThat(JavaTimes.gt(null, a), is(false));
        assertThat(JavaTimes.gt(a, b), is(false));
        assertThat(JavaTimes.gt(a, a), is(false));
        assertThat(JavaTimes.gt(b, b), is(false));
        assertThat(JavaTimes.gt(b, a), is(true));
        assertThat(JavaTimes.gt(a, null), is(true));
    }

    @Test
    public void gte() {
        Instant a = Instant.parse("2019-04-02T01:02:03.456Z");
        Instant b = Instant.parse("2019-04-02T01:03:03.456Z");

        assertThat(JavaTimes.gte(null, null), is(true));
        assertThat(JavaTimes.gte(null, a), is(false));
        assertThat(JavaTimes.gte(a, b), is(false));
        assertThat(JavaTimes.gte(a, a), is(true));
        assertThat(JavaTimes.gte(b, b), is(true));
        assertThat(JavaTimes.gte(b, a), is(true));
        assertThat(JavaTimes.gte(a, null), is(true));
    }

    @Test
    public void lt() {
        Instant b = Instant.parse("2019-04-02T01:02:03.456Z");
        Instant a = Instant.parse("2019-04-02T01:03:03.456Z");

        assertThat(JavaTimes.lt(null, null), is(false));
        assertThat(JavaTimes.lt(null, a), is(true));
        assertThat(JavaTimes.lt(a, b), is(false));
        assertThat(JavaTimes.lt(a, a), is(false));
        assertThat(JavaTimes.lt(b, b), is(false));
        assertThat(JavaTimes.lt(b, a), is(true));
        assertThat(JavaTimes.lt(a, null), is(false));
    }

    @Test
    public void lte() {
        Instant b = Instant.parse("2019-04-02T01:02:03.456Z");
        Instant a = Instant.parse("2019-04-02T01:03:03.456Z");

        assertThat(JavaTimes.lte(null, null), is(true));
        assertThat(JavaTimes.lte(null, a), is(true));
        assertThat(JavaTimes.lte(a, b), is(false));
        assertThat(JavaTimes.lte(a, a), is(true));
        assertThat(JavaTimes.lte(b, b), is(true));
        assertThat(JavaTimes.lte(b, a), is(true));
        assertThat(JavaTimes.lte(a, null), is(false));
    }

    @Test
    public void within() {

        Instant dt0 = Instant.parse("2019-04-01T01:03:03.456Z");
        Instant dt1 = Instant.parse("2019-04-02T01:03:03.456Z");
        Instant dt2 = Instant.parse("2019-04-03T01:03:03.456Z");
        Instant dt3 = Instant.parse("2019-04-04T01:03:03.456Z");
        Instant dt4 = Instant.parse("2019-04-05T01:03:03.456Z");

        assertThat(JavaTimes.within(null, dt0, dt2, true, true), is(false));
        assertThat(JavaTimes.within(dt1, null, null, true, true), is(false));
        assertThat(JavaTimes.within(dt1, null, dt2, true, true), is(false));
        assertThat(JavaTimes.within(dt1, dt2, null, true, true), is(false));
        assertThat(JavaTimes.within(dt1, dt0, dt2, true, true), is(true));
        assertThat(JavaTimes.within(dt1, dt2, dt0, true, true), is(true));      // auto picked?
        assertThat(JavaTimes.within(dt1, dt0, dt2, false, false), is(true));
        assertThat(JavaTimes.within(dt0, dt2, dt3, true, true), is(false));
        assertThat(JavaTimes.within(dt0, dt3, dt4, true, true), is(false));
        assertThat(JavaTimes.within(dt0, dt0, dt0, true, true), is(true));
        assertThat(JavaTimes.within(dt0, dt0, dt0, false, true), is(false));
        assertThat(JavaTimes.within(dt0, dt0, dt0, true, false), is(false));
        assertThat(JavaTimes.within(dt0, dt0, dt0, true, false), is(false));
        assertThat(JavaTimes.within(dt3, dt0, dt4, true, true), is(true));
        assertThat(JavaTimes.within(dt4, dt0, dt4, true, true), is(true));
        assertThat(JavaTimes.within(dt4, dt0, dt4, true, false), is(false));
        assertThat(JavaTimes.within(dt0, dt0, dt4, true, true), is(true));
        assertThat(JavaTimes.within(dt0, dt0, dt4, false, true), is(false));
        assertThat(JavaTimes.within(dt0, dt4, dt0, false, true), is(false));
    }

    @Test
    public void overlap() {
        // range 2 starts at the exact same instant that range1 ends
        Instant start1 = Instant.parse("2022-01-01T00:00:00.000000Z");
        Instant end1 = Instant.parse("2022-01-02T00:00:00.000000Z");
        Instant start2 = Instant.parse("2022-01-02T00:00:00.000000Z");
        Instant end2 = Instant.parse("2022-01-03T00:00:00.000000Z");
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, true, true), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, true), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, false), is(false));

        // force ranges to be swapped so the order doesn't matter
        assertThat(JavaTimes.overlap(start2, end2, start1, end1), is(false));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, true, true), is(true));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, false, true), is(true));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, false, false), is(false));

        // all nulls.... always overlaps...
        assertThat(JavaTimes.overlap(null, null, null, null), is(true));
        assertThat(JavaTimes.overlap(null, null, null, null, true, true), is(true));
        assertThat(JavaTimes.overlap(null, null, null, null, false, true), is(true));
        assertThat(JavaTimes.overlap(null, null, null, null, false, false), is(true));

        // range 2 starts .000001 after range 1 ends
        start1 = Instant.parse("2022-01-01T00:00:00.000000Z");
        end1 = Instant.parse("2022-01-02T00:00:00.000000Z");
        start2 = Instant.parse("2022-01-02T00:00:00.000001Z");
        end2 = Instant.parse("2022-01-03T00:00:00.000000Z");
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, true, true), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, true), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, false), is(false));

        // force ranges to be swapped so the order doesn't matter
        assertThat(JavaTimes.overlap(start2, end2, start1, end1), is(false));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, true, true), is(false));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, false, true), is(false));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, false, false), is(false));

        // range 2 had no start and range1 is before range2 end
        start1 = Instant.parse("2022-01-01T00:00:00.000000Z");
        end1 = Instant.parse("2022-01-02T00:00:00.000000Z");
        start2 = null;
        end2 = Instant.parse("2022-01-03T00:00:00.000000Z");
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, true, true), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, true), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, false), is(true));

        // range2 completely within range1 
        start1 = Instant.parse("2022-01-01T00:00:00.000000Z");
        end1 = Instant.parse("2022-01-10T00:00:00.000000Z");
        start2 = Instant.parse("2022-01-02T00:00:00.000001Z");
        end2 = Instant.parse("2022-01-03T00:00:00.000000Z");
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, true, true), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, true), is(true));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, false), is(true));

        // force ranges to be swapped so the order doesn't matter
        assertThat(JavaTimes.overlap(start2, end2, start1, end1), is(true));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, true, true), is(true));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, false, true), is(true));
        assertThat(JavaTimes.overlap(start2, end2, start1, end1, false, false), is(true));

        // range 2 had no end and range1 is before range2 start
        start1 = Instant.parse("2022-01-01T00:00:00.000000Z");
        end1 = Instant.parse("2022-01-02T00:00:00.000000Z");
        start2 = Instant.parse("2022-01-03T00:00:00.000000Z");
        end2 = null;
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, true, true), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, true), is(false));
        assertThat(JavaTimes.overlap(start1, end1, start2, end2, false, false), is(false));
    }
    
}