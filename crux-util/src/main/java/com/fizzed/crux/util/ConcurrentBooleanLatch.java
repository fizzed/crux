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
    private final Condition opened;
    private final Condition closed;
    private boolean open;

    /**
     * Creates a new instance in the "opened" initial state.
     */
    public ConcurrentBooleanLatch() {
        this(true);
    }
    
    /**
     * Creates a new instance in the specified initial state
     * @param open The initial open state
     */
    public ConcurrentBooleanLatch(boolean open) {
        this.lock = new ReentrantLock();
        this.opened = this.lock.newCondition();
        this.closed = this.lock.newCondition();
        this.open = open;
    }
    
    public boolean isOpened() {
        this.lock.lock();
        try {
            return this.open;
        } finally {
            this.lock.unlock();
        }
    }
    
    public boolean isClosed() {
        return !this.isOpened();
    }
    
    /**
     * Waits forever for the latch to be open(ed).
     * 
     * @throws InterruptedException 
     */
    public void awaitOpened() throws InterruptedException {
        this.awaitOpened(null);
    }
    
    public void awaitOpened(Runnable willWait) throws InterruptedException {
        this.lock.lockInterruptibly();
        try {
            // if closed then wait till its opened
            while (!this.open) {
                if (willWait != null) { willWait.run(); }
                this.opened.await();
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
    public void awaitClosed() throws InterruptedException {
        this.awaitClosed(null);
    }
    
    public void awaitClosed(Runnable willWait) throws InterruptedException {
        this.lock.lockInterruptibly();
        try {
            // if open then wait till its closed
            while (this.open) {
                if (willWait != null) { willWait.run(); }
                this.closed.await();
            }
        } finally {
            this.lock.unlock();
        }
    }
    
    public void open() {
        this.lock.lock();
        try {
            this.open = true;
            this.opened.signal();
        } finally {
            this.lock.unlock();
        }
    }
    
    public void close() {
        this.lock.lock();
        try {
            this.open = false;
            this.closed.signal();
        } finally {
            this.lock.unlock();
        }
    }
    
}