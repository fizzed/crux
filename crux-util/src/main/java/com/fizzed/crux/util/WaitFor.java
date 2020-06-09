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
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Basic abstraction of a spin-lock waiting for something to be true. Ideally
 * you'd use real concurrent mechanisms, but that's not always available for
 * things you need to evaluate.
 * 
 * @author joelauer
 */
public class WaitFor {

    static public class Progress {
        
        final private int attempt;
        final private TimeDuration timeout;
        final private TimeDuration every;
        final private StopWatch timer;

        public Progress(int attempt, TimeDuration timeout, TimeDuration every, StopWatch timer) {
            this.attempt = attempt;
            this.timeout = timeout;
            this.every = every;
            this.timer = timer;
        }

        public int getAttempt() {
            return attempt;
        }

        public TimeDuration getTimeout() {
            return timeout;
        }

        public TimeDuration getEvery() {
            return every;
        }

        public StopWatch getTimer() {
            return timer;
        }
 
    }
    
    private final Function<Progress,Boolean> condition;
    
    public WaitFor(Supplier<Boolean> condition) {
        this.condition = c -> condition.get();
    }
    
    public WaitFor(Function<Progress,Boolean> condition) {
        this.condition = condition;
    }
    
    public void requireMillis(
            long timeout,
            long every) throws InterruptedException, TimeoutException {
        
        this.require(timeout, every, TimeUnit.MILLISECONDS);
    }
    
    public void require(
            long timeout,
            long every,
            TimeUnit tu) throws InterruptedException, TimeoutException {
        
        this.require(new TimeDuration(timeout, tu), new TimeDuration(every, tu));
    }
    
    public void require(
            TimeDuration timeout,
            TimeDuration every) throws InterruptedException, TimeoutException {
        
        if (!this.await(timeout, every)) {
            throw new TimeoutException("Condition failed within " + timeout);
        }
    }
    
    public boolean awaitMillis(
            long timeout,
            long every) throws InterruptedException {
        
        return this.await(timeout, every, TimeUnit.MILLISECONDS);
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public boolean await(
            long timeout,
            long every,
            TimeUnit tu) throws InterruptedException {
        
        return this.await(new TimeDuration(timeout, tu), new TimeDuration(every, tu));
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public boolean await(
            TimeDuration timeout,
            TimeDuration every) throws InterruptedException {
        
        Objects.requireNonNull(timeout, "timeout was null");
        Objects.requireNonNull(every, "every was null");
        
        if (every.gt(timeout)) {
            throw new IllegalArgumentException("every must be <= timeout");
        }
        
        final StopWatch timer = StopWatch.time(timeout.getUnit());
        int attempt = 1;
        
        while (timer.elapsedMillis() < timeout.asMillis()) {
            if (this.condition.apply(new Progress(attempt, timeout, every, timer))) {
                return true;
            }
            
            Thread.sleep(every.asMillis());
            attempt++;
        }
        
        return false;
    }
    
    // helpers
    
    static public WaitFor of(Supplier<Boolean> condition) {
        return new WaitFor(condition);
    }
    
    static public WaitFor of(Function<Progress,Boolean> condition) {
        return new WaitFor(condition);
    }
    
    static public void requireMillis(
            Supplier<Boolean> condition,
            long timeout,
            long every) throws InterruptedException, TimeoutException {
        
        new WaitFor(condition).requireMillis(timeout, every);
    }
    
    static public void require(
            TimeDuration timeout,
            TimeDuration every,
            Function<Progress,Boolean> condition) throws InterruptedException, TimeoutException {
        
        new WaitFor(condition).require(timeout, every);
    }
    
    static public void require(
            Supplier<Boolean> condition,
            long timeout,
            long every,
            TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        
        new WaitFor(condition).require(timeout, every, timeUnit);
    }
    
}