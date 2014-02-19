package com.github.skyao.showcase.guava.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.google.common.base.Stopwatch;

/**
 * show case for class base.Stopwatch.
 * 
 * @author Sky Ao
 *
 */
public class StopwatchShowcase {

    @Test
    public void createStarted() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        doSomething();
        stopwatch.stop();

        long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        long nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        System.out.println("time used = " + millis + " ms");
        System.out.println("time used = " + nanos + " ns");
    }

    private void doSomething() {
        try {
            Thread.sleep(0, 100 * 1000);
        } catch (InterruptedException e) {
        }
    }

    @Test
    public void elapsedAfterStop() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        doSomething();
        stopwatch.stop();

        long nanos1 = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        long nanos2 = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        long nanos3 = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        assertThat(nanos1).isEqualTo(nanos2).isEqualTo(nanos3);
    }

    @Test
    public void elapsedNotStop() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        doSomething();
        //stopwatch.stop();

        long nanos1 = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        long nanos2 = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        long nanos3 = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        assertThat(nanos1).isLessThan(nanos2);
        assertThat(nanos2).isLessThan(nanos3);
    }
}
