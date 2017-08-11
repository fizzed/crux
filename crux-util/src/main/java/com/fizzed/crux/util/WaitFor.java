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
import java.util.function.Supplier;

/**
 * Basic abstraction of a spin-lock waiting for something to be true. Ideally
 * you'd use real concurrent mechanisms, but that's not always available for
 * things you need to evaluate.
 * 
 * @author joelauer
 */
public class WaitFor {

    private final Supplier<Boolean> condition;

    public WaitFor(Supplier<Boolean> condition) {
        this.condition = condition;
    }
    
    static public WaitFor of(Supplier<Boolean> condition) {
        return new WaitFor(condition);
    }
    
    static public void requireMillis(Supplier<Boolean> condition, long timeout, long every) throws InterruptedException, TimeoutException {
        new WaitFor(condition).requireMillis(timeout, every);
    }
    
    static public void require(Supplier<Boolean> condition, long timeout, long every, TimeUnit tu) throws InterruptedException, TimeoutException {
        new WaitFor(condition).require(timeout, every, tu);
    }
    
    public void requireMillis(long timeout, long every) throws InterruptedException, TimeoutException {
        this.require(timeout, every, TimeUnit.MILLISECONDS);
    }
    
    public void require(long timeout, long every, TimeUnit tu) throws InterruptedException, TimeoutException {
        if (!this.await(timeout, every, tu)) {
            throw new TimeoutException("condition failed within " + timeout + " " + tu);
        }
    }
    
    public boolean awaitMillis(long timeout, long every) throws InterruptedException {
        return this.await(timeout, every, TimeUnit.MILLISECONDS);
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public boolean await(long timeout, long every, TimeUnit tu) throws InterruptedException {
        Objects.requireNonNull(tu, "time unit was null");
        if (every > timeout) {
            throw new IllegalArgumentException("every must be <= timeout");
        }
        StopWatch timer = StopWatch.time(tu);
        while (timer.lt(timeout)) {
            if (!condition.get()) {
                Thread.sleep(tu.toMillis(every));
            } else {
                return true;
            }
        }
        return false;
    }
    
}