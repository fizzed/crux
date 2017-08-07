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

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        StopWatch sw = new StopWatch(TimeUnit.MILLISECONDS, start, start+elapsedNanos);
        
        assertThat(sw.isRunning(), is(false));
        assertThat(sw.elapsedNanos(), is(elapsedNanos));
        assertThat(sw.elapsedMillis(), is((double)elapsedMillis));
        assertThat(sw.elapsedSeconds(), is(1.1d));
        assertThat(sw.toString(), is("1100.0 ms"));
    }
    
    @Test
    public void format() {
        long start = System.nanoTime();
        long elapsedMillis = 1102L;
        long elapsedNanos = TimeUnit.MILLISECONDS.toNanos(elapsedMillis);
        
        StopWatch sw = new StopWatch(TimeUnit.SECONDS, start, start+elapsedNanos);
        
        assertThat(sw.isRunning(), is(false));
        assertThat(sw.elapsedNanos(), is(elapsedNanos));
        assertThat(sw.elapsedMillis(), is((double)elapsedMillis));
        assertThat(sw.toString(), is("1.10 s"));
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
