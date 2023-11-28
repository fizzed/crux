package com.fizzed.crux.util;

import org.junit.Test;

import static com.fizzed.crux.util.TimeDuration.millis;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static com.fizzed.crux.util.RunEvery.runEvery;

public class RunEveryTest {

    @Test
    public void testRunEvery() throws InterruptedException {
        MutableInteger runCount = new MutableInteger();

        // run every .5 secs
        RunEvery runEvery = runEvery(millis(500L));
        for (int i = 0; i < 12; i++) {
            runEvery.ifRunnable(runCount::increment);
            Thread.sleep(100L);
        }

        assertThat(runCount, is(2));
    }

}