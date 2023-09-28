package com.fizzed.crux.util;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * Helps to run code every X duration, where the last run time is recorded and will not be run again until the next
 * scheduled time.  Useful for logging a progress update every X seconds w/o regard to how much has been processed.
 */
public class RunEvery {

    private final long everyMillis;

    private long lastRunTime;

    public RunEvery(long everyMillis) {
        this.everyMillis = everyMillis;
        this.lastRunTime = System.currentTimeMillis();
    }

    static public RunEvery runEvery(long everyMillis) {
        return new RunEvery(everyMillis);
    }

    static public RunEvery runEvery(Duration everyDuration) {
        return new RunEvery(everyDuration.toMillis());
    }

    static public RunEvery runEvery(TimeDuration everyDuration) {
        return new RunEvery(everyDuration.asMillis());
    }

    public boolean isRunnable() {
        return (System.currentTimeMillis() - this.lastRunTime) >= this.everyMillis;
    }

    public void ifRunnable(Runnable runnable) {
        if (this.isRunnable()) {
            runnable.run();
            this.lastRunTime = System.currentTimeMillis();
        }
    }

    public void ifRunnable(Consumer<Long> runnable) {
        if (this.isRunnable()) {
            runnable.accept(this.lastRunTime);
            this.lastRunTime = System.currentTimeMillis();
        }
    }

}