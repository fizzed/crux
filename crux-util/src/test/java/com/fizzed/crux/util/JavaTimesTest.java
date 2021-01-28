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
    
}