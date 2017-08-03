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

/**
 * Basic class for timing stuff.
 *  
 * @author joelauer
 */
public class StopWatch {

    private final TimeUnit tu;
    private final long start;
    private long stop;

    public StopWatch() {
        this(TimeUnit.MILLISECONDS, System.nanoTime(), -1);
    }
    
    public StopWatch(TimeUnit tu) {
        this(tu, System.nanoTime(), -1);
    }
    
    // package-level for testing
    
    StopWatch(TimeUnit tu, long start, long stop) {
        this.tu = tu;
        this.start = start;
        this.stop = stop;
    }

    /**
     * If still running (not stopped yet)
     * 
     * @return True if running or false if stopped
     */
    public boolean isRunning() {
        return this.stop < 0;
    }
    
    public boolean isStopped() {
        return !isRunning();
    }

    public StopWatch stop() {
        if (isRunning()) {
            this.stop = System.nanoTime();
        }
        return this;
    }
    
    static public StopWatch timeMillis() {
        return new StopWatch(TimeUnit.MILLISECONDS);
    }
    
    static public StopWatch timeMillis(Runnable runnable) {
        return time(TimeUnit.MILLISECONDS, runnable);
    }
    
    static public StopWatch timeNanos() {
        return new StopWatch(TimeUnit.NANOSECONDS);
    }
    
    static public StopWatch timeNanos(Runnable runnable) {
        return time(TimeUnit.NANOSECONDS, runnable);
    }
    
    static public StopWatch timeSeconds() {
        return new StopWatch(TimeUnit.SECONDS);
    }
    
    static public StopWatch timeSeconds(Runnable runnable) {
        return time(TimeUnit.SECONDS, runnable);
    }
    
    static private StopWatch time(TimeUnit tu, Runnable runnable) {
        StopWatch watch = new StopWatch(tu);
        try {
            runnable.run();
        } finally {
            watch.stop();
        }
        return watch;
    }
    
    public long elapsedNanos() {
        if (isRunning()) {
            return (System.nanoTime() - this.start);
        } else
            return (this.stop - this.start);
    }
    
    /**
     * Returns the total timeMillis elapsed in milliseconds. If running this is
 the running elapsed timeMillis otherwise its the stop-start timeMillis.
     *
     * @return The total timeMillis elapsed (in ms)
     */
    public double elapsedMillis() {
        return (double)elapsedNanos() / 1000000.0d;
    }
    
    /**
     * Returns the total timeMillis elapsed in milliseconds. If running this is
 the running elapsed timeMillis otherwise its the stop-start timeMillis.
     *
     * @return The total timeMillis elapsed (in ms)
     */
    public double elapsedSeconds() {
        return this.elapsedMillis() / 1000.0d;
    }

    /**
     * Returns the number of seconds this Stopwatch has elapsed
     *
     * @return The String of the number of seconds
     */
    @Override
    public String toString() {
        switch (this.tu) {
            case NANOSECONDS:
                return String.format("%d ns", this.elapsedNanos());
            case SECONDS:
                return String.format("%.2f s", this.elapsedSeconds());
            default:
                return String.format("%.1f ms", this.elapsedMillis());
            
        }
    }

}