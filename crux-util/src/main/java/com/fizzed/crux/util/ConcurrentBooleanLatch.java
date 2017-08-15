package com.fizzed.crux.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Similar to a CountDownLatch but allows another thread to open or close the
 * latch (resettable).  Unlike a semaphore this is a basic boolean.
 * 
 * @author jjlauer
 */
public class ConcurrentBooleanLatch {
 
    private final ReentrantLock lock;
    private final Condition trueCondition;
    private final Condition falseCondition;
    private boolean value;

    /**
     * Creates a new instance in the "opened" initial state.
     */
    public ConcurrentBooleanLatch() {
        this(true);
    }
    
    /**
     * Creates a new instance in the specified initial state
     * @param initialValue The initial state
     */
    public ConcurrentBooleanLatch(boolean initialValue) {
        this.lock = new ReentrantLock();
        this.trueCondition = this.lock.newCondition();
        this.falseCondition = this.lock.newCondition();
        this.value = initialValue;
    }

    public boolean get() {
        this.lock.lock();
        try {
            return this.value;
        } finally {
            this.lock.unlock();
        }
    }
    
    public void set(boolean newValue) {
        this.lock.lock();
        try {
            this.value = newValue;
            if (this.value) {
                this.trueCondition.signal();
            } else {
                this.falseCondition.signal();
            }
        } finally {
            this.lock.unlock();
        }
    }
    
    /**
     * Will either proceed immediately or wait until the value is set.
     * 
     * @param value The value we expect
     * @throws InterruptedException 
     */
    public void await(boolean value) throws InterruptedException {
        this.await(value, null);
    }
    
    /**
     * Will either proceed immediately or wait until the value is set.
     * 
     * @param value The value we expect
     * @param willWait Will be run only if waiting will be required
     * @throws InterruptedException 
     */
    public void await(boolean value, Runnable willWait) throws InterruptedException {
        this.lock.lockInterruptibly();
        try {
            final Condition condition = (value ? this.trueCondition : this.falseCondition);
            while (value != this.value) {
                if (willWait != null) { willWait.run(); }
                condition.await();
            }
        } finally {
            this.lock.unlock();
        }
    }
    
    /**
     * Will either proceed immediately or wait until the value is set.
     * 
     * @param value The value we expect
     * @param timeout Amount of time to wait
     * @param tu The unit for the timeout
     * @return True is the expected value was met or false if the timeout occurred
     * @throws InterruptedException 
     */
    public boolean await(boolean value, long timeout, TimeUnit tu) throws InterruptedException {
        return this.await(value, timeout, tu, null);
    }
    
    /**
     * Will either proceed immediately or wait until the value is set.
     * 
     * @param value The value we expect
     * @param timeout Amount of time to wait
     * @param tu The unit for the timeout
     * @param willWait Will be run only if waiting will be required
     * @return True is the expected value was met or false if the timeout occurred
     * @throws InterruptedException 
     */
    public boolean await(boolean value, long timeout, TimeUnit tu, Runnable willWait) throws InterruptedException {
        this.lock.lockInterruptibly();
        try {
            final Condition condition = (value ? this.trueCondition : this.falseCondition);
            while (value != this.value) {
                if (willWait != null) { willWait.run(); }
                if (!condition.await(timeout, tu)) {
                    return false;
                }
            }
            return true;
        } finally {
            this.lock.unlock();
        }
    }
    
}