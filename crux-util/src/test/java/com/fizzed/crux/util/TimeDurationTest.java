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

import static com.fizzed.crux.util.TimeDuration.millis;
import static com.fizzed.crux.util.TimeDuration.nanos;
import static com.fizzed.crux.util.TimeDuration.seconds;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import org.junit.Test;

/**
 *
 * @author jjlauer
 */
public class TimeDurationTest {
    
    @Test(expected=IllegalArgumentException.class)
    public void parseNoDefaultUnits() {
        TimeDuration.parse("10");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void parseUnknownUnits() {
        TimeDuration.parse("10X");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void parseNoDuration() {
        TimeDuration.parse("m");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void parseWithSpaces() {
        TimeDuration.parse("s ");
    }
    
    @Test
    public void parse() {
        TimeDuration td;
        
        td = TimeDuration.parse(null);
        assertThat(td, is(nullValue()));
        
        td = TimeDuration.parse("");
        assertThat(td, is(nullValue()));
        
        td = TimeDuration.parse("1s");
        assertThat(td.getDuration(), is(1L));
        assertThat(td.getUnit(), is(TimeUnit.SECONDS));
        assertThat(td.toString(), is("1s"));
        assertThat(td.asMillis(), is(1000L));
        assertThat(td.toMillis().toString(), is("1000ms"));
        assertThat(td.toSeconds().toString(), is("1s"));
        
        // default unit provided
        td = TimeDuration.parse("1", TimeUnit.SECONDS);
        assertThat(td.getDuration(), is(1L));
        assertThat(td.getUnit(), is(TimeUnit.SECONDS));
        assertThat(td.toString(), is("1s"));
        assertThat(td.asMillis(), is(1000L));
        assertThat(td.toMillis().toString(), is("1000ms"));
        assertThat(td.toSeconds().toString(), is("1s"));
        
        td = TimeDuration.parse("2MS");
        assertThat(td.getDuration(), is(2L));
        assertThat(td.getUnit(), is(TimeUnit.MILLISECONDS));
        assertThat(td.toString(), is("2ms"));
        assertThat(td.asMillis(), is(2L));
        assertThat(td.toMillis().toString(), is("2ms"));
        assertThat(td.toSeconds().toString(), is("0s"));
        
        td = TimeDuration.parse("999ms");
        assertThat(td.getDuration(), is(999L));
        assertThat(td.getUnit(), is(TimeUnit.MILLISECONDS));
        assertThat(td.toString(), is("999ms"));
        assertThat(td.asMillis(), is(999L));
        assertThat(td.toMillis().toString(), is("999ms"));
        assertThat(td.toSeconds().toString(), is("0s"));
        
        td = TimeDuration.parse("3d");
        assertThat(td.getDuration(), is(3L));
        assertThat(td.getUnit(), is(TimeUnit.DAYS));
        assertThat(td.toString(), is("3d"));

        td = TimeDuration.parse("512ms");
        assertThat(td.getDuration(), is(512L));
        assertThat(td.getUnit(), is(TimeUnit.MILLISECONDS));
        assertThat(td.toString(), is("512ms"));
        assertThat(td.asMillis(), is(512L));
        assertThat(td.toMillis().toString(), is("512ms"));
        assertThat(td.toSeconds().toString(), is("0s"));
    }
    
    @Test
    public void compareTo() {
        // same units
        assertThat(millis(1).compareTo(millis(1)), is(0));
        assertThat(millis(2).compareTo(millis(1)), greaterThan(0));
        assertThat(millis(0).compareTo(millis(1)), lessThan(0));
        // different units
        assertThat(seconds(1).compareTo(millis(1000)), is(0));
        assertThat(seconds(1).compareTo(millis(999)), greaterThan(0));
        assertThat(seconds(1).compareTo(millis(1001)), lessThan(0));
        assertThat(seconds(1).gt(millis(999)), is(true));
        assertThat(seconds(1).gte(millis(999)), is(true));
        assertThat(seconds(1).lt(millis(1001)), is(true));
        assertThat(seconds(1).lte(millis(1001)), is(true));
        
        assertThat(millis(3639).gt(nanos(0)), is(true));
        assertThat(millis(11113639).gt(nanos(0)), is(true));
        assertThat(millis(0).lt(nanos(1)), is(true));
        assertThat(millis(0).lt(nanos(100000000)), is(true));
    }
    
    @Test
    public void equals() {
        // same units
        assertThat(millis(1).equals(millis(1)), is(true));
        assertThat(millis(2).equals(millis(1)), is(false));
        assertThat(millis(0).equals(millis(1)), is(false));
        // different units
        assertThat(seconds(1).equals(millis(1000)), is(true));
    }
    
}