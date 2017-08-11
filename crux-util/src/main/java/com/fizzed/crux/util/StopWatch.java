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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Lightweight timing for a little bit of anything. Uses System.nanoTime()
 * under-the-hood for highly precise measurements.  NOTE: nothing is synchronized
 * and should ideally only be used within a single thread.
 *  
 * @author joelauer
 */
public class StopWatch {

    private final TimeUnit tu;
    private final long start;
    // note: System.nanoTime() may return negative values so we must use
    // another variable to determine if the stopwatch is stopped yet
    private long stop;
    private boolean stopped;
    
    /**
     * Creates a stopwatch with a default time unit of milliseconds.
     */
    public StopWatch() {
        this(TimeUnit.MILLISECONDS, System.nanoTime(), -1, false);
    }
    
    /**
     * Creates a stopwatch with the specified time unit.
     * 
     * @param tu The time unit for formatting and comparison
     */
    public StopWatch(TimeUnit tu) {
        this(tu, System.nanoTime(), -1, false);
        Objects.requireNonNull(tu, "time unit was null");
    }
    
    // package-level for testing
    
    StopWatch(TimeUnit tu, long start, long stop, boolean stopped) {
        this.tu = tu;
        this.start = start;
        this.stop = stop;
        this.stopped = stopped;
    }

    /**
     * If still running (not stopped yet)
     * 
     * @return True if running or false if stopped
     */
    public boolean isRunning() {
        return !this.stopped;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }

    public StopWatch stop() {
        if (isRunning()) {
            this.stop = System.nanoTime();
            this.stopped = true;
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
    
    static public StopWatch time(TimeUnit tu) {
        return new StopWatch(tu);
    }
    
    static public StopWatch time(TimeUnit tu, Runnable runnable) {
        StopWatch watch = new StopWatch(tu);
        try {
            runnable.run();
        } finally {
            watch.stop();
        }
        return watch;
    }
    
    /**
     * Returns the elapsed time based on the time unit of this stopwatch.
     * @return The elapsed time
     */
    public double elapsed() {
        return elapsed(this.tu);
    }
    
    /**
     * Returns the elapsed time for the specified time unit.
     * @param tu The time unit
     * @return The elapsed time
     */
    public double elapsed(TimeUnit tu) {
        switch (tu) {
            case NANOSECONDS:
                return this.elapsedNanos();
            case MICROSECONDS:
                return this.elapsedMicros();
            case MILLISECONDS:
                return this.elapsedMillis();
            case SECONDS:
                return this.elapsedSeconds();
            case MINUTES:
                return this.elapsedMinutes();
            case HOURS:
                return this.elapsedHours();
            case DAYS:
                return this.elapsedDays();
            default:
                throw new UnsupportedOperationException("Unable to handle time unit " + tu);
        }
    }
    
    public boolean eq(double duration) {
        return elapsed() == duration;
    }
    
    public boolean gt(double duration) {
        return elapsed() > duration;
    }
    
    public boolean gte(double duration) {
        return elapsed() >= duration;
    }
    
    public boolean lt(double duration) {
        return elapsed() < duration;
    }
    
    public boolean lte(double duration) {
        return elapsed() <= duration;
    }
    
    
    
    public long elapsedNanos() {
        if (this.stopped) {
            return (this.stop - this.start);
        } else {
            return (System.nanoTime() - this.start);
        }  
    }
    
    /**
     * Returns the total time elapsed in microseconds. If the timer is still
     * running then this is based on current time.
     *
     * @return The total elapsed time in microseconds (mu)
     */
    public double elapsedMicros() {
        return (double)elapsedNanos() / 1000.0d;
    }
    
    /**
     * Returns the total time elapsed in milliseconds. If the timer is still
     * running then this is based on current time.
     *
     * @return The total elapsed time in milliseconds (ms)
     */
    public double elapsedMillis() {
        return (double)elapsedNanos() / 1000000.0d;
    }
    
    /**
     * Returns the total time elapsed in seconds. If the timer is still
     * running then this is based on current time.
     *
     * @return The total elapsed time in seconds (s)
     */
    public double elapsedSeconds() {
        return this.elapsedNanos() / 1000000000.0d;
    }
    
    /**
     * Returns the total time elapsed in minutes. If the timer is still
     * running then this is based on current time.
     *
     * @return The total elapsed time in minutes (m)
     */
    public double elapsedMinutes() {
        return this.elapsedSeconds() / 60.0d;
    }
    
    /**
     * Returns the total time elapsed in hours. If the timer is still
     * running then this is based on current time.
     *
     * @return The total elapsed time in hours (h)
     */
    public double elapsedHours() {
        return this.elapsedMinutes() / 60.0d;
    }
    
    /**
     * Returns the total time elapsed in days. If the timer is still
     * running then this is based on current time.
     *
     * @return The total elapsed time in days (d)
     */
    public double elapsedDays() {
        return this.elapsedHours() / 24.0d;
    }

    /**
     * Returns the current elapsed time in a format determined by the default time
     * unit of this instance.
     *
     * @return A string of the current elapsed time such as "10.1 ms" or "0.12 s"
     */
    @Override
    public String toString() {
        return toString(this.tu);
    }
    
    /**
     * Returns the number of seconds this Stopwatch has elapsed
     *
     * @return The String of the number of seconds
     */
    public String toString(TimeUnit tu) {
        Objects.requireNonNull(tu, "time unit was null");
        switch (tu) {
            case NANOSECONDS:
                return String.format("%d ns", this.elapsedNanos());
            case MICROSECONDS:
                return String.format("%.0f mu", this.elapsedMicros());
            case SECONDS:
                return String.format("%.2f s", this.elapsedSeconds());
            case MINUTES:
                return String.format("%.2f m", this.elapsedMinutes());
            case HOURS:
                return String.format("%.2f h", this.elapsedHours());
            case DAYS:
                return String.format("%.2f d", this.elapsedDays());
            case MILLISECONDS:
            default:
                // default to millis
                return String.format("%.1f ms", this.elapsedMillis());
            
        }
    }

}