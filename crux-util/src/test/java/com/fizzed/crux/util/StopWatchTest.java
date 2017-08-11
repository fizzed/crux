/*
 * Copyright 2017 Fizzed, Inc.
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
import static org.hamcrest.Matchers.greaterThan;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void start() {
        long start = System.nanoTime();
        long elapsedMillis = 1100L;
        long elapsedNanos = TimeUnit.MILLISECONDS.toNanos(elapsedMillis);
        
        StopWatch sw = new StopWatch(TimeUnit.MILLISECONDS, start, start+elapsedNanos, true);
        
        assertThat(sw.isRunning(), is(false));
        assertThat(sw.elapsedNanos(), is(elapsedNanos));
        assertThat(sw.elapsedMillis(), is((double)elapsedMillis));
        assertThat(sw.elapsedSeconds(), is(1.1d));
        assertThat(sw.toString(), is("1100.0 ms"));
    }
    
    @Test
    public void elapsed() {
        long start = System.nanoTime();
        long elapsedMillis = 1100L;
        long elapsedNanos = TimeUnit.MILLISECONDS.toNanos(elapsedMillis);
        
        StopWatch sw = new StopWatch(TimeUnit.MILLISECONDS, start, start+elapsedNanos, true);
        
        assertThat(sw.elapsed(), is(1100d));
        assertThat(sw.gt(1100), is(false));
        assertThat(sw.gt(1099), is(true));
        assertThat(sw.gte(1100), is(true));
        assertThat(sw.lt(1100), is(false));
        assertThat(sw.lt(1101), is(true));
        assertThat(sw.lte(1100), is(true));
        assertThat(sw.eq(1100), is(true));
        assertThat(sw.eq(1101), is(false));
    }
    
    @Test
    public void format() {
        long start = System.nanoTime();
        long elapsedMillis = 1102L;
        long elapsedNanos = TimeUnit.MILLISECONDS.toNanos(elapsedMillis);
        
        StopWatch sw = new StopWatch(TimeUnit.SECONDS, start, start+elapsedNanos, true);
        
        assertThat(sw.isRunning(), is(false));
        assertThat(sw.elapsedNanos(), is(elapsedNanos));
        assertThat(sw.elapsedMillis(), is((double)elapsedMillis));
        assertThat(sw.toString(), is("1.10 s"));
        assertThat(sw.toString(TimeUnit.NANOSECONDS), is("1102000000 ns"));
        assertThat(sw.toString(TimeUnit.MICROSECONDS), is("1102000 mu"));
        assertThat(sw.toString(TimeUnit.MILLISECONDS), is("1102.0 ms"));
        assertThat(sw.toString(TimeUnit.SECONDS), is("1.10 s"));
        assertThat(sw.toString(TimeUnit.MINUTES), is("0.02 m"));
        assertThat(sw.toString(TimeUnit.HOURS), is("0.00 h"));
        assertThat(sw.toString(TimeUnit.DAYS), is("0.00 d"));
        
        // something more useful for days
        start = System.nanoTime();
        elapsedNanos = TimeUnit.DAYS.toNanos(3);
        
        sw = new StopWatch(TimeUnit.SECONDS, start, start+elapsedNanos, true);
        
        assertThat(sw.toString(TimeUnit.SECONDS), is("259200.00 s"));
        assertThat(sw.toString(TimeUnit.MINUTES), is("4320.00 m"));
        assertThat(sw.toString(TimeUnit.HOURS), is("72.00 h"));
        assertThat(sw.toString(TimeUnit.DAYS), is("3.00 d"));
        
    }
    
    @Test
    public void time() throws Exception {
        StopWatch time = StopWatch.timeMillis(() -> {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                // do nothing
            }
        });
        
        assertThat(time.isRunning(), is(false));
        assertThat(time.elapsedMillis(), greaterThan(50d));
    }
    
}
