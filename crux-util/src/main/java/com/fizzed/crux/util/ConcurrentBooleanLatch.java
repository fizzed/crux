package com.fizzed.crux.util;

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
     * Will either proceed immediately or wait until this latch is true.
     * 
     * @throws InterruptedException 
     */
    public void awaitTrue() throws InterruptedException {
        this.awaitTrue(null);
    }
    
    public void awaitTrue(Runnable willWait) throws InterruptedException {
        this.lock.lockInterruptibly();
        try {
            // if false then wait till its true
            while (!this.value) {
                if (willWait != null) { willWait.run(); }
                this.trueCondition.await();
            }
        } finally {
            this.lock.unlock();
        }
    }
    
    /**
     * Will either proceed immediately or wait until this latch is closed.
     * 
     * @throws InterruptedException 
     */
    public void awaitFalse() throws InterruptedException {
        this.awaitFalse(null);
    }
    
    public void awaitFalse(Runnable willWait) throws InterruptedException {
        this.lock.lockInterruptibly();
        try {
            // if true then wait till its false
            while (this.value) {
                if (willWait != null) { willWait.run(); }
                this.falseCondition.await();
            }
        } finally {
            this.lock.unlock();
        }
    }
    
}