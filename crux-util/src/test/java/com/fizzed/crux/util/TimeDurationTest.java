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

import java.util.concurrent.TimeUnit;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
    
    public void parse() {
        TimeDuration td;
        
        td = TimeDuration.parse("1s");
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
    }
    
}